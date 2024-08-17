package net.swedishfish.steveciv.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.swedishfish.steveciv.SteveCiv;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SteveCiv.MOD_ID);

    public static final RegistryObject<CreativeModeTab> STEVECIV_TAB = CREATIVE_MODE_TABS.register("steveciv_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(Items.PLAYER_HEAD))
                    .title(Component.translatable("creativetab.steveciv_tab"))
                    .displayItems((parameters, output) -> {

                        output.accept(ModItems.RHINO_SPAWN_EGG.get());
                        output.accept(ModItems.GUARD_STEVE_SPAWN_EGG.get());
                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}
