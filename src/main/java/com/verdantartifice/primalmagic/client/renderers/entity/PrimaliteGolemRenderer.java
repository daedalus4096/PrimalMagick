package com.verdantartifice.primalmagic.client.renderers.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.entity.model.PrimaliteGolemModel;
import com.verdantartifice.primalmagic.common.entities.companions.PrimaliteGolemEntity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Entity renderer for a primalite golem.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class PrimaliteGolemRenderer extends MobRenderer<PrimaliteGolemEntity, PrimaliteGolemModel<PrimaliteGolemEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/golem/primalite_golem.png");
    
    public PrimaliteGolemRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new PrimaliteGolemModel<>(), 0.7F);
    }

    @Override
    public ResourceLocation getEntityTexture(PrimaliteGolemEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void applyRotations(PrimaliteGolemEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        if (!((double)entityLiving.limbSwingAmount < 0.01D)) {
            float f = 13.0F;
            float f1 = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
            float f2 = (Math.abs(f1 % f - 6.5F) - 3.25F) / 3.25F;
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(6.5F * f2));
        }
    }
}
