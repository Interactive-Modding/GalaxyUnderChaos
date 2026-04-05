package server.galaxyunderchaos.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import server.galaxyunderchaos.block.TreeHangingSign;
import server.galaxyunderchaos.block.TreeStandingSign;
import server.galaxyunderchaos.block.TreeWallHangingSign;
import server.galaxyunderchaos.block.TreeWallSign;

import server.galaxyunderchaos.galaxyunderchaos;    // your main modclass

/** Holds every BlockEntityType for GalaxyUnderChaos. */
public final class ModBlockEntities {

    /** Master deferred‑register for BE types. */
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES,
                    galaxyunderchaos.MODID);

    /* ───── Ak standing / wall sign ───── */
    public static final RegistryObject<BlockEntityType<ModSignBlockEntity>> AK_SIGN_BE =
            BLOCK_ENTITIES.register("ak_sign",
                    () -> BlockEntityType.Builder.of(ModSignBlockEntity::new,
                                    galaxyunderchaos.AK_SIGN.get(),
                                    galaxyunderchaos.AK_WALL_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModHangingSignBlockEntity>> AK_HANGING_SIGN_BE =
            BLOCK_ENTITIES.register("ak_hanging_sign",
                    () -> BlockEntityType.Builder.of(ModHangingSignBlockEntity::new,
                                    galaxyunderchaos.AK_HANGING_SIGN.get(),
                                    galaxyunderchaos.AK_WALL_HANGING_SIGN.get())
                            .build(null));
    public static final RegistryObject<BlockEntityType<ModSignBlockEntityHB>> HEART_BERRY_SIGN_BE =
            BLOCK_ENTITIES.register("heart_berry_sign",
                    () -> BlockEntityType.Builder.of(ModSignBlockEntityHB::new,
                                    galaxyunderchaos.HEART_BERRY_SIGN.get(),
                                    galaxyunderchaos.HEART_BERRY_WALL_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModHangingSignBlockEntityHB>> HEART_BERRY_HANGING_SIGN_BE =
            BLOCK_ENTITIES.register("heart_berry_hanging_sign",
                    () -> BlockEntityType.Builder.of(ModHangingSignBlockEntityHB::new,
                                    galaxyunderchaos.HEART_BERRY_HANGING_SIGN.get(),
                                    galaxyunderchaos.HEART_BERRY_WALL_HANGING_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<CoffinBlockEntity>> COFFIN_BE =
            BLOCK_ENTITIES.register("coffin",
                    () -> BlockEntityType.Builder.of(CoffinBlockEntity::new,
                                    galaxyunderchaos.JEDI_COFFIN.get(),
                                    galaxyunderchaos.SITH_COFFIN.get())
                            .build(null));
    public static final RegistryObject<BlockEntityType<GroundSaberStandBlockEntity>>
            SABER_STAND_BE =
            BLOCK_ENTITIES.register("saber_stand",
                    () -> BlockEntityType.Builder.of(
                            GroundSaberStandBlockEntity::new,
                            galaxyunderchaos.GROUND_SABER_STAND.get(),
                            galaxyunderchaos.WHITE_GROUND_SABER_STAND.get()
                    ).build(null));
    public static final RegistryObject<BlockEntityType<BleedingTableBlockEntity>> BLEEDING_TABLE_BE =
            BLOCK_ENTITIES.register("bleeding_table",
                    () -> BlockEntityType.Builder.of(
                            BleedingTableBlockEntity::new,
                            galaxyunderchaos.BLEEDING_TABLE.get()
                    ).build(null));
    private ModBlockEntities() {}   // no instantiation
}
