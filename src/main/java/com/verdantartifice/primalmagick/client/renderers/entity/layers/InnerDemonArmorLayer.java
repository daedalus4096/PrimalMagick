package com.verdantartifice.primalmagick.client.renderers.entity.layers;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.entities.misc.InnerDemonEntity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * Layer renderer for the energy field surrounding an inner demon.
 * 
 * @author Daedalus4096
 */
public class InnerDemonArmorLayer extends EnergySwirlLayer<InnerDemonEntity, PlayerModel<InnerDemonEntity>> {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/entity/inner_demon/inner_demon_armor.png");
    protected final PlayerModel<InnerDemonEntity> model;

    public InnerDemonArmorLayer(RenderLayerParent<InnerDemonEntity, PlayerModel<InnerDemonEntity>> renderer, EntityModelSet modelSet, boolean slimModel) {
        super(renderer);
        this.model = new PlayerModel<InnerDemonEntity>(modelSet.bakeLayer(slimModel ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slimModel);
    }

    @Override
    protected float xOffset(float p_225634_1_) {
        return p_225634_1_ * 0.01F;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return TEXTURE;
    }

    @Override
    protected EntityModel<InnerDemonEntity> model() {
        return this.model;
    }
}
