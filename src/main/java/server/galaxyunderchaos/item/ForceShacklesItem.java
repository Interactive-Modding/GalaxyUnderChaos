package server.galaxyunderchaos.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import server.galaxyunderchaos.force.ForcePowerHandler;
import server.galaxyunderchaos.force.ForceTargeting;

public class ForceShacklesItem extends Item {
    public ForceShacklesItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }
        LivingEntity target = ForceTargeting.findTarget(player, 5.0D, 1.35D);
        if (target == null) {
            player.displayClientMessage(Component.literal("No Force user close enough to shackle."), true);
            return InteractionResultHolder.fail(stack);
        }
        if (!ForcePowerHandler.canApplyForceShackles(target)) {
            player.displayClientMessage(Component.literal("Force Shackles only bind low-health living Force users. Ghosts cannot be shackled."), true);
            return InteractionResultHolder.fail(stack);
        }
        if (!ForcePowerHandler.applyForceShackles(target, player, 20 * 45, 0)) {
            player.displayClientMessage(Component.literal("The shackles fail to bind this target."), true);
            return InteractionResultHolder.fail(stack);
        }
        level.playSound(null, target.blockPosition(), SoundEvents.CHAIN_PLACE, SoundSource.PLAYERS, 0.9F, 0.75F);
        if (!player.getAbilities().instabuild) {
            stack.hurtAndBreak(1, player, breaker -> breaker.broadcastBreakEvent(hand));
        }
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.getCooldowns().addCooldown(this, 60);
        }
        return InteractionResultHolder.consume(stack);
    }
}
