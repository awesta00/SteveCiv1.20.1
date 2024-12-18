package net.swedishfish.steveciv.entity.ai;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.swedishfish.steveciv.entity.custom.SteveGuard;
import java.util.Random;


public class SteveGuardAttackGoals extends MeleeAttackGoal {
    private final SteveGuard steve;
    Random rand = new Random();
    Random random = new Random();

    private final double strafeSpeed = 1.1D;
    protected int jumpDelay = randomTimeGen(1,7);
    protected int pearlDelay = randomTimeGen(15,50);
    protected int healDelay = randomTimeGen(5,8);
//    protected int cobwebDelay = randomTimeGen(12,30);
    protected int numGapples = randomGen(1,8);
//    protected int numCobwebs = randomGen(0,5);
    protected boolean isRetreating = false;
    private int retreatTicks = 0;
    protected int handDelayTicks = 0;
    public ItemStack previousWeapon;
    private int blockPlaceCooldown = 0;



    public SteveGuardAttackGoals(SteveGuard steve, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(steve, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        this.steve = steve;
        //switch back to original weapon
        switch (this.rand.nextInt(2)) {
            case 0:
                this.steve.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
                previousWeapon = new ItemStack(Items.IRON_SWORD);
                break;
            case 1:
                this.steve.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
                previousWeapon = new ItemStack(Items.IRON_AXE);
                break;
        }

        this.steve.setItemSlot(EquipmentSlot.HEAD, getRandomArmorPiece(random, EquipmentSlot.HEAD));
        this.steve.setItemSlot(EquipmentSlot.CHEST, getRandomArmorPiece(random, EquipmentSlot.CHEST));
        this.steve.setItemSlot(EquipmentSlot.LEGS, getRandomArmorPiece(random, EquipmentSlot.LEGS));
        this.steve.setItemSlot(EquipmentSlot.FEET, getRandomArmorPiece(random, EquipmentSlot.FEET));
        this.steve.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));

    }

    @Override
    public boolean canUse() {
        return super.canUse() && this.steve.getTarget() != null || this.steve.isOnFire();
    }

    private int randomGen(int min, int max){
        return min + rand.nextInt(max - min + 1);
    }

    private int randomTimeGen(int min, int max){
        return (min + rand.nextInt(max - min + 1)) * 20;
    }

    public int getNumGapples(){
        return numGapples;
    }

    @Override
    protected double getAttackReachSqr(LivingEntity target) {
        return 2.1 * 2.1; //sets reach of steve to 2.2 blocks
    }

    private ItemStack getRandomArmorPiece(Random random, EquipmentSlot slot) {
        int choice = random.nextInt(5);

        switch (choice) {
            case 0:
                return slot == EquipmentSlot.HEAD ? new ItemStack(Items.LEATHER_HELMET) :
                        slot == EquipmentSlot.CHEST ? new ItemStack(Items.LEATHER_CHESTPLATE) :
                                slot == EquipmentSlot.LEGS ? new ItemStack(Items.LEATHER_LEGGINGS) :
                                        new ItemStack(Items.LEATHER_BOOTS);

            case 1:
                return slot == EquipmentSlot.HEAD ? new ItemStack(Items.CHAINMAIL_HELMET) :
                        slot == EquipmentSlot.CHEST ? new ItemStack(Items.CHAINMAIL_CHESTPLATE) :
                                slot == EquipmentSlot.LEGS ? new ItemStack(Items.CHAINMAIL_LEGGINGS) :
                                        new ItemStack(Items.CHAINMAIL_BOOTS);

            case 2:
                return slot == EquipmentSlot.HEAD ? new ItemStack(Items.GOLDEN_HELMET) :
                        slot == EquipmentSlot.CHEST ? new ItemStack(Items.GOLDEN_CHESTPLATE) :
                                slot == EquipmentSlot.LEGS ? new ItemStack(Items.GOLDEN_LEGGINGS) :
                                        new ItemStack(Items.GOLDEN_BOOTS);

            case 3:
                return slot == EquipmentSlot.HEAD ? new ItemStack(Items.IRON_HELMET) :
                        slot == EquipmentSlot.CHEST ? new ItemStack(Items.IRON_CHESTPLATE) :
                                slot == EquipmentSlot.LEGS ? new ItemStack(Items.IRON_LEGGINGS) :
                                        new ItemStack(Items.IRON_BOOTS);

            case 4:
            default:
                return slot == EquipmentSlot.HEAD ? new ItemStack(Items.DIAMOND_HELMET) :
                        slot == EquipmentSlot.CHEST ? new ItemStack(Items.DIAMOND_CHESTPLATE) :
                                slot == EquipmentSlot.LEGS ? new ItemStack(Items.DIAMOND_LEGGINGS) :
                                        new ItemStack(Items.DIAMOND_BOOTS);
        }
    }

    private void retreat() {
        //calculating retreat path
        Vec3 retreatDirection = this.steve.position().subtract(this.steve.getTarget().position()).normalize().scale(10); // Move 10 blocks away from the target
        Vec3 retreatPosition = this.steve.position().add(retreatDirection);
        //move
        this.steve.getNavigation().moveTo(retreatPosition.x, retreatPosition.y, retreatPosition.z, 1.1D);

    }

    private void heal() {
        this.steve.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.GOLDEN_APPLE));
        //play animation/sound
        this.steve.startUsingItem(InteractionHand.MAIN_HAND);

        //apply effects of gapple
        this.steve.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1)); //5 sec regen
        this.steve.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0)); //2 min absorption
    }


    @Override
    public void tick() {
        super.tick();

        if(this.steve.isOnFire() && !this.steve.hasEffect(MobEffects.FIRE_RESISTANCE)){
            this.steve.setItemInHand(InteractionHand.MAIN_HAND, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.FIRE_RESISTANCE));
            this.steve.startUsingItem(InteractionHand.MAIN_HAND);
            isRetreating = true;
            retreatTicks = 22;
        }
        if (isRetreating) {
            retreatTicks--;
            if (retreatTicks <= 0) {
                isRetreating = false;
                this.steve.setItemInHand(InteractionHand.MAIN_HAND, previousWeapon);
            }
        }


        if (this.steve.getTarget() != null) {
            //trigger for leaps at target
                Random random = new Random();
                Vec3 targetDirection = new Vec3(this.steve.getTarget().getX() - this.steve.getX(), 0.0D, this.steve.getTarget().getZ() - this.steve.getZ());

                if (targetDirection.lengthSqr() > 1.0E-7D) {
                    //face target
                    this.steve.setYRot((float) (Mth.atan2(targetDirection.z, targetDirection.x) * (180F / Math.PI)) - 90.0F);
                    this.steve.setXRot((float) (Mth.atan2(targetDirection.z, targetDirection.x) * (180F / Math.PI)) - 90.0F);

                    if (!isRetreating && jumpDelay == 1 && this.steve.distanceTo(steve.getTarget()) < 6.5) {
                        //jump
                        targetDirection = targetDirection.normalize().scale(0.6D);
                        this.steve.setDeltaMovement(targetDirection.x, 0.42, targetDirection.z);

                        if (random.nextFloat() < 0.1F) {
                        //strafe (idk if this even works)
                        double strafeDirection = random.nextBoolean() ? 1.0D : -1.0D; //choose strafe left or right
                        Vec3 strafeVector = new Vec3(strafeDirection * strafeSpeed, 0.2D, strafeDirection * strafeSpeed);
                        this.steve.move(MoverType.SELF, strafeVector);
                        }
                    }
            }
//            if (currentTarget != null && this.steve.distanceTo(currentTarget) < 3.0D && numCobwebs > 0 && cobwebDelay < 1) {
//                // Check if Steve is not already using an item
//                if (!this.steve.isUsingItem()) {
//                    // Place cobweb
//                    BlockPos targetPosition = currentTarget.blockPosition();
//                    BlockPos placementPosition = targetPosition.below(); // Place cobweb on the ground below the target
//                    if (this.steve.level().getBlockState(placementPosition).isAir()) {
//                        this.steve.level().setBlockAndUpdate(placementPosition, Blocks.COBWEB.defaultBlockState());
//                        numCobwebs--;
//                        cobwebDelay = randomTimeGen(12,30);
//                    }
//                }
//            }
                if(!isRetreating && pearlDelay < 1 && this.steve.distanceTo(steve.getTarget()) < 30 && this.steve.distanceTo(steve.getTarget()) > 9){
                    //throw pearl
                    this.steve.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.ENDER_PEARL));

                    //calc target position
                    Vec3 targetPos = this.steve.getTarget().position();
                    Vec3 direction = targetPos.subtract(this.steve.position()).normalize();

                    ThrownEnderpearl pearlEntity = new ThrownEnderpearl(this.steve.level(), this.steve);
                    pearlEntity.setItem(new ItemStack(Items.ENDER_PEARL));
                    pearlEntity.shoot(direction.x, direction.y + 0.1, direction.z, 1.5F, 1.0F);
                    //play sound???
                    this.steve.level().addFreshEntity(pearlEntity);

                    pearlDelay = randomTimeGen(15,45);
                    handDelayTicks = 15;
                }

                if(healDelay < 1 && this.steve.getHealth() < 8 && getNumGapples() > 0){
                    //heal
                    //set main hand to gapple, eat animation/sound, give regen/absorption
                    isRetreating = true;
                    retreatTicks = 36;
                    numGapples--;
                    //maybe retreat doesnt work because melee atk goal and retreat conflict
                    heal();
                }
            if (isRetreating) {
                retreatTicks--;
                if (retreatTicks <= 0) {
//                    heal();
                    isRetreating = false;
                    healDelay = randomTimeGen(5,8);
                    this.steve.setItemInHand(InteractionHand.MAIN_HAND, previousWeapon);
                }
            }

            //reset jump cooldown
            if(jumpDelay < 1){
                jumpDelay = randomTimeGen(1,7);
            }
            if(handDelayTicks > 0){
                handDelayTicks--;
                if(handDelayTicks <= 1){
                    this.steve.setItemInHand(InteractionHand.MAIN_HAND, previousWeapon);
                }
            }
            jumpDelay--;
            pearlDelay--;
            healDelay--;
//            cobwebDelay--;

        }


    }
}


