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


    public static final RegistryObject<BlockEntityType<ModSignBlockEntityBlba>> BLBA_SIGN_BE =
            BLOCK_ENTITIES.register("blba_sign",
                    () -> BlockEntityType.Builder.of(ModSignBlockEntityBlba::new,
                                    galaxyunderchaos.BLBA_SIGN.get(),
                                    galaxyunderchaos.BLBA_WALL_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModHangingSignBlockEntityBlba>> BLBA_HANGING_SIGN_BE =
            BLOCK_ENTITIES.register("blba_hanging_sign",
                    () -> BlockEntityType.Builder.of(ModHangingSignBlockEntityBlba::new,
                                    galaxyunderchaos.BLBA_HANGING_SIGN.get(),
                                    galaxyunderchaos.BLBA_WALL_HANGING_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModSignBlockEntityCambylictus>> CAMBYLICTUS_SIGN_BE =
            BLOCK_ENTITIES.register("cambylictus_sign",
                    () -> BlockEntityType.Builder.of(ModSignBlockEntityCambylictus::new,
                                    galaxyunderchaos.CAMBYLICTUS_SIGN.get(),
                                    galaxyunderchaos.CAMBYLICTUS_WALL_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModHangingSignBlockEntityCambylictus>> CAMBYLICTUS_HANGING_SIGN_BE =
            BLOCK_ENTITIES.register("cambylictus_hanging_sign",
                    () -> BlockEntityType.Builder.of(ModHangingSignBlockEntityCambylictus::new,
                                    galaxyunderchaos.CAMBYLICTUS_HANGING_SIGN.get(),
                                    galaxyunderchaos.CAMBYLICTUS_WALL_HANGING_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModSignBlockEntityPerlote>> PERLOTE_SIGN_BE =
            BLOCK_ENTITIES.register("perlote_sign",
                    () -> BlockEntityType.Builder.of(ModSignBlockEntityPerlote::new,
                                    galaxyunderchaos.PERLOTE_SIGN.get(),
                                    galaxyunderchaos.PERLOTE_WALL_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModHangingSignBlockEntityPerlote>> PERLOTE_HANGING_SIGN_BE =
            BLOCK_ENTITIES.register("perlote_hanging_sign",
                    () -> BlockEntityType.Builder.of(ModHangingSignBlockEntityPerlote::new,
                                    galaxyunderchaos.PERLOTE_HANGING_SIGN.get(),
                                    galaxyunderchaos.PERLOTE_WALL_HANGING_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModSignBlockEntityRutiger>> RUTIGER_SIGN_BE =
            BLOCK_ENTITIES.register("rutiger_sign",
                    () -> BlockEntityType.Builder.of(ModSignBlockEntityRutiger::new,
                                    galaxyunderchaos.RUTIGER_SIGN.get(),
                                    galaxyunderchaos.RUTIGER_WALL_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModHangingSignBlockEntityRutiger>> RUTIGER_HANGING_SIGN_BE =
            BLOCK_ENTITIES.register("rutiger_hanging_sign",
                    () -> BlockEntityType.Builder.of(ModHangingSignBlockEntityRutiger::new,
                                    galaxyunderchaos.RUTIGER_HANGING_SIGN.get(),
                                    galaxyunderchaos.RUTIGER_WALL_HANGING_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModSignBlockEntityPolar>> POLAR_SIGN_BE =
            BLOCK_ENTITIES.register("polar_sign",
                    () -> BlockEntityType.Builder.of(ModSignBlockEntityPolar::new,
                                    galaxyunderchaos.POLAR_SIGN.get(),
                                    galaxyunderchaos.POLAR_WALL_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModHangingSignBlockEntityPolar>> POLAR_HANGING_SIGN_BE =
            BLOCK_ENTITIES.register("polar_hanging_sign",
                    () -> BlockEntityType.Builder.of(ModHangingSignBlockEntityPolar::new,
                                    galaxyunderchaos.POLAR_HANGING_SIGN.get(),
                                    galaxyunderchaos.POLAR_WALL_HANGING_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModSignBlockEntityDillia>> DILLIA_SIGN_BE =
            BLOCK_ENTITIES.register("dillia_sign",
                    () -> BlockEntityType.Builder.of(ModSignBlockEntityDillia::new,
                                    galaxyunderchaos.DILLIA_SIGN.get(),
                                    galaxyunderchaos.DILLIA_WALL_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModHangingSignBlockEntityDillia>> DILLIA_HANGING_SIGN_BE =
            BLOCK_ENTITIES.register("dillia_hanging_sign",
                    () -> BlockEntityType.Builder.of(ModHangingSignBlockEntityDillia::new,
                                    galaxyunderchaos.DILLIA_HANGING_SIGN.get(),
                                    galaxyunderchaos.DILLIA_WALL_HANGING_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModSignBlockEntityNabooPine>> NABOO_PINE_SIGN_BE =
            BLOCK_ENTITIES.register("naboo_pine_sign",
                    () -> BlockEntityType.Builder.of(ModSignBlockEntityNabooPine::new,
                                    galaxyunderchaos.NABOO_PINE_SIGN.get(),
                                    galaxyunderchaos.NABOO_PINE_WALL_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModHangingSignBlockEntityNabooPine>> NABOO_PINE_HANGING_SIGN_BE =
            BLOCK_ENTITIES.register("naboo_pine_hanging_sign",
                    () -> BlockEntityType.Builder.of(ModHangingSignBlockEntityNabooPine::new,
                                    galaxyunderchaos.NABOO_PINE_HANGING_SIGN.get(),
                                    galaxyunderchaos.NABOO_PINE_WALL_HANGING_SIGN.get())
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
                                    galaxyunderchaos.SITH_LORD_COFFIN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<SithTombBlockEntity>> SITH_TOMB_BE =
            BLOCK_ENTITIES.register("sith_tomb",
                    () -> BlockEntityType.Builder.of(SithTombBlockEntity::new,
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
    public static final RegistryObject<BlockEntityType<LightsaberCraftingTableBlockEntity>> LIGHTSABER_CRAFTING_TABLE_BE =
            BLOCK_ENTITIES.register("lightsaber_crafting_table",
                    () -> BlockEntityType.Builder.of(
                            LightsaberCraftingTableBlockEntity::new,
                            galaxyunderchaos.LIGHTSABER_CRAFTING_TABLE.get()
                    ).build(null));
    public static final RegistryObject<BlockEntityType<ShipCraftingTableBlockEntity>> SHIP_CRAFTING_TABLE_BE =
            BLOCK_ENTITIES.register("ship_crafting_table",
                    () -> BlockEntityType.Builder.of(
                            ShipCraftingTableBlockEntity::new,
                            galaxyunderchaos.SHIP_CRAFTING_TABLE.get()
                    ).build(null));
    private ModBlockEntities() {}   // no instantiation
}
