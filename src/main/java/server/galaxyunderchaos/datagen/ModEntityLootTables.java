package server.galaxyunderchaos.datagen;

import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import server.galaxyunderchaos.galaxyunderchaos;

import java.util.stream.Stream;

public class ModEntityLootTables extends EntityLootSubProvider {
    public ModEntityLootTables() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
        LootPool.Builder silkThreadPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(galaxyunderchaos.SILK_THREAD.get())
                        .apply(count(UniformGenerator.between(0.0f, 2.0f), false))
                        .apply(lootingBonus(UniformGenerator.between(0.0f, 1.0f))));

        LootPool.Builder venomPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                .add(LootItem.lootTableItem(galaxyunderchaos.ACIDIC_VENOM_SAC.get())
                        .apply(count(UniformGenerator.between(-1.0f, 1.0f), false))
                        .apply(lootingBonus(UniformGenerator.between(0.0f, 1.0f))));

        LootPool.Builder chitinPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                .add(LootItem.lootTableItem(galaxyunderchaos.CHITIN_FRAGMENTS.get())
                        .apply(count(UniformGenerator.between(-1.0f, 1.0f), false))
                        .apply(lootingBonus(UniformGenerator.between(0.0f, 1.0f))));

        LootTable.Builder acidSpider = LootTable.lootTable()
                .withPool(silkThreadPool)
                .withPool(venomPool)
                .withPool(chitinPool);

        LootPool.Builder meatPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(galaxyunderchaos.RAW_WINGMAW_MEAT.get())
                        .apply(count(UniformGenerator.between(0.0f, 2.0f), false))
                        .apply(lootingBonus(UniformGenerator.between(0.0f, 1.0f))));

        LootPool.Builder wingmawDrops = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                .add(LootItem.lootTableItem(galaxyunderchaos.WINGMAW_FANG.get())
                        .apply(count(UniformGenerator.between(-1.0f, 1.0f), true)))
                .add(LootItem.lootTableItem(galaxyunderchaos.WINGMAW_HIDE.get())
                        .apply(count(UniformGenerator.between(-1.0f, 1.0f), false)))
                .add(LootItem.lootTableItem(galaxyunderchaos.WINGMAW_FEATHER.get())
                        .apply(count(UniformGenerator.between(-1.0f, 1.0f), false))
                        .apply(lootingBonus(UniformGenerator.between(0.0f, 1.0f))));

        LootTable.Builder wingmaw = LootTable.lootTable()
                .withPool(meatPool)
                .withPool(wingmawDrops);

        add(galaxyunderchaos.ACID_SPIDER.get(), acidSpider);
        add(galaxyunderchaos.WINGMAW.get(), wingmaw);
    }

    private LootItemConditionalFunction.Builder<?> count(NumberProvider range, boolean add) {
        return SetItemCountFunction.setCount(range, add);
    }

    private LootItemConditionalFunction.Builder<?> lootingBonus(NumberProvider range) {
        return LootingEnchantFunction.lootingMultiplier(range);
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return Stream.of(galaxyunderchaos.ACID_SPIDER.get(), galaxyunderchaos.WINGMAW.get());
    }
}
