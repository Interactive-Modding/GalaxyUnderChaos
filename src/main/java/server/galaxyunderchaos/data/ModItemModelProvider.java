package server.galaxyunderchaos.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import server.galaxyunderchaos.galaxyunderchaos;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, galaxyunderchaos.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(galaxyunderchaos.AMBER_KYBER);
        simpleItem(galaxyunderchaos.GOLD_KYBER);
        simpleItem(galaxyunderchaos.LIGHT_BLUE_KYBER);
        simpleItem(galaxyunderchaos.DARK_BLUE_KYBER);
        simpleItem(galaxyunderchaos.MAROON_KYBER);
        simpleItem(galaxyunderchaos.DEEP_VIOLET_KYBER);
        simpleItem(galaxyunderchaos.ARCTIC_BLUE_KYBER);
        simpleItem(galaxyunderchaos.ROSE_PINK_KYBER);



    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), "item/handheld")
                .texture("layer0", new ResourceLocation(galaxyunderchaos.MODID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), "item/generated")
                .texture("layer0", new ResourceLocation(galaxyunderchaos.MODID, "item/" + item.getId().getPath()));
    }

    public void buttonItem(RegistryObject<? extends Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/button_inventory"))
                .texture("texture", new ResourceLocation(galaxyunderchaos.MODID,
                        "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void fenceItem(RegistryObject<? extends Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory"))
                .texture("texture", new ResourceLocation(galaxyunderchaos.MODID,
                        "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void wallItem(RegistryObject<? extends Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall", new ResourceLocation(galaxyunderchaos.MODID,
                        "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<? extends Block> item) {
        return withExistingParent(item.getId().getPath(), "item/generated")
                .texture("layer0", new ResourceLocation(galaxyunderchaos.MODID, "item/" + item.getId().getPath()));
    }

    private void blockItem(RegistryObject<? extends Block> block) {
        withExistingParent(block.getId().getPath(), modLoc("block/" + block.getId().getPath()));
    }
}