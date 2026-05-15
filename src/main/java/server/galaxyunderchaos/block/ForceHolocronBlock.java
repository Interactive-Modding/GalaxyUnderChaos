package server.galaxyunderchaos.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import server.galaxyunderchaos.force.ForceCapabilityManager;
import server.galaxyunderchaos.force.ForcePower;
import server.galaxyunderchaos.force.ForceProvider;
import server.galaxyunderchaos.force.ForceSide;
import server.galaxyunderchaos.menu.ForceHolocronMenu;
import server.galaxyunderchaos.sound.ModSounds;

public class ForceHolocronBlock extends Block {
    private static final String PENDING_RENOUNCE_SIDE_TAG = "gucPendingRenounceSide";
    private static final String PENDING_RENOUNCE_TIME_TAG = "gucPendingRenounceTime";
    private static final String PENDING_RENOUNCE_POS_TAG = "gucPendingRenouncePos";
    private static final int RENOUNCE_DOUBLE_CLICK_WINDOW_TICKS = 40;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final VoxelShape SHAPE = Block.box(5, 0, 5, 11, 6, 11);
    private final ForceSide side;

    public ForceHolocronBlock(ForceSide side) {
        super(Properties.of().strength(0.4F, 0.4F).sound(SoundType.GLASS).lightLevel(state -> 11).noOcclusion().pushReaction(PushReaction.NORMAL));
        this.side = side;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public ForceSide getSide() {
        return side;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        level.playSound(player, pos, ModSounds.HOLOCRON_OPEN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            if (tryHandleRenounce(level, pos, serverPlayer)) {
                return InteractionResult.sidedSuccess(false);
            }

            NetworkHooks.openScreen(serverPlayer, new net.minecraft.world.SimpleMenuProvider(
                    (id, inventory, p) -> new ForceHolocronMenu(id, inventory, pos, side),
                    Component.literal(side == ForceSide.DARK ? "Sith Holocron" : side == ForceSide.LIGHT ? "Jedi Holocron" : "Ancient Holocron")
            ), buf -> {
                buf.writeBlockPos(pos);
                buf.writeVarInt(side.ordinal());
            });
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private boolean tryHandleRenounce(Level level, BlockPos pos, ServerPlayer player) {
        if (side != ForceSide.LIGHT && side != ForceSide.DARK) {
            return false;
        }

        final boolean[] handled = {false};
        player.getCapability(ForceProvider.FORCE_CAPABILITY).ifPresent(cap -> {
            ForceSide opposite = side == ForceSide.LIGHT ? ForceSide.DARK : ForceSide.LIGHT;
            ForceSide committed = cap.getCommittedSide();
            if (committed == ForceSide.UNIVERSAL || committed == ForceSide.NEUTRAL) {
                if (opposite == ForceSide.DARK && cap.hasPower(ForcePower.DARK_SIDE) && !cap.hasPower(ForcePower.LIGHT_SIDE)) {
                    committed = ForceSide.DARK;
                } else if (opposite == ForceSide.LIGHT && cap.hasPower(ForcePower.LIGHT_SIDE) && !cap.hasPower(ForcePower.DARK_SIDE)) {
                    committed = ForceSide.LIGHT;
                }
            }

            if (committed != opposite) {
                return;
            }

            long now = level.getGameTime();
            net.minecraft.nbt.CompoundTag data = player.getPersistentData();
            boolean secondClick = data.getInt(PENDING_RENOUNCE_SIDE_TAG) == side.ordinal()
                    && data.getLong(PENDING_RENOUNCE_TIME_TAG) + RENOUNCE_DOUBLE_CLICK_WINDOW_TICKS >= now
                    && pos.asLong() == data.getLong(PENDING_RENOUNCE_POS_TAG);

            if (!secondClick) {
                data.putInt(PENDING_RENOUNCE_SIDE_TAG, side.ordinal());
                data.putLong(PENDING_RENOUNCE_TIME_TAG, now);
                data.putLong(PENDING_RENOUNCE_POS_TAG, pos.asLong());
                player.displayClientMessage(Component.literal("Double-click this " + holocronName() + " to renounce the " + sideName(opposite) + " and begin the " + sideName(side) + "."), false);
                handled[0] = true;
                return;
            }

            data.remove(PENDING_RENOUNCE_SIDE_TAG);
            data.remove(PENDING_RENOUNCE_TIME_TAG);
            data.remove(PENDING_RENOUNCE_POS_TAG);
            cap.renounceAndCommit(side);
            if (side == ForceSide.DARK) {
                cap.beginAlignmentFlash(ForceSide.DARK, 120);
                player.displayClientMessage(Component.literal("Embracing dark side..."), false);
            } else {
                player.displayClientMessage(Component.literal("You renounced the dark side and returned to the light."), false);
            }
            ForceCapabilityManager.sync(player);
            handled[0] = true;
        });
        return handled[0];
    }

    private String holocronName() {
        return side == ForceSide.DARK ? "Sith holocron" : side == ForceSide.LIGHT ? "Jedi holocron" : "Ancient holocron";
    }

    private static String sideName(ForceSide side) {
        return side == ForceSide.DARK ? "dark side" : side == ForceSide.LIGHT ? "light side" : "Force";
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double x = pos.getX() + 0.5D;
        double y = pos.getY() + 0.65D + Math.sin((level.getGameTime() + random.nextInt(10)) * 0.08D) * 0.05D;
        double z = pos.getZ() + 0.5D;
        int count = 2;
        for (int i = 0; i < count; i++) {
            double xo = (random.nextDouble() - 0.5D) * 0.25D;
            double zo = (random.nextDouble() - 0.5D) * 0.25D;
            if (side == ForceSide.DARK) {
                level.addParticle(net.minecraft.core.particles.ParticleTypes.SOUL_FIRE_FLAME, x + xo, y, z + zo, 0.0D, 0.003D, 0.0D);
            } else {
                level.addParticle(net.minecraft.core.particles.ParticleTypes.END_ROD, x + xo, y, z + zo, 0.0D, 0.003D, 0.0D);
            }
        }
    }
}
