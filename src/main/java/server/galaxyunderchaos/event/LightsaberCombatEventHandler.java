package server.galaxyunderchaos.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
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
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.item.LightsaberItem;
import server.galaxyunderchaos.sound.ModSounds;

@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LightsaberCombatEventHandler {
    private static final String GUARD_STAMINA_TAG = "gucLightsaberGuardStamina";
    private static final String GUARD_COOLDOWN_TAG = "gucLightsaberGuardCooldown";
    private static final int MAX_GUARD_STAMINA = 100;
    private static final int GUARD_REGEN_PER_TICK = 1;
    private static final int GUARD_COOLDOWN_TICKS = 14;

    private LightsaberCombatEventHandler() {}

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack held = player.getItemInHand(event.getHand());
        if (!(held.getItem() instanceof LightsaberItem saber) || !saber.isActive(held)) {
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
        CompoundTag data = player.getPersistentData();
        int cooldown = data.getInt(GUARD_COOLDOWN_TAG);
        if (cooldown > 0) {
            data.putInt(GUARD_COOLDOWN_TAG, cooldown - 1);
            return;
        }

        int current = getGuardStamina(player);
        if (current < MAX_GUARD_STAMINA) {
            setGuardStamina(player, Math.min(MAX_GUARD_STAMINA, current + GUARD_REGEN_PER_TICK));
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        LivingEntity target = event.getEntity();
        if (!(target instanceof Player player) || player.level().isClientSide) {
            return;
        }

        ItemStack using = player.getUseItem();
        if (!(using.getItem() instanceof LightsaberItem saber) || !saber.isActive(using)) {
            return;
        }

        Entity incoming = event.getSource().getDirectEntity();
        if (incoming == null) {
            incoming = event.getSource().getEntity();
        }
        if (incoming == null) {
            return;
        }

        if (!isFrontFacingGuard(player, incoming)) {
            return;
        }

        int currentStamina = getGuardStamina(player);
        int staminaCost = Math.max(8, Mth.ceil(event.getAmount() * 7.0F));
        if (currentStamina <= 0) {
            return;
        }

        if (currentStamina >= staminaCost) {
            event.setCanceled(true);
            setGuardStamina(player, currentStamina - staminaCost);
            player.getPersistentData().putInt(GUARD_COOLDOWN_TAG, GUARD_COOLDOWN_TICKS);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    ModSounds.LIGHTSABER_DEFLECT.get(), SoundSource.PLAYERS, 0.85F, 1.0F);
            reflectProjectile(player, incoming);
        } else {
            setGuardStamina(player, 0);
            player.getPersistentData().putInt(GUARD_COOLDOWN_TAG, GUARD_COOLDOWN_TICKS + 6);
        }
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
        return dot > 0.15D;
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

    public static int getGuardStamina(Player player) {
        CompoundTag data = player.getPersistentData();
        if (!data.contains(GUARD_STAMINA_TAG)) {
            data.putInt(GUARD_STAMINA_TAG, MAX_GUARD_STAMINA);
        }
        return data.getInt(GUARD_STAMINA_TAG);
    }

    public static void setGuardStamina(Player player, int value) {
        player.getPersistentData().putInt(GUARD_STAMINA_TAG, Mth.clamp(value, 0, MAX_GUARD_STAMINA));
    }
}
