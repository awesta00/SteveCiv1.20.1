package net.swedishfish.steveciv.event;


import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.swedishfish.steveciv.SteveCiv;
import net.swedishfish.steveciv.entity.ModEntities;
import net.swedishfish.steveciv.entity.custom.RhinoEntity;


@Mod.EventBusSubscriber(modid = SteveCiv.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)

public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(ModEntities.RHINO.get(), RhinoEntity.createAttributes().build());
    }
}