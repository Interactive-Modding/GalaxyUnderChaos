package server.galaxyunderchaos.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import server.galaxyunderchaos.force.ForceSide;
import server.galaxyunderchaos.galaxyunderchaos;

public class ForceHolocronMenu extends AbstractContainerMenu {
    private final BlockPos blockPos;
    private final ForceSide side;
    private final ContainerLevelAccess access;

    public ForceHolocronMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory,
                extraData != null ? extraData.readBlockPos() : BlockPos.ZERO,
                extraData != null ? ForceSide.values()[Math.max(0, Math.min(ForceSide.values().length - 1, extraData.readVarInt()))] : ForceSide.NEUTRAL);
    }

    public ForceHolocronMenu(int containerId, Inventory inventory, BlockPos pos, ForceSide side) {
        super(ModMenuTypes.FORCE_HOLOCRON.get(), containerId);
        this.blockPos = pos;
        this.side = side;
        this.access = ContainerLevelAccess.create(inventory.player.level(), pos);
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public ForceSide getSide() {
        return side;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.distanceToSqr(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D, blockPos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }
}
