package net.swedishfish.steveciv.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.swedishfish.steveciv.SteveCiv;

import net.swedishfish.steveciv.entity.ModEntities;
import net.swedishfish.steveciv.sound.ModSounds;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SteveCiv.MOD_ID);


    public static final RegistryObject<Item> GUARD_STEVE_SPAWN_EGG = ITEMS.register("guard_steve_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.STEVE_GUARD, 0xb16b05, 0x89cff0,
                    new Item.Properties()));

    public static final RegistryObject<Item> ELYTRA_STEVE_SPAWN_EGG = ITEMS.register("elytra_steve_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.ELYTRA_STEVE, 0xb16b05, 0x89cff0,
                    new Item.Properties()));

    public static final RegistryObject<Item> ENGINEER_STEVE_SPAWN_EGG = ITEMS.register("engineer_steve_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.ENGINEER_STEVE, 0xb16b05, 0x89cff0,
                    new Item.Properties()));


        public static final RegistryObject<Item> SOGNO_DI_VOLARE_MUSIC_DISC = ITEMS.register("sogno_di_volare_music_disc",
                () -> new RecordItem(10,
                        ModSounds.SOGNO_DI_VOLARE,
                        new Item.Properties().stacksTo(1).rarity(Rarity.RARE),
                        4560));



    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
