package server.galaxyunderchaos.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import server.galaxyunderchaos.lightsaber.BladeModifierCrystal;

import java.util.List;

public class BladeModifierCrystalItem extends Item {
    private final BladeModifierCrystal crystal;

    public BladeModifierCrystalItem(BladeModifierCrystal crystal, Properties properties) {
        super(properties);
        this.crystal = crystal;
    }

    public BladeModifierCrystal getCrystal() {
        return crystal;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Blade modifier crystal"));
        tooltip.add(Component.literal("Use on the Lightsaber Crafting Table with a finished saber."));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
