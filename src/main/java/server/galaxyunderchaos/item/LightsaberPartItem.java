package server.galaxyunderchaos.item;

import client.renderer.ModItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import server.galaxyunderchaos.lightsaber.LightsaberPartType;

import java.util.function.Consumer;

public class LightsaberPartItem extends Item {
    private final String familyId;
    private final LightsaberPartType partType;

    public LightsaberPartItem(String familyId, LightsaberPartType partType, Properties properties) {
        super(properties);
        this.familyId = familyId;
        this.partType = partType;
    }

    public String getFamilyId() {
        return familyId;
    }

    public LightsaberPartType getPartType() {
        return partType;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, java.util.List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal(partType.getDisplayName() + ": " + familyId));
        tooltip.add(Component.literal("Drop on the Lightsaber Crafting Table with the other sections and a kyber crystal."));
        super.appendHoverText(stack, level, tooltip, flag);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer customRenderer = new ModItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return customRenderer;
            }
        });
    }
}
