package server.galaxyunderchaos.data;

import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import server.galaxyunderchaos.loot.AddItemModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import server.galaxyunderchaos.galaxyunderchaos;
import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, galaxyunderchaos.MODID);
    }

    @Override
    protected void start() {
        this.add("shuura_from_oak_leaves",
                new AddItemModifier(new LootItemCondition[] {
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.OAK_LEAVES).build(),
                        LootItemRandomChanceCondition.randomChance(0.10f).build(),
                        LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity()).build()
                }, galaxyunderchaos.SHUURA.get()));

        this.add("aegis_hilt_from_jungle_temple",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(new ResourceLocation("chests/jungle_temple"))
                                .and(LootItemRandomChanceCondition.randomChance(0.10f)).build()
                }, galaxyunderchaos.AEGIS_HILT.get()));
        this.add("aegis_hilt_from_desert_pyramid",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(new ResourceLocation("chests/desert_pyramid"))
                                .and(LootItemRandomChanceCondition.randomChance(0.10f)).build()
    }, galaxyunderchaos.AEGIS_HILT.get()));

        add("titanium_chromium_from_zombie", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("entities/zombie"))
                        .and(LootItemRandomChanceCondition.randomChance(0.01f)).build() }, // modified by the creeper's own loot table
                galaxyunderchaos.TITANIUM_CHROMIUM_INGOT.get()));

    }











}
