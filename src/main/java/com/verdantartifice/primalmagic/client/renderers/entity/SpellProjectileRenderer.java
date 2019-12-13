package com.verdantartifice.primalmagic.client.renderers.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.models.SpellProjectileModel;
import com.verdantartifice.primalmagic.common.entities.projectiles.SpellProjectileEntity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpellProjectileRenderer extends EntityRenderer<SpellProjectileEntity> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/spell_projectile.png");
    
    protected final SpellProjectileModel model = new SpellProjectileModel();

    public SpellProjectileRenderer(EntityRendererManager renderManager) {
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
    public void doRender(SpellProjectileEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        float yaw = this.rotLerp(entity.prevRotationYaw, entity.rotationYaw, partialTicks);
        float pitch = MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch);
        float ticks = (float)entity.ticksExisted + partialTicks;
        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y + 0.15D, z);
        GlStateManager.rotatef(MathHelper.sin(ticks * 0.1F) * 180.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(MathHelper.cos(ticks * 0.1F) * 180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotatef(MathHelper.sin(ticks * 0.15F) * 360.0F, 0.0F, 0.0F, 1.0F);
        this.bindEntityTexture(entity);
        this.model.render(entity, 0.0F, 0.0F, 0.0F, yaw, pitch, 0.015625F);
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(SpellProjectileEntity entity) {
        return TEXTURE;
    }
}
