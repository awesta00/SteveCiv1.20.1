package net.swedishfish.steveciv.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.swedishfish.steveciv.SteveCiv;
import net.swedishfish.steveciv.entity.custom.RhinoEntity;
import net.swedishfish.steveciv.entity.custom.SteveGuard;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SteveCiv.MOD_ID);

    public static final RegistryObject<EntityType<RhinoEntity>> RHINO =
            ENTITY_TYPES.register("rhino", () -> EntityType.Builder.of(RhinoEntity::new, MobCategory.CREATURE)
                    .sized(2.5f,2.5f).build("rhino")); //the numbers are the hit box size


    public static final RegistryObject<EntityType<SteveGuard>> STEVE_GUARD =
            ENTITY_TYPES.register("steve_guard", () -> EntityType.Builder.of(SteveGuard::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.8F).build("steveguard"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
