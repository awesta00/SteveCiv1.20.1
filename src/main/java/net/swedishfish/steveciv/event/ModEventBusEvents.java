package net.swedishfish.steveciv.event;


import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.swedishfish.steveciv.SteveCiv;
import net.swedishfish.steveciv.entity.ModEntities;
import net.swedishfish.steveciv.entity.custom.ElytraSteve;
import net.swedishfish.steveciv.entity.custom.EngineerSteve;
import net.swedishfish.steveciv.entity.custom.SteveGuard;


@Mod.EventBusSubscriber(modid = SteveCiv.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)

public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(ModEntities.STEVE_GUARD.get(), SteveGuard.createAttributes().build());
        event.put(ModEntities.ELYTRA_STEVE.get(), ElytraSteve.createAttributes().build());
        event.put(ModEntities.ENGINEER_STEVE.get(), EngineerSteve.createAttributes().build());
    }
}
