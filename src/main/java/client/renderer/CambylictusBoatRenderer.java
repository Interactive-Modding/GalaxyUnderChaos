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
import server.galaxyunderchaos.entity.CambylictusBoat;
import server.galaxyunderchaos.entity.CambylictusChestBoat;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.Map;
import java.util.stream.Stream;

public class CambylictusBoatRenderer extends BoatRenderer {

    private final Map<CambylictusBoat.Type, Pair<ResourceLocation, ListModel<Boat>>> boatResources;

    public CambylictusBoatRenderer(EntityRendererProvider.Context ctx, boolean chestBoat) {
        super(ctx, chestBoat);
        this.boatResources = Stream.of(CambylictusBoat.Type.values()).collect(
                ImmutableMap.toImmutableMap(
                        type -> type,
                        type -> Pair.of(
                                new ResourceLocation(galaxyunderchaos.MODID, texturePath(type, chestBoat)),
                                createBoatModel(ctx, type, chestBoat)
                        )
                ));
    }

    private static String texturePath(CambylictusBoat.Type type, boolean chest) {
        return (chest ? "textures/entity/chest_boat/" : "textures/entity/boat/")
                + type.getSerializedName() + ".png";
    }

    private BoatModel createBoatModel(EntityRendererProvider.Context ctx, CambylictusBoat.Type type, boolean chest) {
        ModelLayerLocation layer = chest ? createChestBoatModelName(type) : createBoatModelName(type);
        ModelPart baked = ctx.bakeLayer(layer);
        return chest ? new ChestBoatModel(baked) : new BoatModel(baked);
    }

    public static ModelLayerLocation createBoatModelName(CambylictusBoat.Type t) {
        return createLocation("boat/" + t.getSerializedName(), "main");
    }

    public static ModelLayerLocation createChestBoatModelName(CambylictusBoat.Type t) {
        return createLocation("chest_boat/" + t.getSerializedName(), "main");
    }

    private static ModelLayerLocation createLocation(String path, String part) {
        return new ModelLayerLocation(new ResourceLocation(galaxyunderchaos.MODID, path), part);
    }

    @Override
    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
        if (boat instanceof CambylictusBoat modBoat) {
            return boatResources.get(modBoat.getModVariant());
        } else if (boat instanceof CambylictusChestBoat chestBoat) {
            return boatResources.get(chestBoat.getModVariant());
        }
        return null;
    }
}
