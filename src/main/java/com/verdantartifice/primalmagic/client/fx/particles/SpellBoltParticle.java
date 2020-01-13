package com.verdantartifice.primalmagic.client.fx.particles;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpellBoltParticle extends Particle {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/particle/spell_bolt.png");
    protected static final float WIDTH = 0.66F;
    
    protected final Vec3d source;
    protected final Vec3d target;
    
    protected SpellBoltParticle(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Vec3d target) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.source = new Vec3d(x, y, z);
        this.target = target;
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.maxAge = 30;
    }
    
    @Override
    public void renderParticle(BufferBuilder buffer, ActiveRenderInfo entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        Minecraft mc = Minecraft.getInstance();
        mc.textureManager.bindTexture(TEXTURE);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(516, 0.003921569F);
        
        GlStateManager.pushMatrix();
        GlStateManager.translated(this.target.x, this.target.y, this.target.z);
        GlStateManager.color4f(this.particleRed, this.particleGreen, this.particleBlue, 1.0F);
        
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder bb = tess.getBuffer();
        bb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        
        bb.pos(-WIDTH, WIDTH, WIDTH).tex(0.0D, 1.0D).endVertex();
        bb.pos(-WIDTH, -WIDTH, WIDTH).tex(0.0D, 0.0D).endVertex();
        bb.pos(WIDTH, -WIDTH, WIDTH).tex(1.0D, 0.0D).endVertex();
        bb.pos(WIDTH, WIDTH, WIDTH).tex(1.0D, 1.0D).endVertex();
        
        bb.pos(-WIDTH, WIDTH, -WIDTH).tex(0.0D, 1.0D).endVertex();
        bb.pos(WIDTH, WIDTH, -WIDTH).tex(1.0D, 1.0D).endVertex();
        bb.pos(WIDTH, -WIDTH, -WIDTH).tex(1.0D, 0.0D).endVertex();
        bb.pos(-WIDTH, -WIDTH, -WIDTH).tex(0.0D, 0.0D).endVertex();
        
        bb.pos(WIDTH, WIDTH, -WIDTH).tex(0.0D, 1.0D).endVertex();
        bb.pos(WIDTH, WIDTH, WIDTH).tex(1.0D, 1.0D).endVertex();
        bb.pos(WIDTH, -WIDTH, WIDTH).tex(1.0D, 0.0D).endVertex();
        bb.pos(WIDTH, -WIDTH, -WIDTH).tex(0.0D, 0.0D).endVertex();
        
        bb.pos(-WIDTH, -WIDTH, WIDTH).tex(1.0D, 0.0D).endVertex();
        bb.pos(-WIDTH, WIDTH, WIDTH).tex(1.0D, 1.0D).endVertex();
        bb.pos(-WIDTH, WIDTH, -WIDTH).tex(0.0D, 1.0D).endVertex();
        bb.pos(-WIDTH, -WIDTH, -WIDTH).tex(0.0D, 0.0D).endVertex();
        
        bb.pos(WIDTH, WIDTH, -WIDTH).tex(1.0D, 0.0D).endVertex();
        bb.pos(-WIDTH, WIDTH, -WIDTH).tex(0.0D, 0.0D).endVertex();
        bb.pos(-WIDTH, WIDTH, WIDTH).tex(0.0D, 1.0D).endVertex();
        bb.pos(WIDTH, WIDTH, WIDTH).tex(1.0D, 1.0D).endVertex();
        
        bb.pos(WIDTH, -WIDTH, -WIDTH).tex(1.0D, 0.0D).endVertex();
        bb.pos(WIDTH, -WIDTH, WIDTH).tex(1.0D, 1.0D).endVertex();
        bb.pos(-WIDTH, -WIDTH, WIDTH).tex(0.0D, 1.0D).endVertex();
        bb.pos(-WIDTH, -WIDTH, -WIDTH).tex(0.0D, 0.0D).endVertex();
        
        tess.draw();
        
        GlStateManager.popMatrix();
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.CUSTOM;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<SpellBoltParticleData> {
        public Factory(IAnimatedSprite spriteSet) {}
        
        @Override
        public Particle makeParticle(SpellBoltParticleData typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SpellBoltParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getTargetVec());
        }
    }
}
