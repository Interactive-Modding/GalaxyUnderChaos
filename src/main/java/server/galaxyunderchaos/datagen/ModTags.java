package server.galaxyunderchaos.datagen;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import server.galaxyunderchaos.galaxyunderchaos;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> NEEDS_LIGHTSABER_TOOL = createTag("needs_lightsaber_tool");
        public static final TagKey<Block> INCORRECT_FOR_LIGHTSABER_TOOL = createTag("incorrect_for_lightsaber_tool");
        public static final TagKey<Block> NEEDS_DAGGER_TOOL = createTag("needs_dagger_tool");
        public static final TagKey<Block> INCORRECT_FOR_DAGGER_TOOL = createTag("incorrect_for_dagger_tool");
        private static TagKey<Block> createTag(String name) {

            return BlockTags.create(new ResourceLocation(galaxyunderchaos.MODID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> BLEEDABLE_ITEMS = createTag("bleedable_items");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(new ResourceLocation(galaxyunderchaos.MODID, name));
        }
    }
}