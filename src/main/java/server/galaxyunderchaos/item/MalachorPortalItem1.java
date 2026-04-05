package server.galaxyunderchaos.item;

import client.renderer.HyperspaceManager;
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
import server.galaxyunderchaos.worldgen.dimension.ModDimensions;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MalachorPortalItem1 extends Item {

    public MalachorPortalItem1(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            BlockPos playerPos = serverPlayer.blockPosition(); // Get the player's current position
            handleMalachorPortal(serverPlayer, playerPos);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private void handleMalachorPortal(ServerPlayer player, BlockPos pPos) {
        if (player.level() instanceof ServerLevel serverLevel) {
            MinecraftServer minecraftServer = serverLevel.getServer();
            ResourceKey<Level> targetDimension = player.level().dimension() == ModDimensions.MALACHOR_LEVEL_KEY ?
                    Level.OVERWORLD : ModDimensions.MALACHOR_LEVEL_KEY;

            ServerLevel targetServerLevel = minecraftServer.getLevel(targetDimension);
            if (targetServerLevel != null && !player.isPassenger()) {
                // Start the hyperspace effect
                HyperspaceManager.startHyperspace(player, targetServerLevel, pPos, player.getYRot());

                // Delay teleportation to sync with the hyperspace effect
                scheduler.schedule(() -> {
                    player.getServer().execute(() -> { // Ensure this runs on the main thread
                        player.teleportTo(targetServerLevel, pPos.getX(), pPos.getY(), pPos.getZ(), player.getYRot(), player.getXRot());
                    });
                }, 5, TimeUnit.SECONDS); // 5-second delay for hyperspace animation
            }
        }
    }

}