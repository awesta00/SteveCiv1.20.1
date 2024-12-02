package net.swedishfish.steveciv.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.swedishfish.steveciv.SteveCiv;
import net.swedishfish.steveciv.entity.custom.ElytraSteve;
import net.swedishfish.steveciv.entity.custom.EngineerSteve;
import net.swedishfish.steveciv.entity.custom.SteveGuard;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SteveCiv.MOD_ID);


    public static final RegistryObject<EntityType<SteveGuard>> STEVE_GUARD =
            ENTITY_TYPES.register("steve_warrior", () -> EntityType.Builder.of(SteveGuard::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.8F).build("steve_warrior"));

    public static final RegistryObject<EntityType<ElytraSteve>> ELYTRA_STEVE =
            ENTITY_TYPES.register("elytra_steve", () -> EntityType.Builder.of(ElytraSteve::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.8F).build("elytra_steve"));

    public static final RegistryObject<EntityType<EngineerSteve>> ENGINEER_STEVE =
            ENTITY_TYPES.register("engineer_steve", () -> EntityType.Builder.of(EngineerSteve::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.8F).build("engineer_steve"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
