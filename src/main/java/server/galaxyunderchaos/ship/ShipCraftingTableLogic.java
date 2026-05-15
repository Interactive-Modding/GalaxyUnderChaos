package server.galaxyunderchaos.ship;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public final class ShipCraftingTableLogic {
    public static final int SLOT_BLUEPRINT = 0;
    public static final int INPUT_SLOT_COUNT = 1;

    private ShipCraftingTableLogic() {
    }

    public static boolean isBlueprint(ItemStack stack) {
        return ShipCustomization.isBlueprint(stack);
    }

    public static ItemStack evaluate(Container container, int base, int primary, int secondary, int interior) {
        ItemStack blueprint = container.getItem(SLOT_BLUEPRINT);
        if (!isBlueprint(blueprint)) {
            return ItemStack.EMPTY;
        }

        String shipId = ShipCustomization.getBlueprintShipId(blueprint);
        return ShipCustomization.createShipStack(shipId, base, primary, secondary, interior);
    }

    public static void consumeBlueprint(Container container) {
        ItemStack blueprint = container.getItem(SLOT_BLUEPRINT);
        if (!blueprint.isEmpty()) {
            blueprint.shrink(1);
            if (blueprint.isEmpty()) {
                container.setItem(SLOT_BLUEPRINT, ItemStack.EMPTY);
            }
            container.setChanged();
        }
    }
}
