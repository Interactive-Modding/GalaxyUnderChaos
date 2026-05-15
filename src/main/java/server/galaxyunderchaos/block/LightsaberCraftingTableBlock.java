
package server.galaxyunderchaos.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import server.galaxyunderchaos.entity.LightsaberCraftingTableBlockEntity;
import server.galaxyunderchaos.item.DoubleLightsaberItem;
import server.galaxyunderchaos.item.HiltItem;
import server.galaxyunderchaos.item.LightsaberItem;
import server.galaxyunderchaos.item.LightsaberPartItem;
import server.galaxyunderchaos.lightsaber.BladeModifierCrystal;
import server.galaxyunderchaos.lightsaber.LightsaberCrafting;
import server.galaxyunderchaos.lightsaber.LightsaberPartType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

public class LightsaberCraftingTableBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    private static final VoxelShape NORTH_SHAPE = Block.box(-8, 0.1, 0.1, 24, 16, 17);
    private static final VoxelShape WEST_SHAPE = Block.box(0.1, 0.1, -8, 17, 16, 24);
    private static final VoxelShape SOUTH_SHAPE = Block.box(-8, 0.1, -1, 24, 16, 16);
    private static final VoxelShape EAST_SHAPE = Block.box(-1, 0.1, -8, 16, 16, 24);

    public LightsaberCraftingTableBlock() {
        super(Properties.of()
                .strength(4.0f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.COPPER)
                .noOcclusion()
                .pushReaction(PushReaction.NORMAL));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return switch (facing) {
            case NORTH -> NORTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LightsaberCraftingTableBlockEntity(pos, state);
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof MenuProvider provider ? provider : null;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof LightsaberCraftingTableBlockEntity tableBlockEntity) {
                NetworkHooks.openScreen(serverPlayer, tableBlockEntity, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!(entity instanceof ItemEntity triggerEntity) || level.isClientSide) {
            super.stepOn(level, pos, state, entity);
            return;
        }

        List<ItemEntity> nearbyItems = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(1.25D));

        ItemEntity otherLightsaber = findOtherCraftableLightsaber(nearbyItems, triggerEntity);
        if (otherLightsaber != null) {
            ItemStack result = LightsaberCrafting.craftDoubleLightsaber(triggerEntity.getItem(), otherLightsaber.getItem());
            if (!result.isEmpty()) {
                consumeOne(triggerEntity);
                consumeOne(otherLightsaber);
                spawnCraftedResult(level, pos, result);
                spawnCraftingParticles(level, pos);
                level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 1f, 0.8f);
                super.stepOn(level, pos, state, entity);
                return;
            }
        }

        ItemEntity finishedLightsaber = findFinishedLightsaber(nearbyItems);
        List<ItemEntity> modifierCrystals = findModifierCrystals(nearbyItems);
        if (countCraftableSingleSabers(nearbyItems) < 2 && finishedLightsaber != null && !modifierCrystals.isEmpty()) {
            List<ItemStack> modifierStacks = new ArrayList<>();
            for (ItemEntity modifierCrystal : modifierCrystals) {
                modifierStacks.add(modifierCrystal.getItem());
            }

            ItemStack result = LightsaberCrafting.applyModifierCrystals(finishedLightsaber.getItem(), modifierStacks);
            if (!result.isEmpty()) {
                consumeOne(finishedLightsaber);
                for (ItemEntity modifierCrystal : modifierCrystals) {
                    consumeOne(modifierCrystal);
                }
                spawnCraftedResult(level, pos, result);
                spawnCraftingParticles(level, pos);
                level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 1f, 1.15f);
                super.stepOn(level, pos, state, entity);
                return;
            }
        }

        ItemEntity kyberEntity = findKyber(nearbyItems);
        ItemEntity circuitryEntity = findCircuitry(nearbyItems);
        if (kyberEntity == null || circuitryEntity == null) {
            super.stepOn(level, pos, state, entity);
            return;
        }

        if (triggerEntity.getItem().getItem() instanceof HiltItem) {
            ItemStack result = LightsaberCrafting.craftLightsaber(triggerEntity.getItem(), kyberEntity.getItem());
            if (!result.isEmpty()) {
                consumeOne(triggerEntity);
                consumeOne(circuitryEntity);
                consumeOne(kyberEntity);
                spawnCraftedResult(level, pos, result);
                spawnCraftingParticles(level, pos);
                level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 1f, 1f);
                super.stepOn(level, pos, state, entity);
                return;
            }
        }

        ItemEntity emitter = findPart(nearbyItems, LightsaberPartType.EMITTER);
        ItemEntity switchSection = findPart(nearbyItems, LightsaberPartType.SWITCH_SECTION);
        ItemEntity grip = findPart(nearbyItems, LightsaberPartType.GRIP);
        ItemEntity pommel = findPart(nearbyItems, LightsaberPartType.POMMEL);

        if (emitter != null && switchSection != null && grip != null && pommel != null) {
            ItemStack result = LightsaberCrafting.craftLightsaber(emitter.getItem(), switchSection.getItem(), grip.getItem(), pommel.getItem(), kyberEntity.getItem());
            if (!result.isEmpty()) {
                consumeOne(emitter);
                consumeOne(switchSection);
                consumeOne(grip);
                consumeOne(pommel);
                consumeOne(circuitryEntity);
                consumeOne(kyberEntity);
                spawnCraftedResult(level, pos, result);
                spawnCraftingParticles(level, pos);
                level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 1f, 1f);
            }
        }

        super.stepOn(level, pos, state, entity);
    }

    private void spawnCraftedResult(Level level, BlockPos pos, ItemStack result) {
        ItemEntity entity = new ItemEntity(level,
                pos.getX() + 0.5D,
                pos.getY() + 1.0D,
                pos.getZ() + 0.5D,
                server.galaxyunderchaos.lightsaber.LightsaberCraftingTableLogic.activatePreview(result));
        entity.setDeltaMovement(0.0D, 0.05D, 0.0D);
        level.addFreshEntity(entity);
    }

    private void consumeOne(ItemEntity itemEntity) {
        ItemStack stack = itemEntity.getItem();
        stack.shrink(1);
        if (stack.isEmpty()) {
            itemEntity.discard();
        }
    }

    @Nullable
    private ItemEntity findOtherCraftableLightsaber(List<ItemEntity> entities, ItemEntity exclude) {
        for (ItemEntity entity : entities) {
            if (entity == exclude) {
                continue;
            }
            if (server.galaxyunderchaos.lightsaber.DoubleLightsaberData.canCreateFrom(entity.getItem())
                    && server.galaxyunderchaos.lightsaber.DoubleLightsaberData.canCreateFrom(exclude.getItem())) {
                return entity;
            }
        }
        return null;
    }


    @Nullable
    private ItemEntity findCircuitry(List<ItemEntity> entities) {
        for (ItemEntity entity : entities) {
            if (server.galaxyunderchaos.lightsaber.LightsaberCraftingTableLogic.isCircuitry(entity.getItem())) {
                return entity;
            }
        }
        return null;
    }

    @Nullable
    private ItemEntity findFinishedLightsaber(List<ItemEntity> entities) {
        for (ItemEntity entity : entities) {
            if (entity.getItem().getItem() instanceof LightsaberItem && !(entity.getItem().getItem() instanceof DoubleLightsaberItem)) {
                return entity;
            }
        }
        return null;
    }

    private List<ItemEntity> findModifierCrystals(List<ItemEntity> entities) {
        List<ItemEntity> found = new ArrayList<>();
        LinkedHashSet<BladeModifierCrystal> seen = new LinkedHashSet<>();
        for (ItemEntity entity : entities) {
            BladeModifierCrystal crystal = BladeModifierCrystal.fromStack(entity.getItem());
            if (crystal == null || !seen.add(crystal)) {
                continue;
            }
            found.add(entity);
            if (found.size() >= server.galaxyunderchaos.lightsaber.ModularLightsaberData.MAX_BLADE_MODIFIERS) {
                break;
            }
        }
        return found;
    }

    private int countCraftableSingleSabers(List<ItemEntity> entities) {
        int count = 0;
        for (ItemEntity entity : entities) {
            if (server.galaxyunderchaos.lightsaber.DoubleLightsaberData.canCreateFrom(entity.getItem())) {
                count++;
            }
        }
        return count;
    }

    @Nullable
    private ItemEntity findKyber(List<ItemEntity> entities) {
        for (ItemEntity entity : entities) {
            if (isKyberCrystal(entity.getItem())) {
                return entity;
            }
        }
        return null;
    }

    @Nullable
    private ItemEntity findPart(List<ItemEntity> entities, LightsaberPartType type) {
        for (ItemEntity entity : entities) {
            if (entity.getItem().getItem() instanceof LightsaberPartItem partItem && partItem.getPartType() == type) {
                return entity;
            }
        }
        return null;
    }

    private boolean isKyberCrystal(ItemStack itemStack) {
        String[] validKybers = {
                "red_kyber", "blue_kyber", "green_kyber", "yellow_kyber",
                "cyan_kyber", "white_kyber", "magenta_kyber", "purple_kyber",
                "pink_kyber", "lime_green_kyber", "turquoise_kyber", "orange_kyber", "blood_orange_kyber", "amber_kyber", "gold_kyber", "light_blue_kyber", "dark_blue_kyber", "maroon_kyber", "deep_violet_kyber", "arctic_blue_kyber", "rose_pink_kyber"
        };

        ResourceLocation itemName = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
        return itemName != null && Arrays.asList(validKybers).contains(itemName.getPath());
    }

    @Override
    public void appendHoverText(ItemStack pStack, BlockGetter pLevel, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.literal("Right-click to use the Saber Forge-style assembly GUI."));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pTooltipFlag);
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
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof LightsaberCraftingTableBlockEntity tableBlockEntity) {
                Containers.dropContents(level, pos, tableBlockEntity);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    private void spawnCraftingParticles(Level level, BlockPos pos) {
        if (level.isClientSide) {
            for (int i = 0; i < 10; i++) {
                level.addParticle(ParticleTypes.ENCHANT,
                        pos.getX() + 0.5 + (level.random.nextDouble() - 0.5),
                        pos.getY() + 1,
                        pos.getZ() + 0.5 + (level.random.nextDouble() - 0.5),
                        0, 0, 0);
            }
        }
    }
}
