package com.verdantartifice.primalmagick.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.renderers.entity.layers.InnerDemonArmorLayer;
import com.verdantartifice.primalmagick.common.entities.misc.InnerDemonEntity;
import com.verdantartifice.primalmagick.common.entities.misc.SinCrystalEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Entity renderer for an inner demon.
 * 
 * @author Daedalus4096
 */
public class InnerDemonRenderer extends HumanoidMobRenderer<InnerDemonEntity, PlayerModel<InnerDemonEntity>> {
    protected static final float SCALE = 2.0F;
    
    protected final EntityRendererProvider.Context context;
    protected InnerDemonArmorLayer armorLayer;
    protected boolean modelFinalized = false;
    
    public InnerDemonRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<InnerDemonEntity>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F * SCALE);
        this.context = context;
        this.armorLayer = new InnerDemonArmorLayer(this, context.getModelSet(), false);
        this.addLayer(this.armorLayer);
    }

    @Override
    public ResourceLocation getTextureLocation(InnerDemonEntity entity) {
        // Use the viewing player's skin texture
        Minecraft mc = Minecraft.getInstance();
        return mc.player.getSkin().texture();
    }

    @Override
    protected void scale(InnerDemonEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        if (!this.modelFinalized) {
            // Can't get the player's skin type at renderer registration time, so monkey-patch it after we're already going
            Minecraft mc = Minecraft.getInstance();
            boolean slimModel = mc.player.getSkin().model().equals(PlayerSkin.Model.SLIM);
            
            this.model = new PlayerModel<InnerDemonEntity>(this.context.bakeLayer(slimModel ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slimModel);
            
            this.layers.remove(this.armorLayer);
            this.armorLayer = new InnerDemonArmorLayer(this, this.context.getModelSet(), slimModel);
            this.addLayer(this.armorLayer);
            
            this.modelFinalized = true;
        }
        matrixStackIn.scale(SCALE, SCALE, SCALE);
    }

    @Override
    public void render(InnerDemonEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        
        // Render beams for each in-range sin crystal
        for (SinCrystalEntity crystal : entityIn.getCrystalsInRange()) {
            matrixStackIn.pushPose();
            float f6 = (float)(crystal.getX() - Mth.lerp((double)partialTicks, entityIn.xo, entityIn.getX()));
            float f8 = (float)(crystal.getY() - Mth.lerp((double)partialTicks, entityIn.yo, entityIn.getY()));
            float f9 = (float)(crystal.getZ() - Mth.lerp((double)partialTicks, entityIn.zo, entityIn.getZ()));
            EnderDragonRenderer.renderCrystalBeams(f6, f8 + SinCrystalRenderer.getDeltaY(crystal, partialTicks), f9, partialTicks, entityIn.tickCount, matrixStackIn, bufferIn, packedLightIn);
            matrixStackIn.popPose();
        }
    }
}
