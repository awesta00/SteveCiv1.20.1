package net.swedishfish.steveciv.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.swedishfish.steveciv.SteveCiv;

//public class ModSounds {
//    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
//            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SteveCiv.MOD_ID);
//
//
////    public static final RegistryObject<SoundEvent> SOGNO_DI_VOLARE = registerSoundEvents("sogno_di_volare");
//
//    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
//        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SteveCiv.MOD_ID, name)));
//
//    }
//
//
//    public static void register(IEventBus eventBus) {
//        SOUND_EVENTS.register(eventBus);
//    }
//}

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SteveCiv.MOD_ID);

    // Register the sound event
    public static final RegistryObject<SoundEvent> SOGNO_DI_VOLARE = SOUND_EVENTS.register("sogno_di_volare",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(SteveCiv.MOD_ID, "sogno_di_volare")));

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
