package server.galaxyunderchaos.worldgen.structure;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import server.galaxyunderchaos.galaxyunderchaos;

public final class ModStructureTypes {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, galaxyunderchaos.MODID);

    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_PIECE, galaxyunderchaos.MODID);

    public static final RegistryObject<StructureType<LargeSingleTemplateStructure>> LARGE_SINGLE_TEMPLATE =
            STRUCTURE_TYPES.register("large_single_template", () -> () -> LargeSingleTemplateStructure.CODEC);

    public static final RegistryObject<StructurePieceType> LARGE_SINGLE_TEMPLATE_PIECE =
            STRUCTURE_PIECE_TYPES.register("large_single_template_piece", () -> LargeSingleTemplatePiece::new);

    private ModStructureTypes() {
    }

    public static void register(IEventBus bus) {
        STRUCTURE_TYPES.register(bus);
        STRUCTURE_PIECE_TYPES.register(bus);
    }
}
