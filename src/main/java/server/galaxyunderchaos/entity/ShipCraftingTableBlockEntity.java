package server.galaxyunderchaos.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import server.galaxyunderchaos.menu.ShipCraftingTableMenu;
import server.galaxyunderchaos.ship.ShipColorSection;
import server.galaxyunderchaos.ship.ShipCraftingTableLogic;
import server.galaxyunderchaos.ship.ShipCustomization;

public class ShipCraftingTableBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<net.minecraft.world.item.ItemStack> items = NonNullList.withSize(ShipCraftingTableLogic.INPUT_SLOT_COUNT, net.minecraft.world.item.ItemStack.EMPTY);
    private final int[] colors = {
            ShipCustomization.DEFAULT_BASE,
            ShipCustomization.DEFAULT_PRIMARY,
            ShipCustomization.DEFAULT_SECONDARY,
            ShipCustomization.DEFAULT_INTERIOR
    };

    public ShipCraftingTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SHIP_CRAFTING_TABLE_BE.get(), pos, state);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("menu.galaxyunderchaos.ship_crafting_table");
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new ShipCraftingTableMenu(containerId, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return ShipCraftingTableLogic.INPUT_SLOT_COUNT;
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

    public int getColor(ShipColorSection section) {
        return colors[section.ordinal()];
    }

    public void setColor(ShipColorSection section, int color) {
        colors[section.ordinal()] = ShipCustomization.clampColor(color);
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items);
        }
        for (ShipColorSection section : ShipColorSection.values()) {
            tag.putInt(section.getNbtKey(), this.getColor(section));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(getContainerSize(), net.minecraft.world.item.ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items);
        }
        for (ShipColorSection section : ShipColorSection.values()) {
            this.setColor(section, tag.contains(section.getNbtKey()) ? tag.getInt(section.getNbtKey()) : ShipCustomization.getDefaultColor(section));
        }
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition).inflate(1.0D, 1.0D, 1.0D);
    }
}
