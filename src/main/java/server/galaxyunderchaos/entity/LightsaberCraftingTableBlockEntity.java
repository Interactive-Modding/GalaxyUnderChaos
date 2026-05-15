package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import server.galaxyunderchaos.lightsaber.LightsaberCraftingTableLogic;
import server.galaxyunderchaos.lightsaber.LightsaberPartType;
import server.galaxyunderchaos.lightsaber.ModularLightsaberData;
import server.galaxyunderchaos.menu.LightsaberCraftingTableMenu;

public class LightsaberCraftingTableBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<net.minecraft.world.item.ItemStack> items = NonNullList.withSize(LightsaberCraftingTableLogic.INPUT_SLOT_COUNT, net.minecraft.world.item.ItemStack.EMPTY);
    private final int[] partColors = {
            ModularLightsaberData.getDefaultPartColor(LightsaberPartType.EMITTER),
            ModularLightsaberData.getDefaultPartColor(LightsaberPartType.SWITCH_SECTION),
            ModularLightsaberData.getDefaultPartColor(LightsaberPartType.GRIP),
            ModularLightsaberData.getDefaultPartColor(LightsaberPartType.POMMEL)
    };

    public LightsaberCraftingTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LIGHTSABER_CRAFTING_TABLE_BE.get(), pos, state);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("menu.galaxyunderchaos.lightsaber_crafting_table");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new LightsaberCraftingTableMenu(containerId, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return LightsaberCraftingTableLogic.INPUT_SLOT_COUNT;
    }

    @Override
    protected NonNullList<net.minecraft.world.item.ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<net.minecraft.world.item.ItemStack> newItems) {
        this.items = newItems;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    public int getPartColor(LightsaberPartType type) {
        return partColors[type.ordinal()];
    }

    public void setPartColor(LightsaberPartType type, int color) {
        partColors[type.ordinal()] = ModularLightsaberData.clampPartColor(color);
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items);
        }
        CompoundTag colors = new CompoundTag();
        for (LightsaberPartType type : LightsaberPartType.values()) {
            colors.putInt(type.getSerializedName(), this.getPartColor(type));
        }
        tag.put(ModularLightsaberData.PART_COLORS_TAG, colors);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(getContainerSize(), net.minecraft.world.item.ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items);
        }
        CompoundTag colors = tag.contains(ModularLightsaberData.PART_COLORS_TAG, Tag.TAG_COMPOUND)
                ? tag.getCompound(ModularLightsaberData.PART_COLORS_TAG)
                : new CompoundTag();
        for (LightsaberPartType type : LightsaberPartType.values()) {
            this.setPartColor(type, colors.contains(type.getSerializedName(), Tag.TAG_INT)
                    ? colors.getInt(type.getSerializedName())
                    : ModularLightsaberData.getDefaultPartColor(type));
        }
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition).inflate(1.0D, 1.0D, 1.0D);
    }
}
