package client.renderer;

import client.model.AcidSpiderModel;
import client.model.WingmawModel;
import client.model.VonskrModel;
import client.particle.CoffinRuneParticle;
import client.renderer.forceuser.ForceUserRenderer;
import client.renderer.ship.FlashfireRenderer;
import client.renderer.ship.NovadiveRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import server.galaxyunderchaos.item.ModItemProperties;

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
        e.registerEntityRenderer(galaxyunderchaos.ACID_SPIDER.get(), AcidSpiderRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.WINGMAW.get(), WingmawRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.VONSKR.get(), VonskrRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.JEDI_FORCE_USER.get(), ForceUserRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.SITH_FORCE_USER.get(), ForceUserRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.SITH_GHOST.get(), ForceUserRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.SITH_LORD_GHOST.get(), ForceUserRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.SITH_LORD.get(), ForceUserRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.JEDI_MASTER.get(), ForceUserRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.NEUTRAL_FORCE_USER.get(), ForceUserRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.NEUTRAL_MASTER.get(), ForceUserRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.SITH_APPRENTICE.get(), ForceUserRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.JEDI_PADAWAN.get(), ForceUserRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.NEUTRAL_PADAWAN.get(), ForceUserRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.JEDI_TEMPLE_GUARD.get(), ForceUserRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.SITH_GUARD.get(), ForceUserRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.SEAT.get(), SeatRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.FORCE_BEAM_EFFECT.get(), ForceBeamEffectRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.FORCE_PUSH_WAVE.get(), ForcePushWaveRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.FORCE_ABILITY_EFFECT.get(), ForceAbilityEffectRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.FORCE_PROJECTION_CLONE.get(), ForceProjectionCloneRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.THROWN_LIGHTSABER.get(), ThrownLightsaberRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.NOVADIVE_ENTITY.get(), NovadiveRenderer::new);
        e.registerEntityRenderer(galaxyunderchaos.FLASHFIRE_ENTITY.get(), FlashfireRenderer::new);

        e.registerEntityRenderer(ModEntityTypes.AK_BOAT.get(), ctx -> new AkBoatRenderer(ctx, false));
        e.registerEntityRenderer(ModEntityTypes.AK_CHEST_BOAT.get(), ctx -> new AkBoatRenderer(ctx, true));
        e.registerEntityRenderer(ModEntityTypes.DILLIA_BOAT.get(), ctx -> new DilliaBoatRenderer(ctx, false));
        e.registerEntityRenderer(ModEntityTypes.DILLIA_CHEST_BOAT.get(), ctx -> new DilliaBoatRenderer(ctx, true));
        e.registerEntityRenderer(ModEntityTypes.NABOO_PINE_BOAT.get(), ctx -> new NabooPineBoatRenderer(ctx, false));
        e.registerEntityRenderer(ModEntityTypes.NABOO_PINE_CHEST_BOAT.get(), ctx -> new NabooPineBoatRenderer(ctx, true));
        e.registerEntityRenderer(ModEntityTypes.HEART_BERRY_BOAT.get(), ctx -> new HBBoatRenderer(ctx, false));
        e.registerEntityRenderer(ModEntityTypes.HEART_BERRY_CHEST_BOAT.get(), ctx -> new HBBoatRenderer(ctx, true));
        e.registerEntityRenderer(ModEntityTypes.BLBA_BOAT.get(), ctx -> new BlbaBoatRenderer(ctx, false));
        e.registerEntityRenderer(ModEntityTypes.BLBA_CHEST_BOAT.get(), ctx -> new BlbaBoatRenderer(ctx, true));
        e.registerEntityRenderer(ModEntityTypes.CAMBYLICTUS_BOAT.get(), ctx -> new CambylictusBoatRenderer(ctx, false));
        e.registerEntityRenderer(ModEntityTypes.CAMBYLICTUS_CHEST_BOAT.get(), ctx -> new CambylictusBoatRenderer(ctx, true));
        e.registerEntityRenderer(ModEntityTypes.PERLOTE_BOAT.get(), ctx -> new PerloteBoatRenderer(ctx, false));
        e.registerEntityRenderer(ModEntityTypes.PERLOTE_CHEST_BOAT.get(), ctx -> new PerloteBoatRenderer(ctx, true));
        e.registerEntityRenderer(ModEntityTypes.RUTIGER_BOAT.get(), ctx -> new RutigerBoatRenderer(ctx, false));
        e.registerEntityRenderer(ModEntityTypes.RUTIGER_CHEST_BOAT.get(), ctx -> new RutigerBoatRenderer(ctx, true));
        e.registerEntityRenderer(ModEntityTypes.POLAR_BOAT.get(), ctx -> new PolarBoatRenderer(ctx, false));
        e.registerEntityRenderer(ModEntityTypes.POLAR_CHEST_BOAT.get(), ctx -> new PolarBoatRenderer(ctx, true));

        e.registerBlockEntityRenderer(ModBlockEntities.AK_SIGN_BE.get(), SignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.AK_HANGING_SIGN_BE.get(), HangingSignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.DILLIA_SIGN_BE.get(), SignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.DILLIA_HANGING_SIGN_BE.get(), HangingSignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.NABOO_PINE_SIGN_BE.get(), SignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.NABOO_PINE_HANGING_SIGN_BE.get(), HangingSignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.HEART_BERRY_SIGN_BE.get(), SignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.HEART_BERRY_HANGING_SIGN_BE.get(), HangingSignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.BLBA_SIGN_BE.get(), SignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.BLBA_HANGING_SIGN_BE.get(), HangingSignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.CAMBYLICTUS_SIGN_BE.get(), SignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.CAMBYLICTUS_HANGING_SIGN_BE.get(), HangingSignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.PERLOTE_SIGN_BE.get(), SignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.PERLOTE_HANGING_SIGN_BE.get(), HangingSignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.RUTIGER_SIGN_BE.get(), SignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.RUTIGER_HANGING_SIGN_BE.get(), HangingSignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.POLAR_SIGN_BE.get(), SignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.POLAR_HANGING_SIGN_BE.get(), HangingSignRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.SABER_STAND_BE.get(), GroundSaberStandRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.BLEEDING_TABLE_BE.get(), BleedingTableRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.LIGHTSABER_CRAFTING_TABLE_BE.get(), LightsaberCraftingTableRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.SHIP_CRAFTING_TABLE_BE.get(), ShipCraftingTableRenderer::new);
        e.registerBlockEntityRenderer(ModBlockEntities.COFFIN_BE.get(), CoffinGeoRenderer::new);
    }
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(AcidSpiderModel.LAYER_LOCATION, AcidSpiderModel::createBodyLayer);
        event.registerLayerDefinition(WingmawModel.LAYER_LOCATION, WingmawModel::createBodyLayer);
        event.registerLayerDefinition(VonskrModel.LAYER_LOCATION, VonskrModel::createBodyLayer);
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
        e.enqueueWork(() -> {
            ModItemProperties.addCustomItemProperties();
            Sheets.addWoodType(galaxyunderchaos.AK_WOOD_TYPE);
            Sheets.addWoodType(galaxyunderchaos.DILLIA_WOOD_TYPE);
            Sheets.addWoodType(galaxyunderchaos.NABOO_PINE_WOOD_TYPE);
            Sheets.addWoodType(galaxyunderchaos.HEART_BERRY_WOOD_TYPE);
            Sheets.addWoodType(galaxyunderchaos.BLBA_WOOD_TYPE);
            Sheets.addWoodType(galaxyunderchaos.CAMBYLICTUS_WOOD_TYPE);
            Sheets.addWoodType(galaxyunderchaos.PERLOTE_WOOD_TYPE);
            Sheets.addWoodType(galaxyunderchaos.RUTIGER_WOOD_TYPE);
            Sheets.addWoodType(galaxyunderchaos.POLAR_WOOD_TYPE);

            MenuScreens.register(ModMenuTypes.LIGHTSABER_CRAFTING_TABLE.get(), LightsaberCraftingTableScreen::new);
            MenuScreens.register(ModMenuTypes.SHIP_CRAFTING_TABLE.get(), ShipCraftingTableScreen::new);
            MenuScreens.register(ModMenuTypes.FORCE_HOLOCRON.get(), ForceHolocronScreen::new);
            MenuScreens.register(ModMenuTypes.BLEEDING_TABLE.get(), BleedingTableScreen::new);

            ItemBlockRenderTypes.setRenderLayer(galaxyunderchaos.GROUND_SABER_STAND.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(galaxyunderchaos.WHITE_GROUND_SABER_STAND.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(galaxyunderchaos.MALACHOR_TEMPLE_STONE_GLASS.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(galaxyunderchaos.MALACHOR_TEMPLE_STONE_GLASS_2.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(galaxyunderchaos.MALACHOR_TEMPLE_STONE_GLASS_3.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(galaxyunderchaos.MALACHOR_TEMPLE_STONE_GLASS_4.get(), RenderType.translucent());
            java.util.List.of(
                    galaxyunderchaos.BLBA_LEAVES.get(),
                    galaxyunderchaos.BLBA_SAPLING.get(),
                    galaxyunderchaos.DILLIA_LEAVES.get(),
                    galaxyunderchaos.DILLIA_SAPLING.get(),
                    galaxyunderchaos.DILLIA_DOOR_BLOCK.get(),
                    galaxyunderchaos.DILLIA_TRAPDOOR_BLOCK.get(),
                    galaxyunderchaos.BLBA_DOOR_BLOCK.get(),
                    galaxyunderchaos.BLBA_TRAPDOOR_BLOCK.get(),
                    galaxyunderchaos.CAMBYLICTUS_DOOR_BLOCK.get(),
                    galaxyunderchaos.CAMBYLICTUS_TRAPDOOR_BLOCK.get(),
                    galaxyunderchaos.PERLOTE_DOOR_BLOCK.get(),
                    galaxyunderchaos.PERLOTE_TRAPDOOR_BLOCK.get(),
                    galaxyunderchaos.RUTIGER_DOOR_BLOCK.get(),
                    galaxyunderchaos.RUTIGER_TRAPDOOR_BLOCK.get(),
                    galaxyunderchaos.POLAR_DOOR_BLOCK.get(),
                    galaxyunderchaos.POLAR_TRAPDOOR_BLOCK.get(),
                    galaxyunderchaos.BELLEW_FLOWER.get(),
                    galaxyunderchaos.CAMBYLICTUS_LEAVES.get(),
                    galaxyunderchaos.CAMBYLICTUS_SAPLING.get(),
                    galaxyunderchaos.PERLOTE_LEAVES.get(),
                    galaxyunderchaos.PERLOTE_SAPLING.get(),
                    galaxyunderchaos.RUTIGER_LEAVES.get(),
                    galaxyunderchaos.RUTIGER_SAPLING.get(),
                    galaxyunderchaos.POLAR_LEAVES.get(),
                    galaxyunderchaos.POLAR_SAPLING.get(),
                    galaxyunderchaos.NABOO_PINE_LEAVES.get(),
                    galaxyunderchaos.NABOO_PINE_SAPLING.get(),
                    galaxyunderchaos.NABOO_PINE_DOOR_BLOCK.get(),
                    galaxyunderchaos.NABOO_PINE_TRAPDOOR_BLOCK.get(),
                    galaxyunderchaos.QUEENS_HEART_FLOWER.get(),
                    galaxyunderchaos.HEART_BERRY_LEAVES.get(),
                    galaxyunderchaos.HEART_BERRY_FRUIT_LEAVES.get(),
                    galaxyunderchaos.HEART_BERRY_SAPLING.get(),
                    galaxyunderchaos.AK_LEAVES.get(),
                    galaxyunderchaos.AK_SAPLING.get(),
                    galaxyunderchaos.AK_DOOR_BLOCK.get(),
                    galaxyunderchaos.AK_TRAPDOOR_BLOCK.get(),
                    galaxyunderchaos.HEART_BERRY_DOOR_BLOCK.get(),
                    galaxyunderchaos.HEART_BERRY_TRAPDOOR_BLOCK.get()
            ).forEach(block -> ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout()));

            galaxyunderchaos.LIGHTSABERS.values().forEach(reg -> ItemBlockRenderTypes.setRenderLayer(
                    Block.byItem(reg.get()),
                    RenderType.translucent()
            ));
        });
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(galaxyunderchaos.JEDI_COFFIN_PARTICLE.get(), CoffinRuneParticle.JediProvider::new);
        event.registerSpriteSet(galaxyunderchaos.SITH_COFFIN_PARTICLE.get(), CoffinRuneParticle.SithProvider::new);
    }

}
