package server.galaxyunderchaos.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import server.galaxyunderchaos.galaxyunderchaos;

import static server.galaxyunderchaos.galaxyunderchaos.*;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, galaxyunderchaos.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);


    public static final RegistryObject<EntityType<AkBoat>> AK_BOAT =
            ENTITY_TYPES.register("ak_boat", () -> EntityType.Builder.<AkBoat>of(AkBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("ak_boat"));
    public static final RegistryObject<EntityType<AkChestBoat>> AK_CHEST_BOAT =
            ENTITY_TYPES.register("ak_chest_boat", () -> EntityType.Builder.<AkChestBoat>of(AkChestBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("ak_chest_boat"));

    public static final RegistryObject<EntityType<DilliaBoat>> DILLIA_BOAT =
            ENTITY_TYPES.register("dillia_boat", () -> EntityType.Builder.<DilliaBoat>of(DilliaBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("dillia_boat"));
    public static final RegistryObject<EntityType<DilliaChestBoat>> DILLIA_CHEST_BOAT =
            ENTITY_TYPES.register("dillia_chest_boat", () -> EntityType.Builder.<DilliaChestBoat>of(DilliaChestBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("dillia_chest_boat"));
    public static final RegistryObject<EntityType<NabooPineBoat>> NABOO_PINE_BOAT =
            ENTITY_TYPES.register("naboo_pine_boat", () -> EntityType.Builder.<NabooPineBoat>of(NabooPineBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("naboo_pine_boat"));
    public static final RegistryObject<EntityType<NabooPineChestBoat>> NABOO_PINE_CHEST_BOAT =
            ENTITY_TYPES.register("naboo_pine_chest_boat", () -> EntityType.Builder.<NabooPineChestBoat>of(NabooPineChestBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("naboo_pine_chest_boat"));




    public static final RegistryObject<EntityType<BlbaBoat>> BLBA_BOAT =
            ENTITY_TYPES.register("blba_boat", () -> EntityType.Builder.<BlbaBoat>of(BlbaBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("blba_boat"));
    public static final RegistryObject<EntityType<BlbaChestBoat>> BLBA_CHEST_BOAT =
            ENTITY_TYPES.register("blba_chest_boat", () -> EntityType.Builder.<BlbaChestBoat>of(BlbaChestBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("blba_chest_boat"));

    public static final RegistryObject<EntityType<CambylictusBoat>> CAMBYLICTUS_BOAT =
            ENTITY_TYPES.register("cambylictus_boat", () -> EntityType.Builder.<CambylictusBoat>of(CambylictusBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("cambylictus_boat"));
    public static final RegistryObject<EntityType<CambylictusChestBoat>> CAMBYLICTUS_CHEST_BOAT =
            ENTITY_TYPES.register("cambylictus_chest_boat", () -> EntityType.Builder.<CambylictusChestBoat>of(CambylictusChestBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("cambylictus_chest_boat"));

    public static final RegistryObject<EntityType<PerloteBoat>> PERLOTE_BOAT =
            ENTITY_TYPES.register("perlote_boat", () -> EntityType.Builder.<PerloteBoat>of(PerloteBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("perlote_boat"));
    public static final RegistryObject<EntityType<PerloteChestBoat>> PERLOTE_CHEST_BOAT =
            ENTITY_TYPES.register("perlote_chest_boat", () -> EntityType.Builder.<PerloteChestBoat>of(PerloteChestBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("perlote_chest_boat"));

    public static final RegistryObject<EntityType<RutigerBoat>> RUTIGER_BOAT =
            ENTITY_TYPES.register("rutiger_boat", () -> EntityType.Builder.<RutigerBoat>of(RutigerBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("rutiger_boat"));
    public static final RegistryObject<EntityType<RutigerChestBoat>> RUTIGER_CHEST_BOAT =
            ENTITY_TYPES.register("rutiger_chest_boat", () -> EntityType.Builder.<RutigerChestBoat>of(RutigerChestBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("rutiger_chest_boat"));

    public static final RegistryObject<EntityType<PolarBoat>> POLAR_BOAT =
            ENTITY_TYPES.register("polar_boat", () -> EntityType.Builder.<PolarBoat>of(PolarBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("polar_boat"));
    public static final RegistryObject<EntityType<PolarChestBoat>> POLAR_CHEST_BOAT =
            ENTITY_TYPES.register("polar_chest_boat", () -> EntityType.Builder.<PolarChestBoat>of(PolarChestBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("polar_chest_boat"));


    public static final RegistryObject<BlockEntityType<ModSignBlockEntity>> AK_SIGN_BE =
            BLOCK_ENTITIES.register("ak_sign",
                    () -> BlockEntityType.Builder.of(
                                    ModSignBlockEntity::new,
                                    AK_SIGN.get(), AK_WALL_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModHangingSignBlockEntity>> AK_HANGING_SIGN_BE =
            BLOCK_ENTITIES.register("ak_hanging_sign",
                    () -> BlockEntityType.Builder.of(
                                    ModHangingSignBlockEntity::new,
                                    AK_HANGING_SIGN.get(), AK_WALL_HANGING_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModSignBlockEntityDillia>> DILLIA_SIGN_BE =
            BLOCK_ENTITIES.register("dillia_sign",
                    () -> BlockEntityType.Builder.of(
                                    ModSignBlockEntityDillia::new,
                                    DILLIA_SIGN.get(), DILLIA_WALL_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModHangingSignBlockEntityDillia>> DILLIA_HANGING_SIGN_BE =
            BLOCK_ENTITIES.register("dillia_hanging_sign",
                    () -> BlockEntityType.Builder.of(
                                    ModHangingSignBlockEntityDillia::new,
                                    DILLIA_HANGING_SIGN.get(), DILLIA_WALL_HANGING_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModSignBlockEntityNabooPine>> NABOO_PINE_SIGN_BE =
            BLOCK_ENTITIES.register("naboo_pine_sign",
                    () -> BlockEntityType.Builder.of(
                                    ModSignBlockEntityNabooPine::new,
                                    NABOO_PINE_SIGN.get(), NABOO_PINE_WALL_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModHangingSignBlockEntityNabooPine>> NABOO_PINE_HANGING_SIGN_BE =
            BLOCK_ENTITIES.register("naboo_pine_hanging_sign",
                    () -> BlockEntityType.Builder.of(
                                    ModHangingSignBlockEntityNabooPine::new,
                                    NABOO_PINE_HANGING_SIGN.get(), NABOO_PINE_WALL_HANGING_SIGN.get())
                            .build(null));

    public static final RegistryObject<EntityType<HeartBerryBoat>> HEART_BERRY_BOAT =
            ENTITY_TYPES.register("heart_berry_boat", () -> EntityType.Builder.<HeartBerryBoat>of(HeartBerryBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("heart_berry_boat"));
    public static final RegistryObject<EntityType<HeartBerryChestBoat>> HEART_BERRY_CHEST_BOAT =
            ENTITY_TYPES.register("heart_berry_chest_boat", () -> EntityType.Builder.<HeartBerryChestBoat>of(HeartBerryChestBoat::new, MobCategory.MISC)
                    .sized(1.375f, 0.5625f).build("heart_berry_chest_boat"));



    public static final RegistryObject<BlockEntityType<ModSignBlockEntityHB>> HEART_BERRY_SIGN_BE =
            BLOCK_ENTITIES.register("heart_berry_sign",
                    () -> BlockEntityType.Builder.of(
                                    ModSignBlockEntityHB::new,
                                    HEART_BERRY_SIGN.get(), HEART_BERRY_WALL_SIGN.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ModHangingSignBlockEntityHB>> HEART_BERRY_HANGING_SIGN_BE =
            BLOCK_ENTITIES.register("heart_berry_hanging_sign",
                    () -> BlockEntityType.Builder.of(
                                    ModHangingSignBlockEntityHB::new,
                                    HEART_BERRY_HANGING_SIGN.get(), HEART_BERRY_WALL_HANGING_SIGN.get())
                            .build(null));

}
