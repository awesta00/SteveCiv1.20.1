package net.swedishfish.steveciv.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.swedishfish.steveciv.entity.custom.EngineerSteve;

import java.util.EnumSet;
import java.util.Set;


public class EngineerGoals extends Goal {

    private final EngineerSteve steve;
    private BlockPos targetPos;
    public int cooldown;
    private int stuckTicks = 0; // duration of being stuck
    private static final int STUCK_THRESHOLD = 60; // # of ticks before considering stuck (5 sec)
    private static final double MIN_PROGRESS = 0.3D; // Min distance to consider as progress
    private BlockPos lastPosition;
    private boolean primaryCheck = false; //checks to see if primary targets have been checked for stuck
    private boolean secondaryCheck = false;

    private final Set<Block> destructibleBlocks = Set.of(
            Blocks.OAK_DOOR, Blocks.IRON_DOOR, Blocks.OAK_FENCE, Blocks.OAK_FENCE_GATE,
            Blocks.BLACK_BED, Blocks.BLUE_BED, Blocks.BROWN_BED, Blocks.LIGHT_BLUE_BED,
            Blocks.CYAN_BED, Blocks.GRAY_BED, Blocks.GREEN_BED, Blocks.LIGHT_GRAY_BED,
            Blocks.LIME_BED, Blocks.MAGENTA_BED, Blocks.ORANGE_BED, Blocks.PINK_BED,
            Blocks.PURPLE_BED, Blocks.RED_BED, Blocks.WHITE_BED, Blocks.YELLOW_BED,
            Blocks.STONE_BRICKS, Blocks.COBBLESTONE, Blocks.COBBLESTONE_WALL
    );




    public EngineerGoals(EngineerSteve steve) {
        this.steve = steve;
        this.cooldown = 0;
        this.lastPosition = steve.blockPosition();

        this.steve.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        this.steve.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
        this.steve.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
        this.steve.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
        this.steve.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.TNT));
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));

    }

    @Override
    public boolean canUse() {
//        return cooldown <= 0;
        return true;
    }

private boolean isStuck() {
    BlockPos currentPosition = this.steve.blockPosition();
    double distanceMoved = currentPosition.distSqr(lastPosition);

    if (distanceMoved < MIN_PROGRESS) {
        stuckTicks++;
    } else {
        stuckTicks = 0;
    }
    lastPosition = currentPosition;
    return stuckTicks >= STUCK_THRESHOLD;

}

    @Override
    public void tick() {
        super.tick();

        if (targetPos != null) {
            BlockPos stevePos = steve.blockPosition();
            ServerLevel world = (ServerLevel) steve.level();

            if (isStuck()) {
//                System.out.println("Is stuck = " + isStuck());

                if(secondaryCheck && primaryCheck){
                    //explode
                    this.steve.kill();
                    secondaryCheck = false;
                    primaryCheck = false;
//                    System.out.println("Target not reachable");
                }
                else if(primaryCheck){
                    //try secondary
//                    secondaryCheck = true;
                    stuckTicks = 0;
                    targetPos = findClosestBlock(world, stevePos, 40, Blocks.STONE_BRICKS, Blocks.COBBLESTONE, Blocks.COBBLESTONE_WALL);
//                    System.out.println("Trying secondary targets...");
                }
                else{
                    primaryCheck = true;
                }
            }
            else {
//                secondaryCheck = false;
//                primaryCheck = false;
            }
        }

        if(cooldown <= 0){
            this.steve.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.TNT));
            findTargetBlock();
            if (targetPos != null) {
                steve.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.2);
            }

            if (targetPos != null) {
                double distance = steve.distanceToSqr(targetPos.getX(), targetPos.getY(), targetPos.getZ());
                if (distance <= 16.0) { //4 blocks away
                    placeAndIgniteTNT();
                    cooldown = 300; //15 sec
                    targetPos = null;
                } else {
                    // move towards target
                    steve.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.2);
                }
            }
        }
        cooldown--;
    }

    private void findTargetBlock() {
        BlockPos stevePos = steve.blockPosition();
        ServerLevel world = (ServerLevel) steve.level();

        // First, search for the closest primary target
        if (!primaryCheck) {
            targetPos = findClosestBlock(world, stevePos, 40, Blocks.OAK_DOOR, Blocks.IRON_DOOR, Blocks.OAK_FENCE,
                    Blocks.OAK_FENCE_GATE, Blocks.BLACK_BED, Blocks.BLUE_BED, Blocks.BROWN_BED,
                    Blocks.LIGHT_BLUE_BED, Blocks.CYAN_BED, Blocks.GRAY_BED, Blocks.GREEN_BED,
                    Blocks.LIGHT_GRAY_BED, Blocks.LIME_BED, Blocks.MAGENTA_BED, Blocks.ORANGE_BED,
                    Blocks.PINK_BED, Blocks.PURPLE_BED, Blocks.RED_BED, Blocks.WHITE_BED,
                    Blocks.YELLOW_BED);

            if (targetPos != null) {
                return;  // If a primary target is found, return immediately
            } else {
                primaryCheck = true;  // No primary target found, switch to secondary targets
            }
        }

        // If no primary target is found, search for the closest secondary target
        if (primaryCheck && !secondaryCheck) {
            targetPos = findClosestBlock(world, stevePos, 40, Blocks.STONE_BRICKS, Blocks.COBBLESTONE,
                    Blocks.COBBLESTONE_WALL);
            if (targetPos != null) {
                return;  // If a secondary target is found, return immediately
            }
        }
    }

    private BlockPos findClosestBlock(ServerLevel world, BlockPos origin, int radius, Block... blocks) {
        BlockPos closestPos = null;
        double closestDistance = Double.MAX_VALUE;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    mutablePos.set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
                    BlockState state = world.getBlockState(mutablePos);

                    for (Block block : blocks) {
                        if (state.is(block)) {
                            double distance = origin.distSqr(mutablePos);
                            if (distance < closestDistance) {
                                closestDistance = distance;
                                closestPos = mutablePos.immutable();
                            }
                        }
                    }
                }
            }
        }

        return closestPos;
    }

//    private void placeAndIgniteTNT() {
//        if (targetPos != null) {
//            BlockPos tntPos = targetPos.relative(steve.getDirection().getOpposite());
//            ServerLevel world = (ServerLevel) steve.level();
//
//            world.setBlock(tntPos, Blocks.TNT.defaultBlockState(), 3);
//            this.steve.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.FLINT_AND_STEEL));
//
//            //place/light tnt
//            BlockState tntState = Blocks.TNT.defaultBlockState();
//            world.setBlock(tntPos, tntState, 3);
//
//            if (tntState.getBlock() instanceof TntBlock) {
//                TntBlock tntBlock = (TntBlock) tntState.getBlock();
//                tntBlock.onCaughtFire(tntState, world, tntPos, null, null);
//                world.setBlock(tntPos, Blocks.AIR.defaultBlockState(),3);
//            }
//
//            primaryCheck = false;
//            secondaryCheck = false;
//            this.steve.getNavigation().moveTo(steve.getX() + 20, steve.getY(), steve.getZ() + 20, 1.3);
//        }
//    }
private void placeAndIgniteTNT() {
    if (targetPos != null) {
        BlockPos tntPos = targetPos.relative(steve.getDirection().getOpposite());
        ServerLevel world = (ServerLevel) steve.level();

        // Spawn the TNT as a PrimedTnt entity
        PrimedTnt primedTnt = new PrimedTnt(world, tntPos.getX() + 0.5, tntPos.getY(), tntPos.getZ() + 0.5, null);
        world.addFreshEntity(primedTnt);

        // Set a custom NBT tag or flag on the PrimedTnt entity
        primedTnt.getPersistentData().putBoolean("PlacedBySteve", true);

        // Ignite the TNT
        primedTnt.setFuse(80);  // 4 seconds before explosion

        // Reset primary and secondary check and move Steve away
        primaryCheck = false;
        secondaryCheck = false;
        this.steve.kill();
//        this.steve.getNavigation().moveTo(steve.getX() + 20, steve.getY(), steve.getZ() + 20, 1.3);
    }
}






}
