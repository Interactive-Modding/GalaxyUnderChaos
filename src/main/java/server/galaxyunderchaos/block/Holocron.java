package server.galaxyunderchaos.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
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
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.menu.ForceHolocronMenu;
import server.galaxyunderchaos.sound.ModSounds;

public class Holocron extends Block {
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    public static final VoxelShape SHAPE = Block.box(6, 0, 6, 10, 4, 10);

    private static final String PENDING_RENOUNCE_SIDE_TAG = "gucPendingRenounceSide";
    private static final String PENDING_RENOUNCE_TIME_TAG = "gucPendingRenounceTime";
    private static final String PENDING_RENOUNCE_POS_TAG = "gucPendingRenouncePos";
    private static final int RENOUNCE_DOUBLE_CLICK_WINDOW_TICKS = 40;

    public Holocron() {
        super(Properties.of()
                .strength(0.3f, 0.3f)
                .sound(SoundType.GLASS)
                .lightLevel((state) -> 9)
                .pushReaction(PushReaction.NORMAL));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
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
            ForceSide side = sideForState(state);
            if (tryHandleRenounce(level, pos, serverPlayer, side)) {
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

    private ForceSide sideForState(BlockState state) {
        if (state.is(galaxyunderchaos.SITH_HOLOCRON.get())) {
            return ForceSide.DARK;
        }
        if (state.is(galaxyunderchaos.ANCIENT_HOLOCRON.get())) {
            return ForceSide.NEUTRAL;
        }
        return ForceSide.LIGHT;
    }

    private boolean tryHandleRenounce(Level level, BlockPos pos, ServerPlayer player, ForceSide side) {
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
                player.displayClientMessage(Component.literal("Double-click this " + holocronName(side) + " to renounce the " + sideName(opposite) + " and begin the " + sideName(side) + "."), false);
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

    private static String holocronName(ForceSide side) {
        return side == ForceSide.DARK ? "Sith holocron" : side == ForceSide.LIGHT ? "Jedi holocron" : "Ancient holocron";
    }

    private static String sideName(ForceSide side) {
        return side == ForceSide.DARK ? "dark side" : side == ForceSide.LIGHT ? "light side" : "Force";
    }
}
