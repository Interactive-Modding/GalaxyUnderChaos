package server.galaxyunderchaos.compat;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.item.LightsaberItem;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Optional integration for AtomicStryker's Dynamic Lights.
 *
 * This intentionally does NOT place vanilla LightBlock blocks itself. Dynamic Lights owns
 * the fake light blocks and cleans them up safely. This handler only tells Dynamic Lights
 * when an active Galaxy Under Chaos saber should emit light.
 *
 * Notes:
 * - Dynamic Lights 1.20.1 only exposes a vanilla light level, not RGB colored light.
 * - The colored saber glow is still handled by the client blade renderer.
 * - Reflection keeps this class safe when Dynamic Lights is not installed.
 */
@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class LightsaberDynamicLightHandler {
    private static final int ACTIVE_SABER_LIGHT_LEVEL = 15;
    private static final Map<UUID, DynamicLightAdapter> ACTIVE_LIGHTS = new HashMap<>();

    private LightsaberDynamicLightHandler() {
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side != LogicalSide.SERVER || event.phase != TickEvent.Phase.END) {
            return;
        }

        Player player = event.player;
        if (!player.isAlive()) {
            removeLight(player.getUUID());
            return;
        }

        int lightLevel = Math.max(
                getActiveSaberLightLevel(player.getMainHandItem()),
                getActiveSaberLightLevel(player.getOffhandItem())
        );

        updateLight(player, lightLevel);
    }


    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        removeLight(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        if (event.getEntity() instanceof ItemEntity itemEntity) {
            updateLight(itemEntity, getActiveSaberLightLevel(itemEntity.getItem()));
        }
    }

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.side != LogicalSide.SERVER || event.phase != TickEvent.Phase.END) {
            return;
        }

        Iterator<Map.Entry<UUID, DynamicLightAdapter>> iterator = ACTIVE_LIGHTS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, DynamicLightAdapter> entry = iterator.next();
            DynamicLightAdapter adapter = entry.getValue();
            Entity entity = adapter.entity;

            if (!entity.isAlive()) {
                removeAdapter(adapter);
                iterator.remove();
                continue;
            }

            if (entity.level() != event.level || !(entity instanceof ItemEntity itemEntity)) {
                continue;
            }

            int lightLevel = getActiveSaberLightLevel(itemEntity.getItem());
            if (lightLevel <= 0) {
                removeAdapter(adapter);
                iterator.remove();
            } else {
                adapter.lightLevel = lightLevel;
            }
        }
    }

    private static int getActiveSaberLightLevel(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof LightsaberItem saber && saber.isActive(stack)) {
            return ACTIVE_SABER_LIGHT_LEVEL;
        }
        return 0;
    }

    private static void updateLight(Entity entity, int lightLevel) {
        if (!DynamicLightsBridge.isAvailable()) {
            return;
        }

        UUID uuid = entity.getUUID();
        DynamicLightAdapter existing = ACTIVE_LIGHTS.get(uuid);

        if (lightLevel <= 0) {
            if (existing != null) {
                removeAdapter(existing);
                ACTIVE_LIGHTS.remove(uuid);
            }
            return;
        }

        if (existing != null) {
            if (existing.level != entity.level()) {
                removeAdapter(existing);
                ACTIVE_LIGHTS.remove(uuid);
            } else {
                existing.entity = entity;
                existing.lightLevel = lightLevel;
                return;
            }
        }

        DynamicLightAdapter created = DynamicLightsBridge.create(entity, lightLevel);
        if (created != null && DynamicLightsBridge.add(created)) {
            ACTIVE_LIGHTS.put(uuid, created);
        }
    }

    private static void removeLight(UUID uuid) {
        DynamicLightAdapter existing = ACTIVE_LIGHTS.remove(uuid);
        removeAdapter(existing);
    }

    private static void removeAdapter(DynamicLightAdapter adapter) {
        if (adapter != null) {
            // If the external Dynamic Lights mod keeps a stale reference after removal,
            // make the proxy report zero light instead of leaving a permanent light source.
            adapter.disable();
            DynamicLightsBridge.remove(adapter);
        }
    }

    private static final class DynamicLightAdapter {
        private Entity entity;
        private Level level;
        private int lightLevel;
        private final Object proxy;

        private DynamicLightAdapter(Entity entity, int lightLevel, Class<?> sourceInterface) {
            this.entity = entity;
            this.level = entity.level();
            this.lightLevel = lightLevel;
            InvocationHandler handler = this::invoke;
            this.proxy = Proxy.newProxyInstance(
                    sourceInterface.getClassLoader(),
                    new Class<?>[]{sourceInterface},
                    handler
            );
        }

        private void disable() {
            this.lightLevel = 0;
        }

        private Object invoke(Object proxy, Method method, Object[] args) {
            return switch (method.getName()) {
                case "getAttachmentEntity" -> entity;
                case "getLightLevel" -> lightLevel;
                case "equals" -> proxy == args[0];
                case "hashCode" -> System.identityHashCode(proxy);
                case "toString" -> "GalaxyUnderChaosLightsaberDynamicLight[" + entity + ", level=" + lightLevel + "]";
                default -> throw new UnsupportedOperationException("Unsupported Dynamic Lights method: " + method.getName());
            };
        }
    }

    private static final class DynamicLightsBridge {
        private static boolean resolved;
        private static boolean available;
        private static Class<?> sourceInterface;
        private static Method addLightSource;
        private static Method removeLightSource;

        private DynamicLightsBridge() {
        }

        private static boolean isAvailable() {
            resolve();
            return available;
        }

        private static DynamicLightAdapter create(Entity entity, int lightLevel) {
            resolve();
            if (!available) {
                return null;
            }
            return new DynamicLightAdapter(entity, lightLevel, sourceInterface);
        }

        private static boolean add(DynamicLightAdapter adapter) {
            resolve();
            if (!available || adapter == null) {
                return false;
            }

            try {
                addLightSource.invoke(null, adapter.proxy);
                return true;
            } catch (ReflectiveOperationException ignored) {
                return false;
            }
        }

        private static void remove(DynamicLightAdapter adapter) {
            resolve();
            if (!available || adapter == null) {
                return;
            }

            try {
                removeLightSource.invoke(null, adapter.proxy);
            } catch (ReflectiveOperationException ignored) {
                // Optional compat only. If Dynamic Lights unloads/fails, do not crash GUC.
            }
        }

        private static void resolve() {
            if (resolved) {
                return;
            }
            resolved = true;

            if (!ModList.get().isLoaded("dynamiclights") && !ModList.get().isLoaded("dynamiclightsreforged")) {
                available = false;
                return;
            }

            try {
                Class<?> dynamicLightsClass = Class.forName("atomicstryker.dynamiclights.server.DynamicLights");
                sourceInterface = Class.forName("atomicstryker.dynamiclights.server.IDynamicLightSource");
                addLightSource = dynamicLightsClass.getMethod("addLightSource", sourceInterface);
                removeLightSource = dynamicLightsClass.getMethod("removeLightSource", sourceInterface);
                available = true;
            } catch (ReflectiveOperationException ignored) {
                available = false;
            }
        }
    }
}
