package server.galaxyunderchaos.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import server.galaxyunderchaos.entity.GroundSaberStandBlockEntity;

public class GroundSaberStandBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;

    private static final VoxelShape FLOOR_SHAPE_NORTH = Block.box(6, 0, 5, 10, 1.4, 11);
    private static final VoxelShape FLOOR_SHAPE_SOUTH = Block.box(6, 0, 5, 10, 1.4, 11);
    private static final VoxelShape FLOOR_SHAPE_EAST  = Block.box(5, 0, 6, 11, 1.4, 10);
    private static final VoxelShape FLOOR_SHAPE_WEST  = Block.box(5, 0, 6, 11, 1.4, 10);

    private static final VoxelShape WALL_SHAPE_NORTH = Block.box(5, 5, 14, 11, 11, 16);
    private static final VoxelShape WALL_SHAPE_SOUTH = Block.box(5, 5, 0, 11, 11, 2);
    private static final VoxelShape WALL_SHAPE_EAST  = Block.box(0, 5, 5, 2, 11, 11);
    private static final VoxelShape WALL_SHAPE_WEST  = Block.box(14, 5, 5, 16, 11, 11);

    public GroundSaberStandBlock(BlockBehaviour.Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(FACE, AttachFace.FLOOR));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction clickedFace = ctx.getClickedFace();

        if (clickedFace == Direction.DOWN) {
            return null;
        }

        BlockState state;
        if (clickedFace == Direction.UP) {
            state = this.defaultBlockState()
                    .setValue(FACE, AttachFace.FLOOR)
                    .setValue(FACING, ctx.getHorizontalDirection().getOpposite());
        } else {
            state = this.defaultBlockState()
                    .setValue(FACE, AttachFace.WALL)
                    .setValue(FACING, clickedFace);
        }

        return state.canSurvive(ctx.getLevel(), ctx.getClickedPos()) ? state : null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        AttachFace face = state.getValue(FACE);
        Direction facing = state.getValue(FACING);

        if (face == AttachFace.FLOOR) {
            BlockPos supportPos = pos.below();
            return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, Direction.UP);
        }

        BlockPos supportPos = pos.relative(facing.getOpposite());
        return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, facing);
    }

    @Override
    public BlockState updateShape(BlockState state,
                                  Direction direction,
                                  BlockState neighborState,
                                  LevelAccessor level,
                                  BlockPos pos,
                                  BlockPos neighborPos) {
        return state.canSurvive(level, pos) ? super.updateShape(state, direction, neighborState, level, pos, neighborPos) : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return getStandShape(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return getStandShape(state);
    }

    private static VoxelShape getStandShape(BlockState state) {
        AttachFace face = state.getValue(FACE);
        Direction facing = state.getValue(FACING);

        if (face == AttachFace.WALL) {
            return switch (facing) {
                case NORTH -> WALL_SHAPE_NORTH;
                case SOUTH -> WALL_SHAPE_SOUTH;
                case EAST -> WALL_SHAPE_EAST;
                case WEST -> WALL_SHAPE_WEST;
                default -> WALL_SHAPE_NORTH;
            };
        }

        return switch (facing) {
            case NORTH -> FLOOR_SHAPE_NORTH;
            case SOUTH -> FLOOR_SHAPE_SOUTH;
            case EAST -> FLOOR_SHAPE_EAST;
            case WEST -> FLOOR_SHAPE_WEST;
            default -> FLOOR_SHAPE_NORTH;
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GroundSaberStandBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof GroundSaberStandBlockEntity stand)) {
            return InteractionResult.PASS;
        }

        ItemStack held = player.getItemInHand(hand);

        if (!held.isEmpty()) {
            if (stand.isEmpty()) {
                stand.setItem(held.copyWithCount(1));
                held.shrink(1);
                return InteractionResult.CONSUME;
            }
            return InteractionResult.PASS;
        }

        ItemStack removed = stand.removeItem();
        if (!removed.isEmpty()) {
            player.getInventory().placeItemBackInInventory(removed);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof GroundSaberStandBlockEntity stand && !stand.isEmpty()) {
                ItemStack stack = stand.removeItem();
                if (!stack.isEmpty()) {
                    Containers.dropItemStack(level, pos.getX() + 0.5D, pos.getY() + 0.25D, pos.getZ() + 0.5D, stack);
                }
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }
}
