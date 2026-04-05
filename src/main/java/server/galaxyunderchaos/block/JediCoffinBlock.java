package server.galaxyunderchaos.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import server.galaxyunderchaos.entity.CoffinBlockEntity;

public class JediCoffinBlock extends AbstractCoffinBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BedPart DEFAULT_PART = BedPart.FOOT;

    public JediCoffinBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(BlockStateProperties.BED_PART, DEFAULT_PART));
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(0, 0.1, 0, 16, 16, 16);
    }
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return state.getValue(BlockStateProperties.BED_PART) == BedPart.FOOT
                ? RenderShape.MODEL
                : RenderShape.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(BlockStateProperties.BED_PART) == BedPart.FOOT ? new CoffinBlockEntity(pos, state) : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BlockStateProperties.BED_PART);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return tryOpenContainer(level, pos, player);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, net.minecraft.world.entity.LivingEntity placer, net.minecraft.world.item.ItemStack stack) {
        Direction direction = placer.getDirection();
        BlockPos headPos = pos.relative(direction);
        level.setBlock(headPos, state.setValue(BlockStateProperties.BED_PART, BedPart.HEAD).setValue(FACING, direction), 3);
    }

    @Override
    public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection();
        BlockPos pos = context.getClickedPos();
        BlockPos headPos = pos.relative(direction);
        Level level = context.getLevel();
        if (!level.getBlockState(headPos).canBeReplaced(context)) {
            return null;
        }
        return this.defaultBlockState().setValue(FACING, direction).setValue(BlockStateProperties.BED_PART, BedPart.FOOT);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            BedPart part = state.getValue(BlockStateProperties.BED_PART);
            Direction facing = state.getValue(FACING);
            BlockPos otherPos = (part == BedPart.FOOT)
                    ? pos.relative(facing)
                    : pos.relative(facing.getOpposite());
            BlockState otherState = level.getBlockState(otherPos);
            if (otherState.is(this) && otherState.getValue(BlockStateProperties.BED_PART) != part) {
                level.destroyBlock(otherPos, false);
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }


    @Override
    protected BlockPos getMainBlockPos(BlockState state, BlockPos pos) {
        return state.getValue(BlockStateProperties.BED_PART) == BedPart.HEAD ? pos.relative(state.getValue(FACING).getOpposite()) : pos;
    }
}
