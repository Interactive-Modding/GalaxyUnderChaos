package server.galaxyunderchaos.force;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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

@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID)
public class ForceEventHandler {
    private static final ResourceLocation FORCE_CAP_ID = new ResourceLocation(galaxyunderchaos.MODID, "force_state");

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
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ForceCapabilityManager.sync(player);
        }
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ForceCapabilityManager.sync(player);
        }
    }

    @SubscribeEvent
    public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ForceCapabilityManager.sync(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) {
            return;
        }
        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            cap.tickCooldowns();
            cap.tickAlignmentFlash();
            cap.tickVisual();
            ForcePowerHandler.tickUsingPower(player, cap);
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
            if (cap.hasPower(ForcePower.REBOUND) && cap.getReboundCooldownTicks() <= 0 && cap.getCurrentForce() >= 10.0F && event.getSource().getEntity() instanceof LivingEntity attacker) {
                cap.consumeForce(10.0F);
                cap.setReboundCooldownTicks(40);
                event.setAmount(Math.max(0.0F, event.getAmount() - 2.0F));
                attacker.push(attacker.getX() - player.getX(), 0.25D, attacker.getZ() - player.getZ());
                net.minecraft.world.effect.MobEffect reboundEffect = ModEffects.getForceEffect(ForcePower.REBOUND);
                if (reboundEffect != null) {
                    player.addEffect(new MobEffectInstance(reboundEffect, 40, 0, false, true, true));
                }
                attacker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 2, false, false, false));
                player.displayClientMessage(Component.literal("Force Rebound triggered."), true);
                ForceCapabilityManager.sync(player);
            }
        });
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
