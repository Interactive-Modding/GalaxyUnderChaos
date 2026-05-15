package server.galaxyunderchaos.menu;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import server.galaxyunderchaos.galaxyunderchaos;

public final class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, galaxyunderchaos.MODID);

    public static final RegistryObject<MenuType<LightsaberCraftingTableMenu>> LIGHTSABER_CRAFTING_TABLE =
            MENUS.register("lightsaber_crafting_table", () -> IForgeMenuType.create(LightsaberCraftingTableMenu::new));

    public static final RegistryObject<MenuType<ShipCraftingTableMenu>> SHIP_CRAFTING_TABLE =
            MENUS.register("ship_crafting_table", () -> IForgeMenuType.create(ShipCraftingTableMenu::new));

    public static final RegistryObject<MenuType<ForceHolocronMenu>> FORCE_HOLOCRON =
            MENUS.register("force_holocron", () -> IForgeMenuType.create(ForceHolocronMenu::new));

    public static final RegistryObject<MenuType<BleedingTableMenu>> BLEEDING_TABLE =
            MENUS.register("bleeding_table", () -> IForgeMenuType.create(BleedingTableMenu::new));

    private ModMenuTypes() {
    }
}
