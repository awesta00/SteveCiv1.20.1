package net.swedishfish.steveciv.entity.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.swedishfish.steveciv.SteveCiv;
import net.swedishfish.steveciv.entity.custom.ElytraSteve;

public class ElytraSteveRenderer extends HumanoidMobRenderer<ElytraSteve, HumanoidModel<ElytraSteve>> {

    public ElytraSteveRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(
                this,
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
                context.getModelManager()));
    }

    @Override
    public ResourceLocation getTextureLocation(ElytraSteve pEntity) {
        return new ResourceLocation(SteveCiv.MOD_ID, "textures/entity/steve.png");
    }
}