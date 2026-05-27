package server.galaxyunderchaos.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import server.galaxyunderchaos.entity.forceuser.ForceUserDialogueEngine;
import server.galaxyunderchaos.force.ForceCapabilityManager;
import server.galaxyunderchaos.force.ForceProvider;
import server.galaxyunderchaos.force.ForceSide;

public class ForceLoreHolobookItem extends Item {
    private final ForceSide side;

    public ForceLoreHolobookItem(ForceSide side, Properties properties) {
        super(properties);
        this.side = side;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            long seed = stack.getOrCreateTag().getLong("LoreSeed");
            if (seed == 0L) {
                seed = level.random.nextLong();
                stack.getOrCreateTag().putLong("LoreSeed", seed);
            }
            serverPlayer.displayClientMessage(ForceUserDialogueEngine.holobookStory(side, seed), false);
            serverPlayer.displayClientMessage(Component.literal("Knowledge gained: +4 " + label(side) + " Force points. The holobook's pages fade after being absorbed."), true);
            serverPlayer.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
                cap.addAlignmentPoints(side, 4);
                ForceCapabilityManager.sync(serverPlayer);
            });
            level.playSound(null, serverPlayer.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 0.9F, 1.0F);
            if (!serverPlayer.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private static String label(ForceSide side) {
        return switch (side) {
            case LIGHT -> "light-side";
            case DARK -> "dark-side";
            case NEUTRAL -> "neutral knowledge";
            default -> "Force";
        };
    }
}
