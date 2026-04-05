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
