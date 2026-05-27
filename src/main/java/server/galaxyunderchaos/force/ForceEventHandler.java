package server.galaxyunderchaos.force;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.effect.ModEffects;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.entity.forceuser.PlayerForceIdentity;

@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID)
public class ForceEventHandler {
    private static final ResourceLocation FORCE_CAP_ID = new ResourceLocation(galaxyunderchaos.MODID, "force_state");

    private static final String FORCE_WORLD_SCOPE_KEY = "GUCForceJourneyWorldKey";
    private static final String FORCE_TRAINING_ROOT = "GUCForceTraining";
    private static final String FORCE_QUEST_ROOT = "GUCForceQuestInventory";
    private static final String FORCE_IDENTITY_ROOT = "GUCForceIdentity";

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (!player.getCapability(ForceProvider.FORCE_CAPABILITY).isPresent()) {
                event.addCapability(FORCE_CAP_ID, new ForceProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        CompoundTag oldTag = event.getOriginal().getCapability(ForceProvider.FORCE_CAPABILITY)
                .map(ForceCapability::serializeNBT)
                .orElse(new CompoundTag());
        event.getEntity().getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> cap.deserializeNBT(oldTag));
        copyForcePersistentData(event.getOriginal(), event.getEntity());
        if (event.getOriginal() instanceof ServerPlayer oldPlayer && event.getEntity() instanceof ServerPlayer newPlayer) {
            PlayerForceIdentity.copyIdentity(oldPlayer, newPlayer);
            PlayerForceIdentity.applyTitle(newPlayer);
            ForceCapabilityManager.sync(newPlayer);
        }
        event.getOriginal().invalidateCaps();
    }

    private static void copyForcePersistentData(Player oldPlayer, Player newPlayer) {
        CompoundTag oldData = oldPlayer.getPersistentData();
        CompoundTag newData = newPlayer.getPersistentData();
        copyCompoundRoot(oldData, newData, FORCE_TRAINING_ROOT);
        copyCompoundRoot(oldData, newData, FORCE_QUEST_ROOT);
        copyCompoundRoot(oldData, newData, FORCE_IDENTITY_ROOT);
        if (oldData.contains(FORCE_WORLD_SCOPE_KEY)) {
            newData.putString(FORCE_WORLD_SCOPE_KEY, oldData.getString(FORCE_WORLD_SCOPE_KEY));
        }
    }

    private static void copyCompoundRoot(CompoundTag oldData, CompoundTag newData, String key) {
        if (oldData.contains(key)) {
            newData.put(key, oldData.getCompound(key).copy());
        }
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            boolean reset = scopeForceJourneyToCurrentWorld(player);
            ForceCapabilityManager.sync(player);
            syncOrApplyIdentity(player);
            if (reset) {
                player.displayClientMessage(Component.literal("Force journey data was from another world and has been reset for this save."), true);
            }
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            scopeForceJourneyToCurrentWorld(player);
            ForceCapabilityManager.sync(player);
            syncOrApplyIdentity(player);
        }
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            scopeForceJourneyToCurrentWorld(player);
            ForceCapabilityManager.sync(player);
            syncOrApplyIdentity(player);
        }
    }


    /**
     * Keeps the player's Force progression tied to the current save folder.
     * Integrated-client sessions can keep the same UUID and client/server-side
     * persistent tags alive while moving between saves. Without this guard, the
     * quest ledger, mentor rank, title/species identity, and unlocked Force
     * powers can appear to bleed into every world the same account joins.
     */
    private static boolean scopeForceJourneyToCurrentWorld(ServerPlayer player) {
        CompoundTag persistent = player.getPersistentData();
        String currentWorldKey = currentWorldKey(player);
        String previousWorldKey = persistent.getString(FORCE_WORLD_SCOPE_KEY);

        if (!previousWorldKey.isBlank() && !previousWorldKey.equals(currentWorldKey)) {
            persistent.remove(FORCE_TRAINING_ROOT);
            persistent.remove(FORCE_QUEST_ROOT);
            persistent.remove(FORCE_IDENTITY_ROOT);
            player.setCustomName(null);
            player.setCustomNameVisible(false);
            player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> cap.deserializeNBT(new CompoundTag()));
            persistent.putString(FORCE_WORLD_SCOPE_KEY, currentWorldKey);
            return true;
        }

        if (previousWorldKey.isBlank()) {
            persistent.putString(FORCE_WORLD_SCOPE_KEY, currentWorldKey);
        }
        return false;
    }

    private static String currentWorldKey(ServerPlayer player) {
        if (player == null || player.server == null) {
            return "unknown";
        }
        try {
            return player.server.getWorldPath(LevelResource.ROOT).toAbsolutePath().normalize().toString();
        } catch (Exception ignored) {
            return player.server.getWorldData().getLevelName();
        }
    }

    /**
     * Always tell the client whether this world's player has a Force identity.
     *
     * Without this, the client-side render cache can keep the same UUID's old
     * robe/species selection from a previous world because worlds share the same
     * client session but not the same player data.
     */
    private static void syncOrApplyIdentity(ServerPlayer player) {
        if (PlayerForceIdentity.hasCustomIdentity(player)) {
            PlayerForceIdentity.applyTitle(player);
        } else {
            PlayerForceIdentity.syncIdentity(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) {
            return;
        }
        if (player.tickCount % 100 == 0) {
            PlayerForceIdentity.applyTitle(player);
        }
        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            if (ForcePowerHandler.isForceSuppressed(player)) {
                cap.stopUsingPower();
                cap.setUseCooldownTicks(Math.max(cap.getUseCooldownTicks(), 20));
                ForceCapabilityManager.sync(player);
                return;
            }
            cap.tickCooldowns();
            cap.tickAlignmentFlash();
            cap.tickVisual();
            ForcePowerHandler.tickUsingPower(player, cap);
            if (player.tickCount % 10 == 0) {
                ForcePowerHandler.tickSightHighlights(player);
            }
            if (player.tickCount % 5 == 0 && cap.getMaxForce() > 0) {
                float regen = cap.getRegenPerSecond() / 4.0F;
                if (ModEffects.hasAnyForceEffect(player, ForcePower.MEDITATION1, ForcePower.MEDITATION2, ForcePower.MEDITATION3)) {
                    regen += 1.5F;
                }
                cap.addForce(regen);
            }
            int syncRate = cap.isUsingPower() ? 2 : 10;
            if (cap.isDirty() && player.tickCount % syncRate == 0) {
                ForceCapabilityManager.sync(player);
            }
        });
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            if (ForcePowerHandler.isTutaminisActive(player) && isTutaminisDamage(event.getSource())) {
                float absorbed = event.getAmount() * 0.75F;
                event.setAmount(Math.max(0.0F, event.getAmount() - absorbed));
                cap.addForce(Math.max(1.0F, absorbed * 1.25F));
                if (player.tickCount % 6 == 0) {
                    ForcePowerHandler.spawnTutaminisVisual(player);
                }
                ForceCapabilityManager.sync(player);
            }

            if (cap.hasPower(ForcePower.REBOUND) && cap.getReboundCooldownTicks() <= 0 && cap.getCurrentForce() >= 10.0F && event.getSource().getEntity() instanceof LivingEntity attacker) {
                cap.consumeForce(10.0F);
                cap.setReboundCooldownTicks(40);
                event.setAmount(Math.max(0.0F, event.getAmount() - 2.0F));
                attacker.push(attacker.getX() - player.getX(), 0.25D, attacker.getZ() - player.getZ());
                net.minecraft.world.effect.MobEffect reboundEffect = ModEffects.getForceEffect(ForcePower.REBOUND);
                if (reboundEffect != null) {
                    player.addEffect(new MobEffectInstance(reboundEffect, 40, 0, false, false, true));
                }
                attacker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 2, false, false, false));
                ForcePowerHandler.spawnReboundVisual(player, attacker);
                player.displayClientMessage(Component.literal("Force Rebound triggered."), true);
                ForceCapabilityManager.sync(player);
            }
        });
    }


    private static boolean isTutaminisDamage(DamageSource source) {
        if (source == null) {
            return false;
        }
        String id = source.getMsgId();
        return source.is(DamageTypeTags.IS_FIRE)
                || source.is(DamageTypeTags.IS_EXPLOSION)
                || source.is(DamageTypeTags.IS_PROJECTILE)
                || id.contains("magic")
                || id.contains("lightning")
                || id.contains("sonic")
                || id.contains("laser")
                || id.contains("force");
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        ForceCommand.register(event.getDispatcher());
    }

    @Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBus {
        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(ForceCapability.class);
        }
    }
}
