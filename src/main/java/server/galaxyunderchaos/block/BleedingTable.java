package server.galaxyunderchaos.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import server.galaxyunderchaos.entity.BleedingTableBlockEntity;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.List;

public class BleedingTable extends BaseEntityBlock {

    public static final DirectionProperty FACING =
            DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

    public static final VoxelShape SHAPE =
            Block.box(0.1, 0.1, 0.1, 16, 16, 16);

    /* ----------------------------------------------------------------------- */
    /*  CODEC (REQUIRED FOR 1.20+ BLOCKS)                                       */
    /* ----------------------------------------------------------------------- */

    /* ----------------------------------------------------------------------- */
    /*  CONSTRUCTORS                                                           */
    /* ----------------------------------------------------------------------- */

    /** Constructor used by the codec */
    public BleedingTable(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(FACING, Direction.NORTH)
        );
    }

    /** Convenience constructor used during registration */
    public BleedingTable() {
        this(BlockBehaviour.Properties.of()
                .strength(3.0f, 10.0f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.STONE)
                .pushReaction(PushReaction.NORMAL)
                .noOcclusion());
    }

    /* ----------------------------------------------------------------------- */
    /*  SHAPE / RENDERING                                                       */
    /* ----------------------------------------------------------------------- */

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    /* ----------------------------------------------------------------------- */
    /*  BLOCK STATE                                                             */
    /* ----------------------------------------------------------------------- */

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    /* ----------------------------------------------------------------------- */
    /*  BLOCK ENTITY                                                            */
    /* ----------------------------------------------------------------------- */

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BleedingTableBlockEntity(pos, state);
    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos,
                         BlockState newState, boolean isMoving) {
        if (oldState.getBlock() != newState.getBlock()) {
            super.onRemove(oldState, level, pos, newState, isMoving);
        }
    }

    /* ----------------------------------------------------------------------- */
    /*  TOOLTIP                                                                */
    /* ----------------------------------------------------------------------- */

    @Override
    public void appendHoverText(ItemStack stack, BlockGetter level,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(
                "tooltip.galaxyunderchaos.bleeding_table.tooltip"
        ));
        super.appendHoverText(stack, level, tooltip, flag);
    }

    /* ----------------------------------------------------------------------- */
    /*  INTERACTION                                                             */
    /* ----------------------------------------------------------------------- */

    @Override
    public InteractionResult use(BlockState state, Level level,
                                               BlockPos pos, Player player, InteractionHand hand,
                                               BlockHitResult hit) {
        level.playSound(player, pos,
                SoundEvents.AMETHYST_CLUSTER_PLACE,
                SoundSource.BLOCKS, 1f, 1f);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof ItemEntity itemEntity) {
            ItemStack stack = itemEntity.getItem();

            if (stack.getItem() == galaxyunderchaos.ORANGE_KYBER.get()) {
                itemEntity.setItem(
                        new ItemStack(galaxyunderchaos.BLOOD_ORANGE_KYBER.get(),
                                stack.getCount()));
                summonLightningEffect(level, pos);
            } else if (stack.getItem() == galaxyunderchaos.PURPLE_KYBER.get()) {
                itemEntity.setItem(
                        new ItemStack(galaxyunderchaos.MAROON_KYBER.get(),
                                stack.getCount()));
                summonLightningEffect(level, pos);
            } else if (isValidKyber(stack)) {
                itemEntity.setItem(
                        new ItemStack(galaxyunderchaos.RED_KYBER.get(),
                                stack.getCount()));
                summonLightningEffect(level, pos);
            }

            level.playSound(null, pos,
                    SoundEvents.AMETHYST_BLOCK_STEP,
                    SoundSource.BLOCKS, 1f, 1f);
        }

        super.stepOn(level, pos, state, entity);
    }

    /* ----------------------------------------------------------------------- */
    /*  HELPERS                                                                 */
    /* ----------------------------------------------------------------------- */

    private boolean isValidKyber(ItemStack stack) {
        return stack.getItem() == galaxyunderchaos.BLUE_KYBER.get()
                || stack.getItem() == galaxyunderchaos.GREEN_KYBER.get()
                || stack.getItem() == galaxyunderchaos.YELLOW_KYBER.get()
                || stack.getItem() == galaxyunderchaos.CYAN_KYBER.get()
                || stack.getItem() == galaxyunderchaos.WHITE_KYBER.get()
                || stack.getItem() == galaxyunderchaos.MAGENTA_KYBER.get()
                || stack.getItem() == galaxyunderchaos.PURPLE_KYBER.get()
                || stack.getItem() == galaxyunderchaos.PINK_KYBER.get()
                || stack.getItem() == galaxyunderchaos.LIME_GREEN_KYBER.get()
                || stack.getItem() == galaxyunderchaos.TURQUOISE_KYBER.get()
                || stack.getItem() == galaxyunderchaos.ARCTIC_BLUE_KYBER.get()
                || stack.getItem() == galaxyunderchaos.LIGHT_BLUE_KYBER.get()
                || stack.getItem() == galaxyunderchaos.DARK_BLUE_KYBER.get()
                || stack.getItem() == galaxyunderchaos.GOLD_KYBER.get()
                || stack.getItem() == galaxyunderchaos.AMBER_KYBER.get()
                || stack.getItem() == galaxyunderchaos.DEEP_VIOLET_KYBER.get()
                || stack.getItem() == galaxyunderchaos.TURQUOISE_KYBER.get();
    }

    private void summonLightningEffect(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
            if (bolt != null) {
                bolt.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                bolt.setCause(null);
                bolt.setVisualOnly(true);
                level.addFreshEntity(bolt);
            }
        }
    }
}
