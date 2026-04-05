package client.renderer;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import server.galaxyunderchaos.entity.AkBoat;
import server.galaxyunderchaos.entity.AkChestBoat;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.Map;
import java.util.stream.Stream;

public class AkBoatRenderer extends BoatRenderer {

    private final Map<AkBoat.Type, Pair<ResourceLocation, ListModel<Boat>>> boatResources;

    public AkBoatRenderer(EntityRendererProvider.Context ctx, boolean chestBoat) {
        super(ctx, chestBoat);

        this.boatResources =
                Stream.of(AkBoat.Type.values()).collect(
                        ImmutableMap.toImmutableMap(
                                type -> type,
                                type -> Pair.of(
                                        // correct factory call
                                        new ResourceLocation(
                                                galaxyunderchaos.MODID,
                                                texturePath(type, chestBoat)),
                                        // model instance
                                        createBoatModel(ctx, type, chestBoat)
                                )
                        ));
    }

    // ------------------------------------------------------------------ helpers

    private static String texturePath(AkBoat.Type type, boolean chest) {
        return (chest ? "textures/entity/chest_boat/" : "textures/entity/boat/")
                + type.getSerializedName() + ".png";
    }

    private BoatModel createBoatModel(EntityRendererProvider.Context ctx,
                                      AkBoat.Type type,
                                      boolean chest) {
        ModelLayerLocation layer =
                chest ? createChestBoatModelName(type) : createBoatModelName(type);

        ModelPart baked = ctx.bakeLayer(layer);
        return chest ? new ChestBoatModel(baked) : new BoatModel(baked);
    }

    // layer‑location factories --------------------------------------------------

    public static ModelLayerLocation createBoatModelName(AkBoat.Type t) {
        return createLocation("boat/" + t.getSerializedName(), "main");
    }

    public static ModelLayerLocation createChestBoatModelName(AkBoat.Type t) {
        return createLocation("chest_boat/" + t.getSerializedName(), "main");
    }

    private static ModelLayerLocation createLocation(String path, String part) {
        return new ModelLayerLocation(
                new ResourceLocation(galaxyunderchaos.MODID, path),
                part);
    }

    // ------------------------------------------------------------------ public API

    @Override
    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
        if (boat instanceof AkBoat ak) {
            return boatResources.get(ak.getModVariant());
        } else if (boat instanceof AkChestBoat chest) {
            return boatResources.get(chest.getModVariant());
        }
        return null; // fallback shouldn’t happen
    }
}
