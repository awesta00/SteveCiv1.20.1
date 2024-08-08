package net.swedishfish.steveciv.entity.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.swedishfish.steveciv.SteveCiv;
import net.swedishfish.steveciv.entity.custom.SteveGuard;

public class GuardRenderer  extends HumanoidMobRenderer<SteveGuard, HumanoidModel<SteveGuard>> {


    public GuardRenderer(EntityRendererProvider.Context context){
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)),0.5f);
    }



    @Override
    public ResourceLocation getTextureLocation(SteveGuard pEntity) {
        return new ResourceLocation(SteveCiv.MOD_ID, "textures/entity/steve.png");
    }
}
