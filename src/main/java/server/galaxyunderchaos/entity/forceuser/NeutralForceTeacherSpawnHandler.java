package server.galaxyunderchaos.entity.forceuser;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import server.galaxyunderchaos.galaxyunderchaos;

@Mod.EventBusSubscriber(modid = galaxyunderchaos.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class NeutralForceTeacherSpawnHandler {
    private static final int CHECK_INTERVAL_TICKS = 240;
    private static final int SITE_SCAN_RADIUS = 18;
    private static final int SITE_SCAN_Y = 8;
    private static final int MAX_NEUTRAL_GUIDES_NEAR_SITE = 2;

    private NeutralForceTeacherSpawnHandler() {}

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player) || !(player.level() instanceof ServerLevel level)) {
            return;
        }
        if (player.tickCount % CHECK_INTERVAL_TICKS != 0 || level.getRandom().nextFloat() > 0.18F) {
            return;
        }
        BlockPos site = findAncientSiteBlock(level, player.blockPosition());
        if (site == null) {
            return;
        }
        AABB area = new AABB(site).inflate(48.0D, 24.0D, 48.0D);
        int existing = level.getEntitiesOfClass(ForceUserEntity.class, area, e -> e.isAlive() && e.getForceUserSide().isNeutral()).size();
        if (existing >= MAX_NEUTRAL_GUIDES_NEAR_SITE) {
            return;
        }

        EntityType<ForceUserEntity> type;
        float roll = level.getRandom().nextFloat();
        if (roll < 0.12F) {
            type = galaxyunderchaos.NEUTRAL_MASTER.get();
        } else if (roll < 0.42F) {
            type = galaxyunderchaos.NEUTRAL_PADAWAN.get();
        } else {
            type = galaxyunderchaos.NEUTRAL_FORCE_USER.get();
        }

        ForceUserEntity guide = type.create(level);
        if (guide == null) {
            return;
        }
        BlockPos spawn = site.offset(level.getRandom().nextInt(9) - 4, 2, level.getRandom().nextInt(9) - 4);
        BlockPos.MutableBlockPos cursor = spawn.mutable();
        for (int i = 0; i < 8 && !level.getBlockState(cursor).isAir(); i++) {
            cursor.move(0, 1, 0);
        }
        guide.moveTo(cursor.getX() + 0.5D, cursor.getY(), cursor.getZ() + 0.5D, level.getRandom().nextFloat() * 360.0F, 0.0F);
        guide.finalizeSpawn(level, level.getCurrentDifficultyAt(cursor), MobSpawnType.STRUCTURE, null, null);
        level.addFreshEntity(guide);
    }

    private static BlockPos findAncientSiteBlock(ServerLevel level, BlockPos center) {
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-SITE_SCAN_RADIUS, -SITE_SCAN_Y, -SITE_SCAN_RADIUS), center.offset(SITE_SCAN_RADIUS, SITE_SCAN_Y, SITE_SCAN_RADIUS))) {
            Block block = level.getBlockState(pos).getBlock();
            if (block == galaxyunderchaos.ANCIENT_TEMPLE_STONE.get()
                    || block == galaxyunderchaos.ANCIENT_TEMPLE_STONE_CRACKED.get()
                    || block == galaxyunderchaos.ANCIENT_TEMPLE_STONE_PILLAR.get()
                    || block == galaxyunderchaos.ANCIENT_TEMPLE_STONE_HOLOBOOK.get()) {
                return pos.immutable();
            }
        }
        return null;
    }
}
