package client.compat;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.item.LightsaberItem;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Optional client-side compat for LambDynamicLights-style ports such as
 * Sodium/Embeddium Dynamic Lights (dynamiclightsreforged).
 *
 * The JSON item-light format can only assign constant luminance to an item.
 * Lightsabers need NBT-sensitive light, so this registers entity handlers that
 * return light only while the saber stack is actually active.
 */
@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class LightsaberDynamicLightsReforgedCompat {
    private static boolean registered;

    private LightsaberDynamicLightsReforgedCompat() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (registered || !isLambStyleDynamicLightsLoaded()) {
            return;
        }

        event.enqueueWork(LightsaberDynamicLightsReforgedCompat::registerHandlers);
    }

    private static boolean isLambStyleDynamicLightsLoaded() {
        ModList mods = ModList.get();
        return mods.isLoaded("dynamiclightsreforged")
                || mods.isLoaded("lambdynlights")
                || mods.isLoaded("ryoamiclights");
    }

    private static void registerHandlers() {
        if (registered) {
            return;
        }
        registered = true;

        try {
            Class<?> handlersClass = Class.forName("dev.lambdaurora.lambdynlights.api.DynamicLightHandlers");
            Class<?> handlerInterface = Class.forName("dev.lambdaurora.lambdynlights.api.DynamicLightHandler");
            Method registerMethod = findRegisterMethod(handlersClass, handlerInterface);

            Object playerHandler = createHandler(handlerInterface, source -> {
                if (source instanceof Player player) {
                    return Math.max(getActiveSaberLightLevel(player.getMainHandItem()), getActiveSaberLightLevel(player.getOffhandItem()));
                }
                return 0;
            });

            Object itemHandler = createHandler(handlerInterface, source -> {
                if (source instanceof ItemEntity itemEntity) {
                    return getActiveSaberLightLevel(itemEntity.getItem());
                }
                return 0;
            });

            registerMethod.invoke(null, EntityType.PLAYER, playerHandler);
            registerMethod.invoke(null, EntityType.ITEM, itemHandler);
            galaxyunderchaos.LOGGER.info("Registered Galaxy Under Chaos active-lightsaber handlers for LambDynamicLights-style dynamic lights.");
        } catch (Throwable throwable) {
            galaxyunderchaos.LOGGER.debug("Could not register LambDynamicLights-style lightsaber handlers. Dynamic lights will fall back to the installed mod defaults.", throwable);
        }
    }

    private static Method findRegisterMethod(Class<?> handlersClass, Class<?> handlerInterface) throws NoSuchMethodException {
        for (Method method : handlersClass.getMethods()) {
            if (!"registerDynamicLightHandler".equals(method.getName()) || method.getParameterCount() != 2) {
                continue;
            }

            Class<?>[] params = method.getParameterTypes();
            if (EntityType.class.isAssignableFrom(params[0]) && params[1].isAssignableFrom(handlerInterface)) {
                return method;
            }
        }

        throw new NoSuchMethodException("DynamicLightHandlers#registerDynamicLightHandler(EntityType, DynamicLightHandler)");
    }

    private static Object createHandler(Class<?> handlerInterface, LuminanceResolver resolver) {
        InvocationHandler invocationHandler = (proxy, method, args) -> switch (method.getName()) {
            case "getLuminance" -> resolver.getLuminance(args != null && args.length > 0 ? args[0] : null);
            case "isWaterSensitive" -> false;
            case "equals" -> proxy == (args != null && args.length > 0 ? args[0] : null);
            case "hashCode" -> System.identityHashCode(proxy);
            case "toString" -> "GalaxyUnderChaosActiveLightsaberDynamicLightHandler";
            default -> defaultValue(method.getReturnType());
        };

        return Proxy.newProxyInstance(
                handlerInterface.getClassLoader(),
                new Class<?>[]{handlerInterface},
                invocationHandler
        );
    }

    private static Object defaultValue(Class<?> type) {
        if (type == boolean.class) {
            return false;
        }
        if (type == byte.class) {
            return (byte) 0;
        }
        if (type == short.class) {
            return (short) 0;
        }
        if (type == int.class) {
            return 0;
        }
        if (type == long.class) {
            return 0L;
        }
        if (type == float.class) {
            return 0.0F;
        }
        if (type == double.class) {
            return 0.0D;
        }
        if (type == char.class) {
            return (char) 0;
        }
        return null;
    }

    private static int getActiveSaberLightLevel(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof LightsaberItem saber && saber.isActive(stack) ? 15 : 0;
    }

    @FunctionalInterface
    private interface LuminanceResolver {
        int getLuminance(Object source);
    }
}
