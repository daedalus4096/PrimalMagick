package com.verdantartifice.primalmagic.client.renderers.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.verdantartifice.primalmagic.client.renderers.entity.layers.InnerDemonArmorLayer;
import com.verdantartifice.primalmagic.common.entities.misc.InnerDemonEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Entity renderer for an inner demon.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class InnerDemonRenderer extends BipedRenderer<InnerDemonEntity, PlayerModel<InnerDemonEntity>> {
    protected static final float SCALE = 2.0F;
    
    protected InnerDemonArmorLayer armorLayer;
    protected boolean modelFinalized = false;
    
    public InnerDemonRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new PlayerModel<InnerDemonEntity>(0.0F, false), 0.5F * SCALE);
        this.armorLayer = new InnerDemonArmorLayer(this, false);
        this.addLayer(this.armorLayer);
    }

    @Override
    public ResourceLocation getEntityTexture(InnerDemonEntity entity) {
        // Use the viewing player's skin texture
        Minecraft mc = Minecraft.getInstance();
        return mc.player.getLocationSkin();
    }

    @Override
    protected void preRenderCallback(InnerDemonEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        if (!this.modelFinalized) {
            // Can't get the player's skin type at renderer registration time, so monkey-patch it after we're already going
            Minecraft mc = Minecraft.getInstance();
            boolean slimModel = mc.player.getSkinType().equals("slim");
            
            this.entityModel = new PlayerModel<InnerDemonEntity>(0.0F, slimModel);
            
            this.layerRenderers.remove(this.armorLayer);
            this.armorLayer = new InnerDemonArmorLayer(this, slimModel);
            this.addLayer(this.armorLayer);
            
            this.modelFinalized = true;
        }
        matrixStackIn.scale(SCALE, SCALE, SCALE);
    }
}
