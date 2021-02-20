package com.verdantartifice.primalmagic.client.renderers.entity;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.models.SpellMineModel;
import com.verdantartifice.primalmagic.common.entities.projectiles.SpellMineEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Entity renderer for a spell mine.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class SpellMineRenderer extends EntityRenderer<SpellMineEntity> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/spell_projectile.png");
    protected static final RenderType TRANSLUCENT_TYPE = RenderType.getEntityTranslucent(TEXTURE);

    protected final SpellMineModel model = new SpellMineModel();

    public SpellMineRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }
    
    @Override
    public void render(SpellMineEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        @SuppressWarnings("deprecation")
        float yaw = MathHelper.rotLerp(entity.prevRotationYaw, entity.rotationYaw, partialTicks);
        float pitch = MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch);
        float ticks = (float)entity.ticksExisted + partialTicks;
        Color c = new Color(entity.getColor());
        float r = (float)c.getRed() / 255.0F;
        float g = (float)c.getGreen() / 255.0F;
        float b = (float)c.getBlue() / 255.0F;
        float alphaFactor = entity.isArmed() ? 0.25F : 1.0F;    // Fade out the mine if it's armed
        double bob = 0.25D * MathHelper.sin(ticks * 0.1F);      // Calculate a vertical bobbing displacement
        matrixStack.push();
        matrixStack.translate(0.0D, 0.5D + bob, 0.0D);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(MathHelper.sin(ticks * 0.1F) * 180.0F)); // Spin the mine like a shulker bullet
        matrixStack.rotate(Vector3f.XP.rotationDegrees(MathHelper.cos(ticks * 0.1F) * 180.0F));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(MathHelper.sin(ticks * 0.15F) * 360.0F));
        matrixStack.scale(-0.5F, -0.5F, 0.5F);
        this.model.setRotationAngles(entity, 0.0F, 0.0F, 0.0F, yaw, pitch);
        IVertexBuilder coreVertexBuilder = buffer.getBuffer(this.model.getRenderType(TEXTURE));
        this.model.render(matrixStack, coreVertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F * alphaFactor);  // Render the core of the mine
        matrixStack.scale(1.5F, 1.5F, 1.5F);
        IVertexBuilder glowVertexBuilder = buffer.getBuffer(TRANSLUCENT_TYPE);
        this.model.render(matrixStack, glowVertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, r, g, b, 0.5F * alphaFactor);  // Render the transparent glow of the mine
        matrixStack.pop();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getEntityTexture(SpellMineEntity entity) {
        return TEXTURE;
    }
}
