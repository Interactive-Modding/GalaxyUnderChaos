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
import server.galaxyunderchaos.entity.HeartBerryBoat;
import server.galaxyunderchaos.entity.HeartBerryChestBoat;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.Map;
import java.util.stream.Stream;

public class HBBoatRenderer extends BoatRenderer {

    private final Map<HeartBerryBoat.Type, Pair<ResourceLocation, ListModel<Boat>>> boatResources;

    public HBBoatRenderer(EntityRendererProvider.Context ctx, boolean chestBoat) {
        super(ctx, chestBoat);

        this.boatResources =
                Stream.of(HeartBerryBoat.Type.values()).collect(
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

    private static String texturePath(HeartBerryBoat.Type type, boolean chest) {
        return (chest ? "textures/entity/chest_boat/" : "textures/entity/boat/")
                + type.getSerializedName() + ".png";
    }

    private BoatModel createBoatModel(EntityRendererProvider.Context ctx,
                                      HeartBerryBoat.Type type,
                                      boolean chest) {
        ModelLayerLocation layer =
                chest ? createChestBoatModelName(type) : createBoatModelName(type);

        ModelPart baked = ctx.bakeLayer(layer);
        return chest ? new ChestBoatModel(baked) : new BoatModel(baked);
    }

    // layer‑location factories --------------------------------------------------

    public static ModelLayerLocation createBoatModelName(HeartBerryBoat.Type t) {
        return createLocation("boat/" + t.getSerializedName(), "main");
    }

    public static ModelLayerLocation createChestBoatModelName(HeartBerryBoat.Type t) {
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
        if (boat instanceof HeartBerryBoat heartBerryBoat) {
            return boatResources.get(heartBerryBoat.getModVariant());
        } else if (boat instanceof HeartBerryChestBoat chest) {
            return boatResources.get(chest.getModVariant());
        }
        return null; // fallback shouldn’t happen
    }
}
