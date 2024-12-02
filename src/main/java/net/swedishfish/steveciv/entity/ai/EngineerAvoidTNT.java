package net.swedishfish.steveciv.entity.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.PrimedTnt;
import net.swedishfish.steveciv.entity.custom.EngineerSteve;

import java.util.List;

public class EngineerAvoidTNT extends Goal {
    private final EngineerSteve steve;

    public EngineerAvoidTNT(EngineerSteve steve) {
        this.steve = steve;
    }

    @Override
    public boolean canUse() {
        List<PrimedTnt> nearbyTnt = this.steve.level().getEntitiesOfClass(PrimedTnt.class, this.steve.getBoundingBox().inflate(8.0));
        return !nearbyTnt.isEmpty();
    }

    @Override
    public void start() {
        this.steve.getNavigation().moveTo(this.steve.getX() + 10, this.steve.getY(), this.steve.getZ() + 10, 1.5D);
    }

}
