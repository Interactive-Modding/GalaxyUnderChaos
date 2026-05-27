package server.galaxyunderchaos.worldgen;

import client.mixin.JigsawStructureAccessor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.function.Function;

/**
 * Normal non-mixin codec builder used by JigsawStructureLimitMixin.
 * Keeping the RecordCodecBuilder lambdas out of the mixin class prevents
 * ModLauncher/Mixin from trying to load the mixin class as a normal class
 * during bootstrap.
 */
public abstract class GalaxyJigsawStructureCodecs extends Structure {
    private static final int MAX_JIGSAW_DEPTH = 64;
    private static final int DEFAULT_DISTANCE_FROM_CENTER = 80;
    private static final int MAX_DISTANCE_FROM_CENTER = 512;

    private GalaxyJigsawStructureCodecs(StructureSettings settings) {
        super(settings);
    }

    public static Codec<JigsawStructure> create() {
        return ExtraCodecs.validate(RecordCodecBuilder.mapCodec(instance -> instance.group(
                settingsCodec(instance),
                StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> access(structure).galaxyunderchaos$getStartPool()),
                ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> access(structure).galaxyunderchaos$getStartJigsawName()),
                Codec.intRange(0, MAX_JIGSAW_DEPTH).fieldOf("size").forGetter(structure -> access(structure).galaxyunderchaos$getMaxDepth()),
                HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> access(structure).galaxyunderchaos$getStartHeight()),
                Codec.BOOL.fieldOf("use_expansion_hack").orElse(false).forGetter(structure -> access(structure).galaxyunderchaos$getUseExpansionHack()),
                Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> access(structure).galaxyunderchaos$getProjectStartToHeightmap()),
                Codec.intRange(1, MAX_DISTANCE_FROM_CENTER).fieldOf("max_distance_from_center").orElse(DEFAULT_DISTANCE_FROM_CENTER).forGetter(structure -> access(structure).galaxyunderchaos$getMaxDistanceFromCenter())
        ).apply(instance, JigsawStructure::new)), verifyRange()).codec();
    }

    private static JigsawStructureAccessor access(JigsawStructure structure) {
        return (JigsawStructureAccessor) (Object) structure;
    }

    private static Function<JigsawStructure, DataResult<JigsawStructure>> verifyRange() {
        return structure -> {
            int maxDistance = access(structure).galaxyunderchaos$getMaxDistanceFromCenter();
            if (maxDistance > MAX_DISTANCE_FROM_CENTER) {
                return DataResult.error(() -> "Structure size including terrain adaptation must not exceed " + MAX_DISTANCE_FROM_CENTER);
            }
            return DataResult.success(structure);
        };
    }
}
