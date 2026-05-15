package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.BlockState;
import server.galaxyunderchaos.entity.forceuser.ForceUserEntity;
import server.galaxyunderchaos.entity.forceuser.ForceUserLoadout;
import server.galaxyunderchaos.entity.forceuser.ForceUserSide;
import server.galaxyunderchaos.galaxyunderchaos;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CoffinBlockEntity extends RandomizableContainerBlockEntity implements GeoBlockEntity {

    private static final int OPEN_ANIMATION_TICKS = 30;
    private static final RawAnimation OPEN_ANIM = RawAnimation.begin().thenPlay("Open");
    private static final RawAnimation CLOSE_ANIM = RawAnimation.begin().thenPlay("Close");

    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private boolean guardianTriggered;
    private boolean opened;
    private long openGameTime = -1L;
    private UUID coffinInstanceId = UUID.randomUUID();

    public CoffinBlockEntity(BlockPos pos, BlockState blockState) {
        this(ModBlockEntities.COFFIN_BE.get(), pos, blockState);
    }

    protected CoffinBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.galaxyunderchaos.coffin");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return ChestMenu.threeRows(containerId, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> newItems) {
        items = newItems;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    public void onOpenedBy(Player player, Level level, BlockPos mainPos) {
        if (level.isClientSide) {
            return;
        }

        markOpened(level, mainPos);

        if (guardianTriggered) {
            return;
        }

        BlockState state = level.getBlockState(mainPos);
        if (state.is(galaxyunderchaos.JEDI_COFFIN.get())) {
            this.unpackLootTable(player);
            scatterGeneratedLoot(level, ForceUserSide.LIGHT, false);
            spawnTempleGuards(level, mainPos);
            guardianTriggered = true;
            setChanged();
            return;
        }

        if (state.is(galaxyunderchaos.SITH_LORD_COFFIN.get())) {
            this.unpackLootTable(player);
            scatterGeneratedLoot(level, ForceUserSide.DARK, true);
            spawnGuardian(level, mainPos, galaxyunderchaos.SITH_LORD_GHOST.get());
            guardianTriggered = true;
            setChanged();
            return;
        }

        if (state.is(galaxyunderchaos.SITH_COFFIN.get())) {
            scatterGeneratedLoot(level, ForceUserSide.DARK, false);
            spawnGuardian(level, mainPos, galaxyunderchaos.SITH_GHOST.get());
            guardianTriggered = true;
            setChanged();
        }
    }

    private void spawnGuardian(Level level, BlockPos mainPos, EntityType<ForceUserEntity> type) {
        ForceUserEntity guardian = type.create(level);
        if (guardian == null) {
            return;
        }
        guardian.moveTo(mainPos.getX() + 0.5D, mainPos.getY() + 1.05D, mainPos.getZ() + 0.5D, level.getRandom().nextFloat() * 360.0F, 0.0F);
        if (level instanceof ServerLevel serverLevel) {
            guardian.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(mainPos), MobSpawnType.TRIGGERED, null, null);
        }
        level.addFreshEntity(guardian);
    }

    private void spawnTempleGuards(Level level, BlockPos mainPos) {
        BlockPos[] offsets = new BlockPos[] {
                mainPos.relative(getFacing().getClockWise(), 2),
                mainPos.relative(getFacing().getCounterClockWise(), 2),
                mainPos.relative(getFacing(), 2),
                mainPos.relative(getFacing().getOpposite(), 1)
        };
        for (BlockPos spawnPos : offsets) {
            spawnGuardian(level, spawnPos, galaxyunderchaos.JEDI_TEMPLE_GUARD.get());
        }
    }

    private void scatterGeneratedLoot(Level level, ForceUserSide side, boolean lordQuality) {
        // Jedi coffins can contain a saber. Sith coffins/tombs do not: their sabers
        // are earned from the ghost/apprentice kill drop instead.
        if (side.isLight()) {
            ItemStack saber = ForceUserLoadout.randomLightsaber(level.getRandom(), side, lordQuality ? 0.28F : 0.08F, lordQuality);
            scatterItem(saber, level);
        }
        scatterItem(new ItemStack(galaxyunderchaos.INTERNAL_LIGHTSABER_CIRCUITRY.get()), level);
        scatterItem(new ItemStack(Items.DIAMOND, 1 + level.getRandom().nextInt(lordQuality ? 2 : 1)), level);
        scatterItem(new ItemStack(Items.GOLD_INGOT, 1 + level.getRandom().nextInt(lordQuality ? 4 : 3)), level);
        scatterItem(new ItemStack(Items.BONE, 1 + level.getRandom().nextInt(4)), level);
        scatterItem(new ItemStack(Items.COBWEB, 1 + level.getRandom().nextInt(3)), level);

        int parts = level.getRandom().nextInt(5); // max 4 loose saber parts
        List<ItemStack> partStacks = new ArrayList<>();
        for (var part : galaxyunderchaos.LIGHTSABER_PARTS.values()) {
            partStacks.add(new ItemStack(part.get()));
        }
        Collections.shuffle(partStacks, new java.util.Random(level.getRandom().nextLong()));
        for (int i = 0; i < Math.min(parts, partStacks.size()); i++) {
            scatterItem(partStacks.get(i), level);
        }
    }

    private void scatterItem(ItemStack stack, Level level) {
        if (stack.isEmpty()) {
            return;
        }
        int start = level.getRandom().nextInt(items.size());
        for (int n = 0; n < items.size(); n++) {
            int slot = (start + n * 7) % items.size();
            if (items.get(slot).isEmpty()) {
                items.set(slot, stack);
                return;
            }
        }
    }

    private void insertGeneratedLoot(ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isEmpty()) {
                items.set(i, stack);
                return;
            }
        }
    }


    private void markOpened(Level level, BlockPos mainPos) {
        if (!opened) {
            opened = true;
            openGameTime = level.getGameTime();
            triggerAnim("coffin_controller", "open");
            emitGeckoKeyframeParticles(level, mainPos);
            setChanged();
            level.sendBlockUpdated(mainPos, getBlockState(), getBlockState(), 3);
        }
    }

    private void markClosed(Level level) {
        if (opened) {
            opened = false;
            openGameTime = level.getGameTime();
            triggerAnim("coffin_controller", "close");
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public void startOpen(Player player) {
        super.startOpen(player);
        if (!player.isSpectator() && this.level != null && !this.level.isClientSide) {
            markOpened(this.level, this.worldPosition);
        }
    }

    @Override
    public void stopOpen(Player player) {
        super.stopOpen(player);
        if (!player.isSpectator() && this.level != null && !this.level.isClientSide) {
            markClosed(this.level);
        }
    }

    private void emitGeckoKeyframeParticles(Level level, BlockPos mainPos) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        SimpleParticleType particle = isSithStyleCoffin() ? galaxyunderchaos.SITH_COFFIN_PARTICLE.get() : galaxyunderchaos.JEDI_COFFIN_PARTICLE.get();
        Direction facing = getFacing();
        double cx = mainPos.getX() + 0.5D + facing.getStepX() * 0.45D;
        double cy = mainPos.getY() + 0.85D;
        double cz = mainPos.getZ() + 0.5D + facing.getStepZ() * 0.45D;
        serverLevel.sendParticles(particle, cx, cy, cz, 58, 0.45D, 0.08D, 0.95D, 0.035D);
    }

    public boolean isOpened() {
        return opened;
    }

    public float getOpenProgress(float partialTick) {
        if (level == null || openGameTime < 0L) {
            return opened ? 1.0F : 0.0F;
        }
        float ticks = (level.getGameTime() - openGameTime) + partialTick;
        float progress = Math.max(0.0F, Math.min(1.0F, ticks / (float)OPEN_ANIMATION_TICKS));
        return opened ? progress : 1.0F - progress;
    }

    public boolean isSithStyleCoffin() {
        BlockState state = getBlockState();
        return state.is(galaxyunderchaos.SITH_LORD_COFFIN.get());
    }

    public Direction getFacing() {
        BlockState state = getBlockState();
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            return state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        }
        return Direction.NORTH;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "coffin_controller", 0, state -> PlayState.STOP)
                .triggerableAnim("open", OPEN_ANIM)
                .triggerableAnim("close", CLOSE_ANIM)
                .setParticleKeyframeHandler(event -> {
                    if (this.level != null && !this.level.isClientSide) {
                        emitGeckoKeyframeParticles(this.level, this.worldPosition);
                    }
                }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animationCache;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveVisualState(tag);
        return tag;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void saveVisualState(CompoundTag tag) {
        tag.putBoolean("Opened", opened);
        tag.putLong("OpenGameTime", openGameTime);
        tag.putUUID("CoffinInstanceId", coffinInstanceId);
        tag.putLong("CoffinBoundPos", this.worldPosition.asLong());
    }

    private void loadVisualState(CompoundTag tag) {
        boolean clonedFromStructure = tag.contains("CoffinBoundPos") && tag.getLong("CoffinBoundPos") != this.worldPosition.asLong();
        if (tag.hasUUID("CoffinInstanceId") && !clonedFromStructure) {
            coffinInstanceId = tag.getUUID("CoffinInstanceId");
        } else {
            coffinInstanceId = UUID.randomUUID();
        }
        opened = !clonedFromStructure && tag.getBoolean("Opened");
        openGameTime = clonedFromStructure ? -1L : (tag.contains("OpenGameTime") ? tag.getLong("OpenGameTime") : (opened ? -1L : 0L));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("GuardianTriggered", guardianTriggered);
        saveVisualState(tag);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        boolean clonedFromStructure = tag.contains("CoffinBoundPos") && tag.getLong("CoffinBoundPos") != this.worldPosition.asLong();
        guardianTriggered = !clonedFromStructure && tag.getBoolean("GuardianTriggered");
        loadVisualState(tag);
        items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        if (!clonedFromStructure && !this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items);
        }
    }
}
