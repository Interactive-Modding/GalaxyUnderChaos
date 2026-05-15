package server.galaxyunderchaos.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.entity.ShipCraftingTableBlockEntity;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.ship.ShipColorSection;
import server.galaxyunderchaos.ship.ShipCraftingTableLogic;
import server.galaxyunderchaos.ship.ShipCustomization;

public class ShipCraftingTableMenu extends AbstractContainerMenu {
    public static final int RESULT_SLOT = ShipCraftingTableLogic.INPUT_SLOT_COUNT;

    public static final int BLUEPRINT_SLOT_X = 20;
    public static final int BLUEPRINT_SLOT_Y = 17;
    public static final int RESULT_SLOT_X = 136;
    public static final int RESULT_SLOT_Y = 87;
    public static final int PLAYER_INV_X = 47;
    public static final int PLAYER_INV_Y = 184;
    public static final int HOTBAR_Y = 242;

    private static final int PLAYER_INV_START = RESULT_SLOT + 1;
    private static final int PLAYER_INV_END = PLAYER_INV_START + 27;
    private static final int HOTBAR_START = PLAYER_INV_END;
    private static final int HOTBAR_END = HOTBAR_START + 9;

    private static final int DATA_COUNT = 12;

    private final Container inputContainer;
    private final Container resultContainer = new SimpleContainer(1);
    private final ContainerLevelAccess access;
    private final ContainerData colorData;

    public ShipCraftingTableMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, resolveContainer(playerInventory, extraData), ContainerLevelAccess.NULL, createDefaultData());
    }

    public ShipCraftingTableMenu(int containerId, Inventory playerInventory, ShipCraftingTableBlockEntity blockEntity) {
        this(containerId, playerInventory, blockEntity, ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), new ShipTableColorData(blockEntity));
    }

    private ShipCraftingTableMenu(int containerId, Inventory playerInventory, Container container, ContainerLevelAccess access, ContainerData colorData) {
        super(ModMenuTypes.SHIP_CRAFTING_TABLE.get(), containerId);
        checkContainerSize(container, ShipCraftingTableLogic.INPUT_SLOT_COUNT);
        checkContainerDataCount(colorData, DATA_COUNT);
        this.inputContainer = container;
        this.access = access;
        this.colorData = colorData;

        this.addSlot(new BlueprintSlot(this.inputContainer, ShipCraftingTableLogic.SLOT_BLUEPRINT, BLUEPRINT_SLOT_X, BLUEPRINT_SLOT_Y));
        this.addSlot(new ResultSlot(this.resultContainer, 0, RESULT_SLOT_X, RESULT_SLOT_Y));
        addPlayerInventory(playerInventory);
        addDataSlots(colorData);
        slotsChanged(this.inputContainer);
    }

    private static Container resolveContainer(Inventory playerInventory, FriendlyByteBuf extraData) {
        if (extraData == null) {
            return new SimpleContainer(ShipCraftingTableLogic.INPUT_SLOT_COUNT);
        }
        net.minecraft.core.BlockPos pos = extraData.readBlockPos();
        if (playerInventory.player.level().getBlockEntity(pos) instanceof ShipCraftingTableBlockEntity blockEntity) {
            return blockEntity;
        }
        return new SimpleContainer(ShipCraftingTableLogic.INPUT_SLOT_COUNT);
    }

    private static ContainerData createDefaultData() {
        SimpleContainerData data = new SimpleContainerData(DATA_COUNT);
        for (ShipColorSection section : ShipColorSection.values()) {
            setColor(data, section, ShipCustomization.getDefaultColor(section));
        }
        return data;
    }

    private void addPlayerInventory(Inventory inventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(inventory, col + row * 9 + 9, PLAYER_INV_X + col * 18, PLAYER_INV_Y + row * 18));
            }
        }

        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(inventory, col, PLAYER_INV_X + col * 18, HOTBAR_Y));
        }
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        this.resultContainer.setItem(0, ShipCraftingTableLogic.evaluate(this.inputContainer,
                getColor(ShipColorSection.BASE),
                getColor(ShipColorSection.PRIMARY),
                getColor(ShipColorSection.SECONDARY),
                getColor(ShipColorSection.INTERIOR)));
        this.broadcastChanges();
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, galaxyunderchaos.SHIP_CRAFTING_TABLE.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int quickMovedSlotIndex) {
        ItemStack quickMovedStack = ItemStack.EMPTY;
        Slot quickMovedSlot = this.slots.get(quickMovedSlotIndex);
        if (quickMovedSlot == null || !quickMovedSlot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack rawStack = quickMovedSlot.getItem();
        quickMovedStack = rawStack.copy();

        if (quickMovedSlotIndex == RESULT_SLOT) {
            if (!this.moveItemStackTo(rawStack, PLAYER_INV_START, HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }
            quickMovedSlot.onQuickCraft(rawStack, quickMovedStack);
        } else if (quickMovedSlotIndex >= PLAYER_INV_START) {
            if (ShipCraftingTableLogic.isBlueprint(rawStack)) {
                if (!this.moveItemStackTo(rawStack, ShipCraftingTableLogic.SLOT_BLUEPRINT, ShipCraftingTableLogic.SLOT_BLUEPRINT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (quickMovedSlotIndex < PLAYER_INV_END) {
                if (!this.moveItemStackTo(rawStack, HOTBAR_START, HOTBAR_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(rawStack, PLAYER_INV_START, PLAYER_INV_END, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveItemStackTo(rawStack, PLAYER_INV_START, HOTBAR_END, false)) {
            return ItemStack.EMPTY;
        }

        if (rawStack.isEmpty()) {
            quickMovedSlot.set(ItemStack.EMPTY);
        } else {
            quickMovedSlot.setChanged();
        }

        if (rawStack.getCount() == quickMovedStack.getCount()) {
            return ItemStack.EMPTY;
        }

        quickMovedSlot.onTake(player, rawStack);
        return quickMovedStack;
    }

    public ItemStack getPreviewStack() {
        return this.resultContainer.getItem(0);
    }

    public Container getInputContainer() {
        return inputContainer;
    }

    public int getColor(ShipColorSection section) {
        return getColor(this.colorData, section);
    }

    public void setColor(ShipColorSection section, int color) {
        setColor(this.colorData, section, color);
        slotsChanged(this.inputContainer);
    }

    private static int getColor(ContainerData data, ShipColorSection section) {
        int index = section.ordinal() * 3;
        int red = data.get(index) & 255;
        int green = data.get(index + 1) & 255;
        int blue = data.get(index + 2) & 255;
        return (red << 16) | (green << 8) | blue;
    }

    private static void setColor(ContainerData data, ShipColorSection section, int color) {
        int safeColor = ShipCustomization.clampColor(color);
        int index = section.ordinal() * 3;
        data.set(index, (safeColor >> 16) & 255);
        data.set(index + 1, (safeColor >> 8) & 255);
        data.set(index + 2, safeColor & 255);
    }

    private final class BlueprintSlot extends Slot {
        private BlueprintSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return ShipCraftingTableLogic.isBlueprint(stack);
        }

        @Override
        public void setChanged() {
            super.setChanged();
            ShipCraftingTableMenu.this.slotsChanged(inputContainer);
        }
    }

    private final class ResultSlot extends Slot {
        private ResultSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean mayPickup(Player player) {
            return !getItem().isEmpty();
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            ShipCraftingTableLogic.consumeBlueprint(inputContainer);
            access.execute((level, pos) -> level.playSound(null, pos, net.minecraft.sounds.SoundEvents.IRON_TRAPDOOR_OPEN, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 0.75F));
            slotsChanged(inputContainer);
            super.onTake(player, stack);
        }
    }

    private static final class ShipTableColorData implements ContainerData {
        private final ShipCraftingTableBlockEntity blockEntity;

        private ShipTableColorData(ShipCraftingTableBlockEntity blockEntity) {
            this.blockEntity = blockEntity;
        }

        @Override
        public int get(int index) {
            ShipColorSection section = ShipColorSection.byOrdinal(index / 3);
            int color = blockEntity.getColor(section);
            return switch (index % 3) {
                case 0 -> (color >> 16) & 255;
                case 1 -> (color >> 8) & 255;
                default -> color & 255;
            };
        }

        @Override
        public void set(int index, int value) {
            ShipColorSection section = ShipColorSection.byOrdinal(index / 3);
            int color = blockEntity.getColor(section);
            int safeValue = value & 255;
            int updated = switch (index % 3) {
                case 0 -> (color & 0x00FFFF) | (safeValue << 16);
                case 1 -> (color & 0xFF00FF) | (safeValue << 8);
                default -> (color & 0xFFFF00) | safeValue;
            };
            blockEntity.setColor(section, updated);
        }

        @Override
        public int getCount() {
            return DATA_COUNT;
        }
    }
}
