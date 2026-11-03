package com.verdantartifice.primalmagick.client.renderers.entity.layers;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;

/**
 * Layer renderer for the energy field surrounding an inner demon.
 * 
 * @author Daedalus4096
 */
public class InnerDemonArmorLayer extends EnergySwirlLayer<AvatarRenderState, PlayerModel> {
    protected static final Identifier TEXTURE = ResourceUtils.loc("textures/entity/inner_demon/inner_demon_armor.png");
    protected final PlayerModel model;

    public InnerDemonArmorLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer, EntityModelSet modelSet, boolean slimModel) {
        super(renderer);
        this.model = new PlayerModel(modelSet.bakeLayer(slimModel ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slimModel);
    }

    @Override
    protected boolean isPowered(AvatarRenderState state) {
        return true;
    }

    @Override
    protected float xOffset(float p_225634_1_) {
        return p_225634_1_ * 0.01F;
    }

    @Override
    protected Identifier getTextureLocation() {
        return TEXTURE;
    }

    @Override
    protected PlayerModel model() {
        return this.model;
    }
}
