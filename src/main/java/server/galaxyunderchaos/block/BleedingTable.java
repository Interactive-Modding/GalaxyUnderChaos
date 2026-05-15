package server.galaxyunderchaos.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import server.galaxyunderchaos.entity.BleedingTableBlockEntity;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.List;

public class BleedingTable extends BaseEntityBlock {
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    public static final VoxelShape SHAPE = Block.box(0.1, 0.1, 0.1, 16, 16, 16);

    public BleedingTable(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public BleedingTable() {
        this(BlockBehaviour.Properties.of()
                .strength(3.0f, 10.0f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.STONE)
                .pushReaction(PushReaction.NORMAL)
                .noOcclusion());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
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

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BleedingTableBlockEntity(pos, state);
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof MenuProvider provider ? provider : null;
    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (oldState.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof BleedingTableBlockEntity table) {
                table.dropContents(level, pos);
            }
            super.onRemove(oldState, level, pos, newState, isMoving);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Insert a kyber crystal, then channel Force Lightning through the table to bleed it red."));
        super.appendHoverText(stack, level, tooltip, flag);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BleedingTableBlockEntity table) {
                NetworkHooks.openScreen(serverPlayer, table, pos);
            }
        }
        level.playSound(player, pos, SoundEvents.AMETHYST_CLUSTER_PLACE, SoundSource.BLOCKS, 0.8F, 0.9F);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static boolean isValidKyber(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        Item item = stack.getItem();
        return item == galaxyunderchaos.BLUE_KYBER.get()
                || item == galaxyunderchaos.ORANGE_KYBER.get()
                || item == galaxyunderchaos.GREEN_KYBER.get()
                || item == galaxyunderchaos.YELLOW_KYBER.get()
                || item == galaxyunderchaos.CYAN_KYBER.get()
                || item == galaxyunderchaos.WHITE_KYBER.get()
                || item == galaxyunderchaos.MAGENTA_KYBER.get()
                || item == galaxyunderchaos.PURPLE_KYBER.get()
                || item == galaxyunderchaos.PINK_KYBER.get()
                || item == galaxyunderchaos.LIME_GREEN_KYBER.get()
                || item == galaxyunderchaos.TURQUOISE_KYBER.get()
                || item == galaxyunderchaos.BLOOD_ORANGE_KYBER.get()
                || item == galaxyunderchaos.AMBER_KYBER.get()
                || item == galaxyunderchaos.GOLD_KYBER.get()
                || item == galaxyunderchaos.LIGHT_BLUE_KYBER.get()
                || item == galaxyunderchaos.DARK_BLUE_KYBER.get()
                || item == galaxyunderchaos.MAROON_KYBER.get()
                || item == galaxyunderchaos.DEEP_VIOLET_KYBER.get()
                || item == galaxyunderchaos.ARCTIC_BLUE_KYBER.get()
                || item == galaxyunderchaos.ROSE_PINK_KYBER.get();
    }

    public static ItemStack getBleedingResult(ItemStack input) {
        if (!isValidKyber(input)) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(galaxyunderchaos.RED_KYBER.get(), input.getCount());
    }

    public static void summonLightningEffect(Level level, BlockPos pos, @Nullable ServerPlayer player) {
        if (!level.isClientSide) {
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
            if (bolt != null) {
                bolt.moveTo(pos.getX() + 0.5D, pos.getY() + 0.9D, pos.getZ() + 0.5D);
                bolt.setCause(player);
                bolt.setVisualOnly(true);
                level.addFreshEntity(bolt);
            }
        }
    }
}
