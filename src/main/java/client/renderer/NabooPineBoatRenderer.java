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
import server.galaxyunderchaos.entity.NabooPineBoat;
import server.galaxyunderchaos.entity.NabooPineChestBoat;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.Map;
import java.util.stream.Stream;

public class NabooPineBoatRenderer extends BoatRenderer {
    private final Map<NabooPineBoat.Type, Pair<ResourceLocation, ListModel<Boat>>> boatResources;

    public NabooPineBoatRenderer(EntityRendererProvider.Context ctx, boolean chestBoat) {
        super(ctx, chestBoat);
        this.boatResources = Stream.of(NabooPineBoat.Type.values()).collect(
                ImmutableMap.toImmutableMap(
                        type -> type,
                        type -> Pair.of(new ResourceLocation(galaxyunderchaos.MODID, texturePath(type, chestBoat)),
                                createBoatModel(ctx, type, chestBoat))));
    }

    private static String texturePath(NabooPineBoat.Type type, boolean chest) {
        return (chest ? "textures/entity/chest_boat/" : "textures/entity/boat/")
                + type.getSerializedName() + ".png";
    }

    private BoatModel createBoatModel(EntityRendererProvider.Context ctx, NabooPineBoat.Type type, boolean chest) {
        ModelLayerLocation layer = chest ? createChestBoatModelName(type) : createBoatModelName(type);
        ModelPart baked = ctx.bakeLayer(layer);
        return chest ? new ChestBoatModel(baked) : new BoatModel(baked);
    }

    public static ModelLayerLocation createBoatModelName(NabooPineBoat.Type type) {
        return createLocation("boat/" + type.getSerializedName(), "main");
    }

    public static ModelLayerLocation createChestBoatModelName(NabooPineBoat.Type type) {
        return createLocation("chest_boat/" + type.getSerializedName(), "main");
    }

    private static ModelLayerLocation createLocation(String path, String part) {
        return new ModelLayerLocation(new ResourceLocation(galaxyunderchaos.MODID, path), part);
    }

    @Override
    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
        if (boat instanceof NabooPineBoat pineBoat) {
            return boatResources.get(pineBoat.getModVariant());
        } else if (boat instanceof NabooPineChestBoat chestBoat) {
            return boatResources.get(chestBoat.getModVariant());
        }
        return null;
    }
}
