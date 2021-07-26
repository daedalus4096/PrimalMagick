package com.verdantartifice.primalmagic.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.client.renderers.entity.layers.InnerDemonArmorLayer;
import com.verdantartifice.primalmagic.common.entities.misc.InnerDemonEntity;
import com.verdantartifice.primalmagic.common.entities.misc.SinCrystalEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Entity renderer for an inner demon.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class InnerDemonRenderer extends HumanoidMobRenderer<InnerDemonEntity, PlayerModel<InnerDemonEntity>> {
    protected static final float SCALE = 2.0F;
    
    protected InnerDemonArmorLayer armorLayer;
    protected boolean modelFinalized = false;
    
    public InnerDemonRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<InnerDemonEntity>(0.0F, false), 0.5F * SCALE);
        this.armorLayer = new InnerDemonArmorLayer(this, false);
        this.addLayer(this.armorLayer);
    }

    @Override
    public ResourceLocation getTextureLocation(InnerDemonEntity entity) {
        // Use the viewing player's skin texture
        Minecraft mc = Minecraft.getInstance();
        return mc.player.getSkinTextureLocation();
    }

    @Override
    protected void scale(InnerDemonEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        if (!this.modelFinalized) {
            // Can't get the player's skin type at renderer registration time, so monkey-patch it after we're already going
            Minecraft mc = Minecraft.getInstance();
            boolean slimModel = mc.player.getModelName().equals("slim");
            
            this.model = new PlayerModel<InnerDemonEntity>(0.0F, slimModel);
            
            this.layers.remove(this.armorLayer);
            this.armorLayer = new InnerDemonArmorLayer(this, slimModel);
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
