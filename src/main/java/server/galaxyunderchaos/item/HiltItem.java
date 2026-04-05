package server.galaxyunderchaos.item;

import client.renderer.ModItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import server.galaxyunderchaos.lightsaber.AdvancedLightsaberLegacyHilts;
import server.galaxyunderchaos.lightsaber.BladeModifierCrystal;
import server.galaxyunderchaos.lightsaber.LightsaberIdHelper;

public class HiltItem extends Item {
    private final String defaultBladeColor;
    @Nullable
    private final String explicitHiltId;

    public HiltItem(String defaultBladeColor, Properties properties) {
        this(null, defaultBladeColor, properties);
    }

    public HiltItem(@Nullable String hiltId, String defaultBladeColor, Properties properties) {
        super(properties);
        this.defaultBladeColor = defaultBladeColor;
        this.explicitHiltId = hiltId;
    }

    public String getColor() {
        return defaultBladeColor;
    }

    public String getDefaultBladeColor() {
        return defaultBladeColor;
    }

    public String getHiltId() {
        return explicitHiltId != null ? explicitHiltId : LightsaberIdHelper.resolveHiltId(this);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, java.util.List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Legacy hilt: " + getHiltId()));
        tooltip.add(Component.literal("Default blade color: " + defaultBladeColor));
        AdvancedLightsaberLegacyHilts.LegacyHiltSpec spec = AdvancedLightsaberLegacyHilts.HILTS.get(getHiltId());
        if (spec != null && spec.defaultBladeModifiers().length > 0) {
            tooltip.add(Component.literal("Built-in modifiers: " + Arrays.stream(spec.defaultBladeModifiers())
                    .map(BladeModifierCrystal::getSerializedName)
                    .collect(Collectors.joining(", "))));
        }
        tooltip.add(Component.literal("Drop this hilt and a kyber crystal onto the Lightsaber Crafting Table."));
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

