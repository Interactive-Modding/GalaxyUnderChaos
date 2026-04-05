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

public class IlumPortalItem extends Item {

    public IlumPortalItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            BlockPos playerPos = serverPlayer.blockPosition(); // Use player's current position
            clearLandingArea((ServerLevel) serverPlayer.level(), playerPos);
            handleIlumPortal(serverPlayer, playerPos);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    private void handleIlumPortal(ServerPlayer player, BlockPos pPos) {
        if (player.level() instanceof ServerLevel serverLevel) {
            MinecraftServer minecraftServer = serverLevel.getServer();
            ResourceKey<Level> targetDimension = player.level().dimension() == ModDimensions.ILUM_LEVEL_KEY ?
                    Level.OVERWORLD : ModDimensions.ILUM_LEVEL_KEY;

            ServerLevel targetServerLevel = minecraftServer.getLevel(targetDimension);
            if (targetServerLevel != null && !player.isPassenger()) {
                clearLandingArea(targetServerLevel, pPos);
                player.teleportTo(targetServerLevel, pPos.getX() + 0.5, pPos.getY(), pPos.getZ() + 0.5, player.getYRot(), player.getXRot());

                if (targetDimension == ModDimensions.ILUM_LEVEL_KEY) {
                    forceSnowstorm(targetServerLevel);
                } else {
                    resetWeather(targetServerLevel); // Reset weather when leaving Ilum
                }
            }
        }
    }

    private void forceSnowstorm(ServerLevel level) {
        level.setWeatherParameters(99999, 99999, true, false); // Keeps constant snowstorm
    }

    private void resetWeather(ServerLevel level) {
        level.setWeatherParameters(0, 0, false, false); // Clears weather upon leaving
    }

    private void clearLandingArea(ServerLevel world, BlockPos pos) {
        for (int dy = 0; dy <= 1; dy++) { // Clears only a 1x2 space
            BlockPos blockPos = pos.above(dy);
            world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
        }
    }
}