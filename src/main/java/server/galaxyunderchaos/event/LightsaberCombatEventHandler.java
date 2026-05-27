package server.galaxyunderchaos.event;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.force.ForcePowerHandler;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.item.LightsaberItem;
import server.galaxyunderchaos.lightsaber.LightsaberFormCapabilityManager;
import server.galaxyunderchaos.lightsaber.LightsaberFormEffects;
import server.galaxyunderchaos.lightsaber.LightsaberFormProvider;
import server.galaxyunderchaos.sound.ModSounds;

@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LightsaberCombatEventHandler {
    private static final int BASE_GUARD_REGEN_PER_TICK = 1;
    private static final int GUARD_CONTACT_RECOVERY_DELAY = 80;

    /*
     * Guard drain used to be based on very large multipliers (damage × 7-10),
     * which made a single active lightsaber hit or Force power delete the entire
     * guard bar. Keep the raw damage separate from guard pressure so blocking is
     * actually usable in saber duels and against lightning.
     */
    private static final float SABER_GUARD_DRAIN_PER_DAMAGE = 0.35F;
    private static final float ABILITY_GUARD_DRAIN_PER_DAMAGE = 0.22F;
    private static final float PROJECTILE_GUARD_DRAIN_PER_DAMAGE = 0.15F;
    private static final int SABER_GUARD_MIN_COST = 3;
    private static final int ABILITY_GUARD_MIN_COST = 2;
    private static final int PROJECTILE_GUARD_MIN_COST = 1;

    private LightsaberCombatEventHandler() {}

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack held = player.getItemInHand(event.getHand());
        if (!(held.getItem() instanceof LightsaberItem saber) || !saber.isActive(held)) {
            return;
        }

        if (!canUseGuard(player)) {
            return;
        }

        player.startUsingItem(event.getHand());
        event.setUseBlock(Event.Result.DENY);
        event.setUseItem(Event.Result.DENY);
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) {
            return;
        }

        Player player = event.player;
        player.getCapability(LightsaberFormProvider.LIGHTSABER_FORM_CAPABILITY).ifPresent(cap -> {
            String form = cap.getSelectedForm();
            cap.setMaxGuardStamina(LightsaberFormEffects.getMaxStaminaForForm(form));
            cap.tickLastGuardContact();

            boolean holdingActiveSaber = isHoldingActiveSaber(player);
            boolean blocking = isBlockingWithSaber(player);

            if (holdingActiveSaber || blocking) {
                cap.setStaminaVisibleTicks(Math.max(cap.getStaminaVisibleTicks(), 40));
            }

            float moveDrain = LightsaberFormEffects.getMovementDrainMultiplier(form);

            if (holdingActiveSaber && player.isSprinting() && player.tickCount % 8 == 0) {
                cap.setGuardStamina(cap.getGuardStamina() - Math.max(1, Math.round(2 * moveDrain)));
            }

            if (holdingActiveSaber && !player.onGround() && player.getDeltaMovement().y > 0.08D && player.tickCount % 6 == 0) {
                cap.setGuardStamina(cap.getGuardStamina() - Math.max(1, Math.round(3 * moveDrain)));
            }

            boolean canRegen = !holdingActiveSaber
                    || (!blocking && cap.getLastGuardContactTicks() >= GUARD_CONTACT_RECOVERY_DELAY)
                    || (blocking && cap.getLastGuardContactTicks() >= GUARD_CONTACT_RECOVERY_DELAY);

            if (canRegen && cap.getGuardStamina() < cap.getMaxGuardStamina()) {
                float regen = BASE_GUARD_REGEN_PER_TICK * LightsaberFormEffects.getRegenMultiplier(form);
                if (!holdingActiveSaber) {
                    regen += 1.0F;
                }
                cap.setGuardStamina(Math.min(cap.getMaxGuardStamina(), cap.getGuardStamina() + Math.max(1, Math.round(regen))));
            }

            if (event.player instanceof net.minecraft.server.level.ServerPlayer sp && (cap.isDirty() || sp.tickCount % 10 == 0 && cap.isStaminaVisible())) {
                LightsaberFormCapabilityManager.syncCapability(sp);
            }
        });
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide) {
            return;
        }

        Entity incoming = event.getSource().getDirectEntity();
        if (incoming == null) {
            incoming = event.getSource().getEntity();
        }

        if (incoming != null && ForcePowerHandler.isTutaminisActive(target) && isActiveSaberAttack(incoming)) {
            event.setCanceled(true);
            target.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                    ModSounds.LIGHTSABER_DEFLECT.get(), SoundSource.PLAYERS, 0.85F, 1.45F);
            if (target instanceof net.minecraft.server.level.ServerPlayer defender) {
                ForcePowerHandler.spawnTutaminisVisual(defender);
            }
            if (incoming instanceof LivingEntity attacker) {
                attacker.invulnerableTime = 0;
                attacker.hurt(target.damageSources().indirectMagic(target, target), Math.max(1.0F, event.getAmount() * 0.35F));
            }
            return;
        }

        if (!(target instanceof Player player)) {
            return;
        }

        ItemStack using = player.getUseItem();
        if (!(using.getItem() instanceof LightsaberItem saber) || !saber.isActive(using)) {
            return;
        }

        if (incoming == null || !isFrontFacingGuard(player, incoming)) {
            return;
        }

        final Entity finalIncoming = incoming;
        player.getCapability(LightsaberFormProvider.LIGHTSABER_FORM_CAPABILITY).ifPresent(cap -> {
            float formCost = LightsaberFormEffects.getBlockCostMultiplier(cap.getSelectedForm());
            boolean saberVsSaber = isActiveSaberAttack(finalIncoming);
            boolean projectile = finalIncoming instanceof Projectile;

            float drainPerDamage = saberVsSaber
                    ? SABER_GUARD_DRAIN_PER_DAMAGE
                    : projectile ? PROJECTILE_GUARD_DRAIN_PER_DAMAGE : ABILITY_GUARD_DRAIN_PER_DAMAGE;
            int minCost = saberVsSaber
                    ? SABER_GUARD_MIN_COST
                    : projectile ? PROJECTILE_GUARD_MIN_COST : ABILITY_GUARD_MIN_COST;
            int staminaCost = Math.max(minCost, Mth.ceil(event.getAmount() * drainPerDamage * formCost));
            SoundEvent guardSound = saberVsSaber ? ModSounds.LIGHTSABER_HIT.get() : ModSounds.LIGHTSABER_DEFLECT.get();

            if (consumeGuard(player, staminaCost, guardSound, saberVsSaber ? 1.0F : 0.85F, saberVsSaber ? 0.92F : 1.0F)) {
                event.setCanceled(true);
                reflectProjectile(player, finalIncoming);
            }
        });
    }

    public static boolean tryBlockForceLightning(Player defender, Entity caster, float damage, int tier) {
        if (defender == null || defender.level().isClientSide) {
            return false;
        }

        if (!isBlockingWithSaber(defender)) {
            return false;
        }

        if (caster != null && !isFrontFacingGuard(defender, caster)) {
            return false;
        }

        float formCost = defender.getCapability(LightsaberFormProvider.LIGHTSABER_FORM_CAPABILITY)
                .map(cap -> LightsaberFormEffects.getBlockCostMultiplier(cap.getSelectedForm()))
                .orElse(1.0F);
        int staminaCost = Math.max(1, Mth.ceil((0.75F + tier * 0.45F) * formCost));
        return consumeGuard(defender, staminaCost, ModSounds.LIGHTSABER_DEFLECT.get(), 0.70F, 1.25F + tier * 0.10F);
    }

    private static boolean consumeGuard(Player player, int staminaCost, SoundEvent sound, float volume, float pitch) {
        final boolean[] blocked = {false};
        player.getCapability(LightsaberFormProvider.LIGHTSABER_FORM_CAPABILITY).ifPresent(cap -> {
            int currentStamina = cap.getGuardStamina();
            if (currentStamina <= 0) {
                return;
            }

            if (currentStamina >= staminaCost) {
                cap.setGuardStamina(currentStamina - staminaCost);
                blocked[0] = true;
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        sound, SoundSource.PLAYERS, volume, pitch);
            } else {
                cap.setGuardStamina(0);
            }

            cap.markGuardContact();
            if (player instanceof net.minecraft.server.level.ServerPlayer sp) {
                LightsaberFormCapabilityManager.syncCapability(sp);
            }
        });
        return blocked[0];
    }

    public static boolean canUseGuard(Player player) {
        return player.getCapability(LightsaberFormProvider.LIGHTSABER_FORM_CAPABILITY)
                .map(cap -> cap.getGuardStamina() > 0)
                .orElse(true);
    }

    public static int getGuardStamina(Player player) {
        return player.getCapability(LightsaberFormProvider.LIGHTSABER_FORM_CAPABILITY)
                .map(server.galaxyunderchaos.lightsaber.LightsaberFormCapability::getGuardStamina)
                .orElse(100);
    }

    private static boolean isHoldingActiveSaber(Player player) {
        return isActiveSaber(player.getMainHandItem()) || isActiveSaber(player.getOffhandItem());
    }

    private static boolean isBlockingWithSaber(Player player) {
        return player.isUsingItem() && isActiveSaber(player.getUseItem());
    }

    private static boolean isActiveSaberAttack(Entity incoming) {
        if (!(incoming instanceof LivingEntity attacker)) {
            return false;
        }
        return isActiveSaber(attacker.getMainHandItem()) || isActiveSaber(attacker.getOffhandItem());
    }

    private static boolean isActiveSaber(ItemStack stack) {
        return stack.getItem() instanceof LightsaberItem saber && saber.isActive(stack);
    }

    private static boolean isFrontFacingGuard(Player player, Entity incoming) {
        Vec3 forward = player.getLookAngle().normalize();
        Vec3 incomingVector;

        if (incoming instanceof Projectile projectile) {
            incomingVector = projectile.getDeltaMovement();
            if (incomingVector.lengthSqr() < 1.0E-6D) {
                incomingVector = projectile.position().subtract(player.position());
            } else {
                incomingVector = incomingVector.scale(-1.0D);
            }
        } else {
            incomingVector = incoming.position().subtract(player.position());
        }

        if (incomingVector.lengthSqr() < 1.0E-6D) {
            return false;
        }

        double dot = forward.dot(incomingVector.normalize());
        return dot > -0.10D;
    }

    private static void reflectProjectile(Player player, Entity incoming) {
        if (!(incoming instanceof Projectile projectile)) {
            return;
        }

        Vec3 forward = player.getLookAngle().normalize();
        double speed = Math.max(0.8D, projectile.getDeltaMovement().length());
        projectile.setDeltaMovement(forward.scale(speed * 1.10D));
        projectile.hurtMarked = true;
    }
}
