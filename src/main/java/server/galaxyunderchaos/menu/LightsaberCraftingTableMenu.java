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
import server.galaxyunderchaos.entity.LightsaberCraftingTableBlockEntity;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.item.HiltItem;
import server.galaxyunderchaos.item.LightsaberPartItem;
import server.galaxyunderchaos.lightsaber.BladeModifierCrystal;
import server.galaxyunderchaos.lightsaber.LightsaberCraftingTableLogic;
import server.galaxyunderchaos.lightsaber.LightsaberPartType;
import server.galaxyunderchaos.lightsaber.ModularLightsaberData;

public class LightsaberCraftingTableMenu extends AbstractContainerMenu {
    public static final int RESULT_SLOT = LightsaberCraftingTableLogic.INPUT_SLOT_COUNT;

    public static final int PLAYER_INV_X = 47;
    public static final int PLAYER_INV_Y = 184;
    public static final int HOTBAR_Y = 242;
    public static final int RESULT_SLOT_X = 225;
    public static final int RESULT_SLOT_Y = 86;

    private static final int PLAYER_INV_START = RESULT_SLOT + 1;
    private static final int PLAYER_INV_END = PLAYER_INV_START + 27;
    private static final int HOTBAR_START = PLAYER_INV_END;
    private static final int HOTBAR_END = HOTBAR_START + 9;

    private static final int DATA_MODE_INDEX = 0;
    private static final int DATA_COLOR_START = 1;
    private static final int DATA_COUNT = DATA_COLOR_START + LightsaberPartType.values().length * 3;

    private static final int[][] SLOT_POSITIONS = {
            {20, 27},  // emitter
            {20, 47},  // switch
            {20, 67},  // grip
            {20, 87},  // pommel

            {40, 27},  // emitter
            {40, 47},  // switch
            {40, 67},  // grip
            {40, 87},  // pommel
    };

    private final Container inputContainer;
    private final Container resultContainer = new SimpleContainer(1);
    private final ContainerLevelAccess access;
    private final ContainerData tableData;
    private LightsaberCraftingTableLogic.CraftMode lastMode = LightsaberCraftingTableLogic.CraftMode.NONE;

    public LightsaberCraftingTableMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, resolveContainer(playerInventory, extraData), ContainerLevelAccess.NULL, createDefaultData());
    }

    public LightsaberCraftingTableMenu(int containerId, Inventory playerInventory, LightsaberCraftingTableBlockEntity blockEntity) {
        this(containerId, playerInventory, blockEntity, ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), new LightsaberTableData(blockEntity));
    }

    private LightsaberCraftingTableMenu(int containerId, Inventory playerInventory, Container container, ContainerLevelAccess access, ContainerData tableData) {
        super(ModMenuTypes.LIGHTSABER_CRAFTING_TABLE.get(), containerId);
        checkContainerSize(container, LightsaberCraftingTableLogic.INPUT_SLOT_COUNT);
        checkContainerDataCount(tableData, DATA_COUNT);
        this.inputContainer = container;
        this.access = access;
        this.tableData = tableData;

        addInputSlots();
        this.addSlot(new ResultSlot(this.resultContainer, 0, RESULT_SLOT_X, RESULT_SLOT_Y));
        addPlayerInventory(playerInventory);
        addDataSlots(tableData);
        slotsChanged(this.inputContainer);
    }

    private static Container resolveContainer(Inventory playerInventory, FriendlyByteBuf extraData) {
        if (extraData == null) {
            return new SimpleContainer(LightsaberCraftingTableLogic.INPUT_SLOT_COUNT);
        }
        net.minecraft.core.BlockPos pos = extraData.readBlockPos();
        if (playerInventory.player.level().getBlockEntity(pos) instanceof LightsaberCraftingTableBlockEntity blockEntity) {
            return blockEntity;
        }
        return new SimpleContainer(LightsaberCraftingTableLogic.INPUT_SLOT_COUNT);
    }

    private static ContainerData createDefaultData() {
        SimpleContainerData data = new SimpleContainerData(DATA_COUNT);
        data.set(DATA_MODE_INDEX, LightsaberCraftingTableLogic.CraftMode.NONE.ordinal());
        for (LightsaberPartType type : LightsaberPartType.values()) {
            setPartColor(data, type, ModularLightsaberData.getDefaultPartColor(type));
        }
        return data;
    }

    private void addInputSlots() {
        for (int slot = 0; slot < LightsaberCraftingTableLogic.INPUT_SLOT_COUNT; ++slot) {
            int[] pos = SLOT_POSITIONS[slot];
            this.addSlot(new InputSlot(this.inputContainer, slot, pos[0], pos[1]));
        }
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
        LightsaberCraftingTableLogic.Evaluation evaluation = LightsaberCraftingTableLogic.evaluate(this.inputContainer,
                getPartColor(LightsaberPartType.EMITTER),
                getPartColor(LightsaberPartType.SWITCH_SECTION),
                getPartColor(LightsaberPartType.GRIP),
                getPartColor(LightsaberPartType.POMMEL));
        this.lastMode = evaluation.mode();
        this.tableData.set(DATA_MODE_INDEX, this.lastMode.ordinal());
        this.resultContainer.setItem(0, LightsaberCraftingTableLogic.activatePreview(evaluation.result()));
        this.broadcastChanges();
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, galaxyunderchaos.LIGHTSABER_CRAFTING_TABLE.get());
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
            if (!moveIntoInputSlots(rawStack)) {
                if (quickMovedSlotIndex < PLAYER_INV_END) {
                    if (!this.moveItemStackTo(rawStack, HOTBAR_START, HOTBAR_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(rawStack, PLAYER_INV_START, PLAYER_INV_END, false)) {
                    return ItemStack.EMPTY;
                }
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

    private boolean moveIntoInputSlots(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        if (stack.getItem() instanceof LightsaberPartItem partItem) {
            return switch (partItem.getPartType()) {
                case EMITTER -> this.moveItemStackTo(stack, LightsaberCraftingTableLogic.SLOT_EMITTER, LightsaberCraftingTableLogic.SLOT_EMITTER + 1, false);
                case SWITCH_SECTION -> this.moveItemStackTo(stack, LightsaberCraftingTableLogic.SLOT_SWITCH, LightsaberCraftingTableLogic.SLOT_SWITCH + 1, false);
                case GRIP -> this.moveItemStackTo(stack, LightsaberCraftingTableLogic.SLOT_GRIP, LightsaberCraftingTableLogic.SLOT_GRIP + 1, false);
                case POMMEL -> this.moveItemStackTo(stack, LightsaberCraftingTableLogic.SLOT_POMMEL, LightsaberCraftingTableLogic.SLOT_POMMEL + 1, false);
            };
        }

        if (LightsaberCraftingTableLogic.isCircuitry(stack)) {
            return this.moveItemStackTo(stack, LightsaberCraftingTableLogic.SLOT_CORE, LightsaberCraftingTableLogic.SLOT_CORE + 1, false);
        }

        if (stack.getItem() instanceof HiltItem) {
            return this.moveItemStackTo(stack, LightsaberCraftingTableLogic.SLOT_CORE, LightsaberCraftingTableLogic.SLOT_CORE + 1, false);
        }

        if (LightsaberCraftingTableLogic.isKyber(stack)) {
            return this.moveItemStackTo(stack, LightsaberCraftingTableLogic.SLOT_FLEX, LightsaberCraftingTableLogic.SLOT_FLEX + 1, false);
        }

        if (LightsaberCraftingTableLogic.isModifierCrystal(stack)) {
            if (this.moveItemStackTo(stack, LightsaberCraftingTableLogic.SLOT_MODIFIER_A, LightsaberCraftingTableLogic.SLOT_MODIFIER_A + 1, false)) {
                return true;
            }
            return this.moveItemStackTo(stack, LightsaberCraftingTableLogic.SLOT_MODIFIER_B, LightsaberCraftingTableLogic.SLOT_MODIFIER_B + 1, false);
        }

        if (LightsaberCraftingTableLogic.isFinishedSingleLightsaber(stack)) {
            if (this.moveItemStackTo(stack, LightsaberCraftingTableLogic.SLOT_CORE, LightsaberCraftingTableLogic.SLOT_CORE + 1, false)) {
                return true;
            }
            return this.moveItemStackTo(stack, LightsaberCraftingTableLogic.SLOT_FLEX, LightsaberCraftingTableLogic.SLOT_FLEX + 1, false);
        }

        return false;
    }

    public LightsaberCraftingTableLogic.CraftMode getCraftMode() {
        int ordinal = this.tableData.get(DATA_MODE_INDEX);
        LightsaberCraftingTableLogic.CraftMode[] values = LightsaberCraftingTableLogic.CraftMode.values();
        return ordinal >= 0 && ordinal < values.length ? values[ordinal] : LightsaberCraftingTableLogic.CraftMode.NONE;
    }

    public ItemStack getPreviewStack() {
        return this.resultContainer.getItem(0);
    }

    public Container getInputContainer() {
        return inputContainer;
    }

    public int getPartColor(LightsaberPartType type) {
        return getPartColor(this.tableData, type);
    }

    public void setPartColor(LightsaberPartType type, int color) {
        setPartColor(this.tableData, type, color);
        slotsChanged(this.inputContainer);
    }

    private static int getPartColor(ContainerData data, LightsaberPartType type) {
        int index = DATA_COLOR_START + type.ordinal() * 3;
        int red = data.get(index) & 255;
        int green = data.get(index + 1) & 255;
        int blue = data.get(index + 2) & 255;
        return (red << 16) | (green << 8) | blue;
    }

    private static void setPartColor(ContainerData data, LightsaberPartType type, int color) {
        int safeColor = ModularLightsaberData.clampPartColor(color);
        int index = DATA_COLOR_START + type.ordinal() * 3;
        data.set(index, (safeColor >> 16) & 255);
        data.set(index + 1, (safeColor >> 8) & 255);
        data.set(index + 2, safeColor & 255);
    }

    private final class InputSlot extends Slot {
        private final int slotIndex;

        private InputSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
            this.slotIndex = slot;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public void setChanged() {
            super.setChanged();
            LightsaberCraftingTableMenu.this.slotsChanged(inputContainer);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            if (stack.isEmpty()) {
                return false;
            }
            return switch (slotIndex) {
                case LightsaberCraftingTableLogic.SLOT_EMITTER -> LightsaberCraftingTableLogic.isPartType(stack, LightsaberPartType.EMITTER);
                case LightsaberCraftingTableLogic.SLOT_SWITCH -> LightsaberCraftingTableLogic.isPartType(stack, LightsaberPartType.SWITCH_SECTION);
                case LightsaberCraftingTableLogic.SLOT_GRIP -> LightsaberCraftingTableLogic.isPartType(stack, LightsaberPartType.GRIP);
                case LightsaberCraftingTableLogic.SLOT_POMMEL -> LightsaberCraftingTableLogic.isPartType(stack, LightsaberPartType.POMMEL);
                case LightsaberCraftingTableLogic.SLOT_CORE -> LightsaberCraftingTableLogic.isCircuitry(stack) || stack.getItem() instanceof HiltItem || LightsaberCraftingTableLogic.isFinishedSingleLightsaber(stack);
                case LightsaberCraftingTableLogic.SLOT_FLEX -> LightsaberCraftingTableLogic.isKyber(stack) || LightsaberCraftingTableLogic.isFinishedSingleLightsaber(stack);
                case LightsaberCraftingTableLogic.SLOT_MODIFIER_A, LightsaberCraftingTableLogic.SLOT_MODIFIER_B -> {
                    BladeModifierCrystal crystal = BladeModifierCrystal.fromStack(stack);
                    if (crystal == null) {
                        yield false;
                    }
                    int other = slotIndex == LightsaberCraftingTableLogic.SLOT_MODIFIER_A ? LightsaberCraftingTableLogic.SLOT_MODIFIER_B : LightsaberCraftingTableLogic.SLOT_MODIFIER_A;
                    BladeModifierCrystal otherCrystal = BladeModifierCrystal.fromStack(inputContainer.getItem(other));
                    yield crystal != otherCrystal;
                }
                default -> false;
            };
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
            return !getItem().isEmpty() && getCraftMode() != LightsaberCraftingTableLogic.CraftMode.NONE;
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            LightsaberCraftingTableLogic.consumeIngredients(inputContainer, lastMode);
            access.execute((level, pos) -> level.playSound(null, pos, net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_BREAK, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F));
            slotsChanged(inputContainer);
            super.onTake(player, stack);
        }
    }

    private static final class LightsaberTableData implements ContainerData {
        private final LightsaberCraftingTableBlockEntity blockEntity;
        private int modeOrdinal = LightsaberCraftingTableLogic.CraftMode.NONE.ordinal();

        private LightsaberTableData(LightsaberCraftingTableBlockEntity blockEntity) {
            this.blockEntity = blockEntity;
        }

        @Override
        public int get(int index) {
            if (index == DATA_MODE_INDEX) {
                return modeOrdinal;
            }
            LightsaberPartType type = LightsaberPartType.values()[(index - DATA_COLOR_START) / 3];
            int color = blockEntity.getPartColor(type);
            return switch ((index - DATA_COLOR_START) % 3) {
                case 0 -> (color >> 16) & 255;
                case 1 -> (color >> 8) & 255;
                default -> color & 255;
            };
        }

        @Override
        public void set(int index, int value) {
            if (index == DATA_MODE_INDEX) {
                this.modeOrdinal = value;
                return;
            }
            LightsaberPartType type = LightsaberPartType.values()[(index - DATA_COLOR_START) / 3];
            int color = blockEntity.getPartColor(type);
            int safeValue = value & 255;
            int updated = switch ((index - DATA_COLOR_START) % 3) {
                case 0 -> (color & 0x00FFFF) | (safeValue << 16);
                case 1 -> (color & 0xFF00FF) | (safeValue << 8);
                default -> (color & 0xFFFF00) | safeValue;
            };
            blockEntity.setPartColor(type, updated);
        }

        @Override
        public int getCount() {
            return DATA_COUNT;
        }
    }
}
