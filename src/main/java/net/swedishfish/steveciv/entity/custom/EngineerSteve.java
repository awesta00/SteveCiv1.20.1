package net.swedishfish.steveciv.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.swedishfish.steveciv.entity.ai.AvoidTNT;
import net.swedishfish.steveciv.entity.ai.EngineerAvoidTNT;
import net.swedishfish.steveciv.entity.ai.EngineerGoals;
import org.jetbrains.annotations.Nullable;

public class EngineerSteve extends Monster{

    public EngineerSteve(EntityType<? extends Monster> pEntityType, Level pLevel) {
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
        if (currentTarget instanceof EngineerSteve) {
            this.setTarget(null);
            return;
        }

    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new EngineerGoals(this));
        this.goalSelector.addGoal(1, new EngineerAvoidTNT(this));



        this.goalSelector.addGoal(5, new TemptGoal(this, 1.15D, Ingredient.of(Items.DIAMOND), false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.15D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Vex.class, 6.0F, 1.0D, 1.25D));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 6.0F, 1.1D, 1.25D));


    }



    public static AttributeSupplier.Builder createAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH,26D)
                .add(Attributes.MOVEMENT_SPEED,0.23D)
                .add(Attributes.ARMOR_TOUGHNESS,0.1f)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.37)
                .add(Attributes.ATTACK_DAMAGE,2.5f)
                .add(Attributes.ATTACK_SPEED,30f)
                .add(Attributes.FOLLOW_RANGE,50D);
    }


    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        SpawnGroupData entityData = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);

        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.TNT));

        return entityData;
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
