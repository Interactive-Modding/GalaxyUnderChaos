package server.galaxyunderchaos.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CrystalOre extends Block {
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.values());
    public static final VoxelShape SHAPE_UP = Block.box(6.5, -0.7, 5.9, 8.5, 3.0, 9.0);
    public static final VoxelShape SHAPE_DOWN = Block.box(6.5, 13.0, 5.9, 8.5, 16.0, 9.0);
    public static final VoxelShape SHAPE_NORTH = Block.box(6.5, 5.9, -0.7, 8.5, 9.0, 0.3);
    public static final VoxelShape SHAPE_SOUTH = Block.box(6.5, 5.9, 15.7, 8.5, 9.0, 16.3);
    public static final VoxelShape SHAPE_EAST = Block.box(15.7, 5.9, 6.5, 16.3, 9.0, 8.5);
    public static final VoxelShape SHAPE_WEST = Block.box(-0.7, 5.9, 6.5, 0.3, 9.0, 8.5);

    public CrystalOre() {
        super(Properties.of()
                .strength(4.0f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.AMETHYST)
                .lightLevel((state) -> 12)
                .pushReaction(PushReaction.NORMAL));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        switch (facing) {
            case DOWN:
                return SHAPE_DOWN;
            case NORTH:
                return SHAPE_SOUTH;
            case SOUTH:
                return SHAPE_NORTH;
            case EAST:
                return SHAPE_WEST;  // Adjusted for EAST
            case WEST:
                return SHAPE_EAST;  // Adjusted for WEST
            case UP:
            default:
                return SHAPE_UP;
        }
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
        BlockPos below = context.getClickedPos().below();
        LevelReader level = context.getLevel();
        Direction face = context.getClickedFace();

        if (!level.getBlockState(below).isSolidRender(level, below) && face != Direction.UP) {
            return this.defaultBlockState().setValue(FACING, Direction.DOWN);
        }

        if (face == Direction.UP) {
            return this.defaultBlockState().setValue(FACING, Direction.UP);
        }

        return this.defaultBlockState().setValue(FACING, face);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos attachedPos = pos.relative(facing.getOpposite());
        return level.getBlockState(attachedPos).isSolidRender(level, attachedPos);
    }
}
