package client.renderer;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.gui.screens.MenuScreens;

import client.model.BleedingTableModel;
import client.screen.LightsaberCraftingTableScreen;
import client.screen.ShipCraftingTableScreen;
import client.screen.ForceHolocronScreen;
import client.screen.BleedingTableScreen;
import server.galaxyunderchaos.entity.ModBlockEntities;
import server.galaxyunderchaos.entity.ModEntityTypes;
import server.galaxyunderchaos.galaxyunderchaos;
import server.galaxyunderchaos.menu.ModMenuTypes;
import client.renderer.BleedingTableRenderer;
import client.renderer.forceuser.ForceUserModelLayers;
import client.renderer.forceuser.PlayerForceSpeciesLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;

import java.lang.reflect.Field;
import java.util.List;

@Mod.EventBusSubscriber(
        modid = galaxyunderchaos.MODID,
        value  = Dist.CLIENT,
        bus    = Mod.EventBusSubscriber.Bus.MOD)
public final class ClientModEvents {

    /* ──────────────────────────────
       Tell Forge which renderer class
       to use for each entity / BE
       ────────────────────────────── */
    @SubscribeEvent
    public static void renderers(EntityRenderersEvent.RegisterRenderers e) {
        e.registerBlockEntityRenderer(ModBlockEntities.AK_SIGN_BE.get(),
                SignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.AK_HANGING_SIGN_BE.get(),
                HangingSignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.DILLIA_SIGN_BE.get(),
                SignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.DILLIA_HANGING_SIGN_BE.get(),
                HangingSignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.NABOO_PINE_SIGN_BE.get(),
                SignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.NABOO_PINE_HANGING_SIGN_BE.get(),
                HangingSignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.HEART_BERRY_SIGN_BE.get(),
                SignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.HEART_BERRY_HANGING_SIGN_BE.get(),
                HangingSignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.BLEEDING_TABLE_BE.get(),
                BleedingTableRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.LIGHTSABER_CRAFTING_TABLE_BE.get(),
                LightsaberCraftingTableRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.SHIP_CRAFTING_TABLE_BE.get(),
                ShipCraftingTableRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.COFFIN_BE.get(),
                CoffinGeoRenderer::new);

    }
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.AK_BOAT_LAYER, BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayers.AK_CHEST_BOAT_LAYER, ChestBoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayers.DILLIA_BOAT_LAYER, BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayers.DILLIA_CHEST_BOAT_LAYER, ChestBoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayers.NABOO_PINE_BOAT_LAYER, BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayers.NABOO_PINE_CHEST_BOAT_LAYER, ChestBoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayers.HEART_BERRY_BOAT_LAYER, BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayers.HEART_BERRY_CHEST_BOAT_LAYER, ChestBoatModel::createBodyModel);
        event.registerLayerDefinition(BleedingTableModel.LAYER_LOCATION, BleedingTableModel::createBodyLayer);
        ForceUserModelLayers.register(event);
    }

    @SubscribeEvent
    public static void addPlayerLayers(EntityRenderersEvent.AddLayers event) {
        PlayerRenderer defaultRenderer = event.getSkin("default");
        if (defaultRenderer != null && !hasPlayerForceSpeciesLayer(defaultRenderer)) {
            defaultRenderer.addLayer(new PlayerForceSpeciesLayer(defaultRenderer, event.getEntityModels()));
        }
        PlayerRenderer slimRenderer = event.getSkin("slim");
        if (slimRenderer != null && !hasPlayerForceSpeciesLayer(slimRenderer)) {
            slimRenderer.addLayer(new PlayerForceSpeciesLayer(slimRenderer, event.getEntityModels()));
        }
    }

    private static boolean hasPlayerForceSpeciesLayer(PlayerRenderer renderer) {
        try {
            Field layersField = LivingEntityRenderer.class.getDeclaredField("layers");
            layersField.setAccessible(true);
            Object value = layersField.get(renderer);
            if (!(value instanceof List<?> layers)) {
                return false;
            }
            for (Object layer : layers) {
                if (layer instanceof PlayerForceSpeciesLayer) {
                    return true;
                }
            }
        } catch (ReflectiveOperationException ignored) {
            // If reflection fails, allow Forge to add the layer once through the
            // normal path instead of breaking player rendering.
        }
        return false;
    }
    /* ──────────────────────────────
       Register Ak as a wood‑type so
       the two Sign renderers get
       their Materials & textures
       ────────────────────────────── */
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent e) {
        e.enqueueWork(() -> Sheets.addWoodType(galaxyunderchaos.AK_WOOD_TYPE));
        e.enqueueWork(() -> Sheets.addWoodType(galaxyunderchaos.DILLIA_WOOD_TYPE));
        e.enqueueWork(() -> Sheets.addWoodType(galaxyunderchaos.NABOO_PINE_WOOD_TYPE));
        e.enqueueWork(() -> Sheets.addWoodType(galaxyunderchaos.HEART_BERRY_WOOD_TYPE));
        e.enqueueWork(() -> MenuScreens.register(ModMenuTypes.LIGHTSABER_CRAFTING_TABLE.get(), LightsaberCraftingTableScreen::new));
        e.enqueueWork(() -> MenuScreens.register(ModMenuTypes.SHIP_CRAFTING_TABLE.get(), ShipCraftingTableScreen::new));
        e.enqueueWork(() -> MenuScreens.register(ModMenuTypes.FORCE_HOLOCRON.get(), ForceHolocronScreen::new));
        e.enqueueWork(() -> MenuScreens.register(ModMenuTypes.BLEEDING_TABLE.get(), BleedingTableScreen::new));
    }
}
