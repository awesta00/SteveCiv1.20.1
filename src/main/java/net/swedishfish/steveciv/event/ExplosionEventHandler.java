package net.swedishfish.steveciv.event;


import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Set;

public class ExplosionEventHandler {

    // Set of blocks that are allowed to be destroyed by Steve's TNT
    private static final Set<Block> destructibleBlocks = Set.of(Blocks.OAK_DOOR, Blocks.IRON_DOOR, Blocks.OAK_FENCE, Blocks.OAK_FENCE_GATE, Blocks.BLACK_BED); /* add more as needed */

//    @SubscribeEvent
//    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
//        System.out.println("Explosion detected!");
//
//        // Your filtering logic for blocks
//        Level world = event.getLevel();
//        Explosion explosion = event.getExplosion();
//        List<BlockPos> affectedBlocks = event.getAffectedBlocks();
//
//        // Remove blocks not in the destructibleBlocks set
//        affectedBlocks.removeIf(blockPos -> {
//            Block block = world.getBlockState(blockPos).getBlock();
//            return !destructibleBlocks.contains(block);
//        });
//    }
@SubscribeEvent
public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
    Explosion explosion = event.getExplosion();

    // Check if the explosion was caused by PrimedTnt
    if (explosion.getExploder() instanceof PrimedTnt primedTnt) {
        if (primedTnt.getPersistentData().getBoolean("PlacedBySteve")) {
//            System.out.println("Steve's TNT exploded!");

            Level world = event.getLevel();
            List<BlockPos> affectedBlocks = event.getAffectedBlocks();
            affectedBlocks.removeIf(blockPos -> {
                Block block = world.getBlockState(blockPos).getBlock();
                return !destructibleBlocks.contains(block);  // Only destroy blocks in the destructible list
            });
        }
    }
}



}
