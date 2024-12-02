package net.swedishfish.steveciv.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.swedishfish.steveciv.SteveCiv;
import net.swedishfish.steveciv.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {


    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SteveCiv.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(ModItems.GUARD_STEVE_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.ELYTRA_STEVE_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.ENGINEER_STEVE_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        simpleItem(ModItems.SOGNO_DI_VOLARE_MUSIC_DISC);

    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(SteveCiv.MOD_ID,"item/" + item.getId().getPath()));
    }

}

