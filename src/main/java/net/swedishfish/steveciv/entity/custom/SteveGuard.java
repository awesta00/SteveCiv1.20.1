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
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.swedishfish.steveciv.entity.ai.SteveGuardAttackGoals;
import org.jetbrains.annotations.Nullable;

public class SteveGuard extends Monster {
    protected int shieldBlockTicks = -1;
//    public int fireDelay = 20;



    public SteveGuard(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }



    @Override
    public void tick() {
        super.tick();

        //friendly fire
        LivingEntity currentTarget = this.getTarget();
        //friendly fire prevention between steves
        if (currentTarget instanceof SteveGuard) {
            this.setTarget(null);
            return;
        }

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

        this.goalSelector.addGoal(2, new SteveGuardAttackGoals(this, 1.5D, true));
//        this.goalSelector.addGoal(2, new SteveGuardExtinguishFireGoal(this));
//        this.goalSelector.addGoal(1, new (this));

        this.goalSelector.addGoal(5, new TemptGoal(this, 1.15D, Ingredient.of(Items.DIAMOND), false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.15D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Vex.class, 6.0F, 1.0D, 1.25D));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Sheep.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Zombie.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Skeleton.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Spider.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Husk.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Stray.class, true));

    }



    public static AttributeSupplier.Builder createAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH,30D)
                .add(Attributes.MOVEMENT_SPEED,0.23D)
                .add(Attributes.ARMOR_TOUGHNESS,0.1f)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.37)
                .add(Attributes.ATTACK_DAMAGE,3f)
                .add(Attributes.ATTACK_SPEED,30f)
                .add(Attributes.FOLLOW_RANGE,50D);
    }


    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        SpawnGroupData entityData = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);

        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));

//        this.getItemBySlot(EquipmentSlot.MAINHAND).enchant(Enchantments.SHARPNESS, 2); // Example enchantment

        return entityData;
    }

//    @Override
//    public void tick() {
//        super.tick();
//
//        // Handle shield block pause
//        if (shieldBlockTicks > 0) {
//            shieldBlockTicks--;
//        }
//    }

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

