package net.swedishfish.steveciv.event;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.TickEvent;
import net.swedishfish.steveciv.entity.ModEntities;
import net.swedishfish.steveciv.entity.custom.EngineerSteve;
import net.swedishfish.steveciv.entity.custom.SteveGuard;


import java.util.List;
import java.util.Random;


@Mod.EventBusSubscriber(modid = "steveciv")
public class PlayerSleepEventHandler {

    private static boolean outpostSpawnedThisNight = false;
    private static boolean armySpawned = false;


    @SubscribeEvent
    public static void onPlayerSleep(PlayerSleepInBedEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        ServerLevel world = (ServerLevel) player.level();

        AABB boundingBox = new AABB(player.blockPosition()).inflate(50);
        List<ArmorStand> armorStands = world.getEntitiesOfClass(ArmorStand.class, boundingBox, armorStand -> armorStand.getTags().contains("EncampmentBanner"));

        if (!armorStands.isEmpty() && !armySpawned) {
            if (new Random().nextFloat() < 0.5) {
                //spawn 10 steve warriors
                ArmorStand bannerStand = armorStands.get(0);
                BlockPos spawnPos = bannerStand.blockPosition();

                for (int i = 0; i < 10; i++) {
                    SteveGuard steveWarrior = new SteveGuard(ModEntities.STEVE_GUARD.get(), world);
                    steveWarrior.moveTo(spawnPos.getX(), spawnPos.getY() + 3, spawnPos.getZ(), 0, 0);
                    world.addFreshEntity(steveWarrior);
                }
                for (int j = 0; j < 2; j++){
                    EngineerSteve engineerSteve = new EngineerSteve(ModEntities.ENGINEER_STEVE.get(), world);
                    engineerSteve.moveTo(spawnPos.getX(), spawnPos.getY() + 3, spawnPos.getZ(), 0, 0);
                    world.addFreshEntity(engineerSteve);
                }

                player.sendSystemMessage(Component.literal("A Steve Army is attacking!").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD));
                armySpawned = true;
            }
        }

        if (!outpostSpawnedThisNight && armorStands.isEmpty()) {
            if (new Random().nextFloat() < 0.5) {
                BlockPos playerPos = player.blockPosition();
                //random angle/distance
                double angle = new Random().nextDouble() * 2 * Math.PI;
                double distance = 40 + new Random().nextDouble() * 10;

                int xOffset = (int) (Math.cos(angle) * distance);
                int zOffset = (int) (Math.sin(angle) * distance);

                //get surface y level and place at the random x/z coords
                BlockPos encampmentPos = world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, playerPos.offset(xOffset, 0, zOffset));

                //load and place structure
                StructureTemplateManager structureManager = world.getStructureManager();
                StructureTemplate encampment = structureManager.getOrCreate(new ResourceLocation("steveciv:banner_spawn"));


                if (encampment != null) {
                    StructurePlaceSettings settings = new StructurePlaceSettings().setIgnoreEntities(false);
                    encampment.placeInWorld(world, encampmentPos, encampmentPos, settings, world.getRandom(), 2);

                    player.sendSystemMessage(Component.literal("A Steve outpost has appeared nearby!")
                            .withStyle(ChatFormatting.RED));

                    //prevent more than 1 spawn per night
                    outpostSpawnedThisNight = true;
                }
            }

//            AABB boundingBox = new AABB(player.blockPosition()).inflate(50);
//            List<ArmorStand> armorStands = world.getEntitiesOfClass(ArmorStand.class, boundingBox, armorStand -> armorStand.getTags().contains("EncampmentBanner"));

//            if (!armorStands.isEmpty()) {
//                if (new Random().nextFloat() < 0.5) {
//                    //spawn 10 steve warriors
//                    ArmorStand bannerStand = armorStands.get(0);
//                    BlockPos spawnPos = bannerStand.blockPosition();
//
//                    for (int i = 0; i < 10; i++) {
//                        SteveGuard steveWarrior = new SteveGuard(ModEntities.STEVE_GUARD.get(), world);
//                        steveWarrior.moveTo(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 0, 0);
//                        world.addFreshEntity(steveWarrior);
//                    }
//
//                    player.sendSystemMessage(Component.literal("A Steve Army is attacking!").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD));
//                }
//            }
        }
    }

    //reset spawner
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        for (ServerLevel serverWorld : event.getServer().getAllLevels()) {
            if (serverWorld.isDay() && outpostSpawnedThisNight) {
                outpostSpawnedThisNight = false;
                armySpawned = false;
            }
        }
    }
}