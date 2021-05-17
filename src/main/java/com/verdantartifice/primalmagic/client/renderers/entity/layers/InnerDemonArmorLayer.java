package com.verdantartifice.primalmagic.client.renderers.entity.layers;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.entities.misc.InnerDemonEntity;

import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Layer renderer for the energy field surrounding an inner demon.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class InnerDemonArmorLayer extends EnergyLayer<InnerDemonEntity, PlayerModel<InnerDemonEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/inner_demon/inner_demon_armor.png");
    protected final PlayerModel<InnerDemonEntity> model;

    public InnerDemonArmorLayer(IEntityRenderer<InnerDemonEntity, PlayerModel<InnerDemonEntity>> renderer, boolean slimModel) {
        super(renderer);
        this.model = new PlayerModel<InnerDemonEntity>(2.0F, slimModel);
    }

    @Override
    protected float func_225634_a_(float p_225634_1_) {
        return p_225634_1_ * 0.01F;
    }

    @Override
    protected ResourceLocation func_225633_a_() {
        return TEXTURE;
    }

    @Override
    protected EntityModel<InnerDemonEntity> func_225635_b_() {
        return this.model;
    }
}
