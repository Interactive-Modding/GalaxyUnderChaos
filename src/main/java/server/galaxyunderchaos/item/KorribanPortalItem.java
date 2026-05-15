package server.galaxyunderchaos.item;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import server.galaxyunderchaos.worldgen.dimension.ModDimensions;

public class KorribanPortalItem extends Item {

    public KorribanPortalItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            if (!HyperdriveUseHelper.canUseHyperdrive(serverPlayer)) {
                return InteractionResultHolder.fail(player.getItemInHand(hand));
            }
            BlockPos playerPos = HyperdriveUseHelper.getJumpPosition(serverPlayer); // Get the ship/current jump position
            handleKorribanPortal(serverPlayer, playerPos);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    private void handleKorribanPortal(ServerPlayer player, BlockPos pPos) {
        if (player.level() instanceof ServerLevel serverLevel) {
            MinecraftServer minecraftServer = serverLevel.getServer();
            ResourceKey<Level> targetDimension = player.level().dimension() == ModDimensions.KORRIBAN_LEVEL_KEY ?
                    Level.OVERWORLD : ModDimensions.KORRIBAN_LEVEL_KEY;

            ServerLevel targetServerLevel = minecraftServer.getLevel(targetDimension);
            if (targetServerLevel != null) {
                HyperdriveUseHelper.beginHyperspaceJump(player, targetServerLevel, pPos, () -> clearLandingArea(targetServerLevel, pPos));
            }
        }
    }
    private void clearLandingArea(ServerLevel world, BlockPos pos) {
        for (int dy = 0; dy <= 1; dy++) { // Clears only a 1x2 space
            BlockPos blockPos = pos.above(dy);
            world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
        }
    }
}