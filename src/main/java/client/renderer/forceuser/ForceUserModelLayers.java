package client.renderer.forceuser;

import client.model.forceuser.cerean_female;
import client.model.forceuser.cerean_male;
import client.model.forceuser.chiss_female;
import client.model.forceuser.chiss_male;
import client.model.forceuser.human_male;
import client.model.forceuser.human_old_female;
import client.model.forceuser.human_old_male;
import client.model.forceuser.jedi_robes;
import client.model.forceuser.jedi_robes_alt;
import client.model.forceuser.miraluka_female;
import client.model.forceuser.miraluka_male;
import client.model.forceuser.mirialan_female;
import client.model.forceuser.mirialan_male;
import client.model.forceuser.rodian_female;
import client.model.forceuser.rodian_male;
import client.model.forceuser.sith_female;
import client.model.forceuser.sith_male;
import client.model.forceuser.sith_robes;
import client.model.forceuser.sith_robes_alt;
import client.model.forceuser.togruta_female;
import client.model.forceuser.togruta_male;
import client.model.forceuser.twilek_female;
import client.model.forceuser.twilek_male;
import client.model.forceuser.zabrak_female;
import client.model.forceuser.zabrak_male;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import server.galaxyunderchaos.entity.forceuser.ForceUserEntity;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public final class ForceUserModelLayers {
    private ForceUserModelLayers() {
    }

    public static ModelLayerLocation layer(String id) {
        return new ModelLayerLocation(new ResourceLocation(galaxyunderchaos.MODID, "force_user/" + id), "main");
    }

    public static void register(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(layer("cerean_female"), cerean_female::createBodyLayer);
        event.registerLayerDefinition(layer("cerean_male"), cerean_male::createBodyLayer);
        event.registerLayerDefinition(layer("chiss_female"), chiss_female::createBodyLayer);
        event.registerLayerDefinition(layer("chiss_male"), chiss_male::createBodyLayer);
        event.registerLayerDefinition(layer("human_male"), human_male::createBodyLayer);
        event.registerLayerDefinition(layer("human_old_female"), human_old_female::createBodyLayer);
        event.registerLayerDefinition(layer("human_old_male"), human_old_male::createBodyLayer);
        event.registerLayerDefinition(layer("jedi_robes"), jedi_robes::createBodyLayer);
        event.registerLayerDefinition(layer("jedi_robes_alt"), jedi_robes_alt::createBodyLayer);
        event.registerLayerDefinition(layer("miraluka_female"), miraluka_female::createBodyLayer);
        event.registerLayerDefinition(layer("miraluka_male"), miraluka_male::createBodyLayer);
        event.registerLayerDefinition(layer("mirialan_female"), mirialan_female::createBodyLayer);
        event.registerLayerDefinition(layer("mirialan_male"), mirialan_male::createBodyLayer);
        event.registerLayerDefinition(layer("rodian_female"), rodian_female::createBodyLayer);
        event.registerLayerDefinition(layer("rodian_male"), rodian_male::createBodyLayer);
        event.registerLayerDefinition(layer("sith_female"), sith_female::createBodyLayer);
        event.registerLayerDefinition(layer("sith_male"), sith_male::createBodyLayer);
        event.registerLayerDefinition(layer("sith_robes"), sith_robes::createBodyLayer);
        event.registerLayerDefinition(layer("sith_robes_alt"), sith_robes_alt::createBodyLayer);
        event.registerLayerDefinition(layer("togruta_female"), togruta_female::createBodyLayer);
        event.registerLayerDefinition(layer("togruta_male"), togruta_male::createBodyLayer);
        event.registerLayerDefinition(layer("twilek_female"), twilek_female::createBodyLayer);
        event.registerLayerDefinition(layer("twilek_male"), twilek_male::createBodyLayer);
        event.registerLayerDefinition(layer("zabrak_female"), zabrak_female::createBodyLayer);
        event.registerLayerDefinition(layer("zabrak_male"), zabrak_male::createBodyLayer);
    }

    public static Map<String, Function<ModelPart, EntityModel<ForceUserEntity>>> bodyFactories() {
        Map<String, Function<ModelPart, EntityModel<ForceUserEntity>>> map = new LinkedHashMap<>();
        map.put("cerean_female", root -> new cerean_female<ForceUserEntity>(root));
        map.put("cerean_male", root -> new cerean_male<ForceUserEntity>(root));
        map.put("chiss_female", root -> new chiss_female<ForceUserEntity>(root));
        map.put("chiss_male", root -> new chiss_male<ForceUserEntity>(root));
        map.put("human_male", root -> new human_male<ForceUserEntity>(root));
        map.put("human_old_female", root -> new human_old_female<ForceUserEntity>(root));
        map.put("human_old_male", root -> new human_old_male<ForceUserEntity>(root));
        map.put("miraluka_female", root -> new miraluka_female<ForceUserEntity>(root));
        map.put("miraluka_male", root -> new miraluka_male<ForceUserEntity>(root));
        map.put("mirialan_female", root -> new mirialan_female<ForceUserEntity>(root));
        map.put("mirialan_male", root -> new mirialan_male<ForceUserEntity>(root));
        map.put("rodian_female", root -> new rodian_female<ForceUserEntity>(root));
        map.put("rodian_male", root -> new rodian_male<ForceUserEntity>(root));
        map.put("sith_female", root -> new sith_female<ForceUserEntity>(root));
        map.put("sith_male", root -> new sith_male<ForceUserEntity>(root));
        map.put("togruta_female", root -> new togruta_female<ForceUserEntity>(root));
        map.put("togruta_male", root -> new togruta_male<ForceUserEntity>(root));
        map.put("twilek_female", root -> new twilek_female<ForceUserEntity>(root));
        map.put("twilek_male", root -> new twilek_male<ForceUserEntity>(root));
        map.put("zabrak_female", root -> new zabrak_female<ForceUserEntity>(root));
        map.put("zabrak_male", root -> new zabrak_male<ForceUserEntity>(root));
        return map;
    }

    public static Map<String, Function<ModelPart, EntityModel<ForceUserEntity>>> robeFactories() {
        Map<String, Function<ModelPart, EntityModel<ForceUserEntity>>> map = new LinkedHashMap<>();
        map.put("jedi_robes", root -> new jedi_robes<ForceUserEntity>(root));
        map.put("jedi_robes_alt", root -> new jedi_robes_alt<ForceUserEntity>(root));
        map.put("sith_robes", root -> new sith_robes<ForceUserEntity>(root));
        map.put("sith_robes_alt", root -> new sith_robes_alt<ForceUserEntity>(root));
        return map;
    }

    public static Map<String, EntityModel<ForceUserEntity>> bakeModels(EntityRendererProvider.Context context, Map<String, Function<ModelPart, EntityModel<ForceUserEntity>>> factories) {
        Map<String, EntityModel<ForceUserEntity>> result = new LinkedHashMap<>();
        factories.forEach((id, factory) -> result.put(id, factory.apply(context.bakeLayer(layer(id)))));
        return result;
    }
}
