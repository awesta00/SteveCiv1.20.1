package net.swedishfish.steveciv.entity.custom;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SteveGuard extends Monster {



    public SteveGuard(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new MeleeAttackGoal(this,2.15D, true));
        this.goalSelector.addGoal(2, new TemptGoal(this,1.15D, Ingredient.of(Items.DIAMOND),false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this,1.15D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class,3f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

    }

    public static AttributeSupplier.Builder createAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH,20D)
                .add(Attributes.MOVEMENT_SPEED,0.24D)
                .add(Attributes.ARMOR_TOUGHNESS,0.1f)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.12)
                .add(Attributes.ATTACK_DAMAGE,4f)
                .add(Attributes.FOLLOW_RANGE,30D);
    }

//
//    @Nullable
//    @Override
//    protected SoundEvent getAmbientSound() {
//        return SoundEvents.;
//    }


    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PLAYER_DEATH;
    }
}

