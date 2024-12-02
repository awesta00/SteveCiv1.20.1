package net.swedishfish.steveciv.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.swedishfish.steveciv.entity.ai.AvoidTNT;
import net.swedishfish.steveciv.entity.ai.SteveGuardAttackGoals;
import org.jetbrains.annotations.Nullable;


public class SteveGuard extends Monster {
    protected int shieldBlockTicks = -1;


    public SteveGuard(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }



    @Override
    public void tick() {
        super.tick();


        if(this.getTarget() == null || !(this.getTarget() instanceof Player)){
            Player nearestPlayer = this.level().getNearestPlayer(this, 100.0D); //range 100 blocks
            if (nearestPlayer != null && !nearestPlayer.isCreative()) {
                this.setTarget(nearestPlayer);
            }        }

        if(shieldBlockTicks >= 0){
            shieldBlockTicks--;
            if(shieldBlockTicks == 0){
                this.stopUsingItem();
                shieldBlockTicks = -1;
            }
        }
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));

        this.goalSelector.addGoal(2, new SteveGuardAttackGoals(this, 1.35D, true));
        this.goalSelector.addGoal(1, new AvoidTNT(this));

//        this.goalSelector.addGoal(1, new Vindicator.VindicatorBreakDoorGoal(this));
//        this.goalSelector.addGoal(2, new AbstractIllager.RaiderOpenDoorGoal(this));

        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.15D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(8, new NearestAttackableTargetGoal<>(this, Sheep.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }


    public static AttributeSupplier.Builder createAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH,26D)
                .add(Attributes.MOVEMENT_SPEED,0.228D)
                .add(Attributes.ARMOR_TOUGHNESS,0.1f)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.37)
                .add(Attributes.ATTACK_DAMAGE,2.5f)
                .add(Attributes.ATTACK_SPEED,30f)
                .add(Attributes.FOLLOW_RANGE,100D);
    }


    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        SpawnGroupData entityData = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);

        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
//        this.getItemBySlot(EquipmentSlot.MAINHAND).enchant(Enchantments.SHARPNESS, 2);

        return entityData;
    }


    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getDirectEntity() instanceof AbstractArrow && this.getItemBySlot(EquipmentSlot.OFFHAND).getItem() == Items.SHIELD) {
            AbstractArrow arrow = (AbstractArrow) source.getDirectEntity();

            //steve dir
            Vec3 facingDirection = Vec3.directionFromRotation(this.getXRot(), this.getYRot());
            //arrow dir
            Vec3 arrowDirection = arrow.getDeltaMovement().normalize();
            //dot product between two
            double dotProduct = facingDirection.dot(arrowDirection);

            if (dotProduct < 0.5) { //facing the arrow
                if (this.random.nextFloat() < 0.95f) {
                    this.startUsingItem(InteractionHand.OFF_HAND);
                    shieldBlockTicks = 15;
                    this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 1.0F);
                    return false; //blocks damage
                }
            } else { //gets hit from behind and deals full damage
                if (this.random.nextFloat() < 0.95f) {
                    return super.hurt(source, amount);
                }
            }
        }

        return super.hurt(source, amount);
    }


    @Override
    protected boolean shouldDropLoot() {
        return false;
    }

    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }


    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.ILLUSIONER_DEATH;
    }

}

