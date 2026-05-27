package server.galaxyunderchaos.worldgen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class LargeSingleTemplatePiece extends TemplateStructurePiece {
    private static final String TEMPLATE_TAG = "Template";
    private static final String ROTATION_TAG = "Rot";

    private final ResourceLocation template;
    private final Rotation rotation;

    public LargeSingleTemplatePiece(StructureTemplateManager templateManager, ResourceLocation template, BlockPos origin, Rotation rotation) {
        super(ModStructureTypes.LARGE_SINGLE_TEMPLATE_PIECE.get(), 0, templateManager,
                template, template.toString(), makeSettings(rotation), origin);
        this.template = template;
        this.rotation = rotation;
    }

    public LargeSingleTemplatePiece(StructurePieceSerializationContext context, CompoundTag tag) {
        super(ModStructureTypes.LARGE_SINGLE_TEMPLATE_PIECE.get(), tag, context.structureTemplateManager(),
                location -> makeSettings(readRotation(tag)));
        this.template = new ResourceLocation(tag.getString(TEMPLATE_TAG));
        this.rotation = readRotation(tag);
    }

    private static StructurePlaceSettings makeSettings(Rotation rotation) {
        return new StructurePlaceSettings()
                .setMirror(Mirror.NONE)
                .setRotation(rotation)
                .setIgnoreEntities(false)
                .setFinalizeEntities(true);
    }

    private static Rotation readRotation(CompoundTag tag) {
        if (!tag.contains(ROTATION_TAG)) {
            return Rotation.NONE;
        }
        try {
            return Rotation.valueOf(tag.getString(ROTATION_TAG));
        } catch (IllegalArgumentException ignored) {
            return Rotation.NONE;
        }
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        super.addAdditionalSaveData(context, tag);
        tag.putString(TEMPLATE_TAG, this.template.toString());
        tag.putString(ROTATION_TAG, this.rotation.name());
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox box) {
    }
}
