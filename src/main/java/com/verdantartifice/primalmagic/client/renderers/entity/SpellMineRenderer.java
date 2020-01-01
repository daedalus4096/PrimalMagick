package com.verdantartifice.primalmagic.client.renderers.entity;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.models.SpellMineModel;
import com.verdantartifice.primalmagic.common.entities.projectiles.SpellMineEntity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpellMineRenderer extends EntityRenderer<SpellMineEntity> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/spell_projectile.png");
    
    protected final SpellMineModel model = new SpellMineModel();

    public SpellMineRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }
    
    protected float rotLerp(float prevRot, float curRot, float partialTicks) {
        float val = curRot - prevRot;
        while (val < -180.0F) {
            val += 360.0F;
        }
        while (val >= 180.0F) {
            val -= 360.0F;
        }
        return prevRot + (val * partialTicks);
    }
    
    @Override
    public void doRender(SpellMineEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        float yaw = this.rotLerp(entity.prevRotationYaw, entity.rotationYaw, partialTicks);
        float pitch = MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch);
        float ticks = (float)entity.ticksExisted + partialTicks;
        Color c = new Color(entity.getColor());
        float r = (float)c.getRed() / 255.0F;
        float g = (float)c.getGreen() / 255.0F;
        float b = (float)c.getBlue() / 255.0F;
        float alphaFactor = entity.isArmed() ? 0.25F : 1.0F;
        double bob = 0.25D * MathHelper.sin(ticks * 0.1F);
        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y + 0.5D + bob, z);
        GlStateManager.rotatef(MathHelper.sin(ticks * 0.01F) * 180.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(MathHelper.cos(ticks * 0.01F) * 180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotatef(MathHelper.sin(ticks * 0.015F) * 360.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.color4f(r, g, b, 1.0F * alphaFactor);
        this.bindEntityTexture(entity);
        this.model.render(entity, 0.0F, 0.0F, 0.0F, yaw, pitch, 0.015625F);
        GlStateManager.color4f(r, g, b, 0.5F * alphaFactor);
        GlStateManager.scalef(1.5F, 1.5F, 1.5F);
        this.model.render(entity, 0.0F, 0.0F, 0.0F, yaw, pitch, 0.015625F);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(SpellMineEntity entity) {
        return TEXTURE;
    }
}
