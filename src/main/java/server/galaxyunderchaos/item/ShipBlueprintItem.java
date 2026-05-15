package server.galaxyunderchaos.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShipBlueprintItem extends Item {
    private final String shipId;
    private final String displayShipName;

    public ShipBlueprintItem(String shipId, String displayShipName, Properties properties) {
        super(properties);
        this.shipId = shipId;
        this.displayShipName = displayShipName;
    }

    public String getShipId() {
        return shipId;
    }

    public String getDisplayShipName() {
        return displayShipName;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Required in a Ship Crafting Table").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Builds: " + displayShipName).withStyle(ChatFormatting.AQUA));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
