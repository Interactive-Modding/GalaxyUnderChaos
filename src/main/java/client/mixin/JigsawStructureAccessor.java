package client.mixin;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(JigsawStructure.class)
public interface JigsawStructureAccessor {
    @Accessor("startPool")
    Holder<StructureTemplatePool> galaxyunderchaos$getStartPool();

    @Accessor("startJigsawName")
    Optional<ResourceLocation> galaxyunderchaos$getStartJigsawName();

    @Accessor("maxDepth")
    int galaxyunderchaos$getMaxDepth();

    @Accessor("startHeight")
    HeightProvider galaxyunderchaos$getStartHeight();

    @Accessor("useExpansionHack")
    boolean galaxyunderchaos$getUseExpansionHack();

    @Accessor("projectStartToHeightmap")
    Optional<Heightmap.Types> galaxyunderchaos$getProjectStartToHeightmap();

    @Accessor("maxDistanceFromCenter")
    int galaxyunderchaos$getMaxDistanceFromCenter();
}
