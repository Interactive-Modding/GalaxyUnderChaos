package server.galaxyunderchaos.worldgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.Structure.StructureSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.block.Rotation;

import java.util.Optional;

public class LargeSingleTemplateStructure extends Structure {
    public static final Codec<LargeSingleTemplateStructure> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    settingsCodec(instance),
                    ResourceLocation.CODEC.fieldOf("template").forGetter(structure -> structure.template),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
                    Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap),
                    Codec.BOOL.optionalFieldOf("center_template", true).forGetter(structure -> structure.centerTemplate),
                    Codec.INT.optionalFieldOf("y_offset", 0).forGetter(structure -> structure.yOffset),
                    Codec.BOOL.optionalFieldOf("random_rotation", false).forGetter(structure -> structure.randomRotation)
            ).apply(instance, LargeSingleTemplateStructure::new)
    );

    private final ResourceLocation template;
    private final HeightProvider startHeight;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final boolean centerTemplate;
    private final int yOffset;
    private final boolean randomRotation;

    public LargeSingleTemplateStructure(StructureSettings settings,
                                        ResourceLocation template,
                                        HeightProvider startHeight,
                                        Optional<Heightmap.Types> projectStartToHeightmap,
                                        boolean centerTemplate,
                                        int yOffset,
                                        boolean randomRotation) {
        super(settings);
        this.template = template;
        this.startHeight = startHeight;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.centerTemplate = centerTemplate;
        this.yOffset = yOffset;
        this.randomRotation = randomRotation;
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        RandomSource random = context.random();
        Rotation rotation = this.randomRotation ? Rotation.getRandom(random) : Rotation.NONE;

        StructureTemplateManager templateManager = context.structureTemplateManager();
        StructureTemplate structureTemplate = templateManager.getOrCreate(this.template);
        int templateX = structureTemplate.getSize().getX();
        int templateZ = structureTemplate.getSize().getZ();
        if (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90) {
            int swap = templateX;
            templateX = templateZ;
            templateZ = swap;
        }

        int x = chunkPos.x * 16 + 8;
        int z = chunkPos.z * 16 + 8;
        if (this.centerTemplate) {
            x -= templateX / 2;
            z -= templateZ / 2;
        }

        WorldGenerationContext generationContext = new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor());
        int y = this.startHeight.sample(random, generationContext);
        if (this.projectStartToHeightmap.isPresent()) {
            int sampleX = x + Math.max(1, templateX / 2);
            int sampleZ = z + Math.max(1, templateZ / 2);
            y = context.chunkGenerator().getFirstOccupiedHeight(sampleX, sampleZ,
                    this.projectStartToHeightmap.get(), context.heightAccessor(), context.randomState());
        }
        y += this.yOffset;

        BlockPos origin = new BlockPos(x, y, z);
        return Optional.of(new GenerationStub(origin, builder ->
                builder.addPiece(new LargeSingleTemplatePiece(templateManager, this.template, origin, rotation))));
    }

    @Override
    public StructureType<?> type() {
        return ModStructureTypes.LARGE_SINGLE_TEMPLATE.get();
    }
}
