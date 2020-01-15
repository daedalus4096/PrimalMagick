package com.verdantartifice.primalmagic.client.fx.particles;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.util.LineSegment;
import com.verdantartifice.primalmagic.common.util.VectorUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
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
    protected static final float WIDTH = 3F;
    protected static final double MAX_DISPLACEMENT = 0.5D;
    protected static final int GENERATIONS = 6;
    
    protected final Vec3d delta;
    protected List<LineSegment> segmentList;
    
    protected SpellBoltParticle(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Vec3d target) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.delta = target.subtract(new Vec3d(x, y, z));
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.maxAge = 30;
        this.segmentList = this.calcSegments();
    }
    
    @Nonnull
    protected List<LineSegment> calcSegments() {
        List<LineSegment> retVal = new ArrayList<>();
        double curOffset = MAX_DISPLACEMENT;
        
        retVal.add(new LineSegment(Vec3d.ZERO, this.delta));
        for (int gen = 0; gen < GENERATIONS; gen++) {
            List<LineSegment> tempList = new ArrayList<>();
            for (LineSegment segment : retVal) {
                Vec3d midpoint = segment.getMiddle();
                midpoint = midpoint.add(VectorUtils.getRandomOrthogonalUnitVector(segment.getDelta(), this.world.rand).scale(curOffset));
                tempList.add(new LineSegment(segment.getStart(), midpoint));
                tempList.add(new LineSegment(midpoint, segment.getEnd()));
            }
            retVal = tempList;
            curOffset /= 2.0D;
        }
        return retVal;
    }
    
    @Override
    public void renderParticle(BufferBuilder buffer, ActiveRenderInfo entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        Minecraft mc = Minecraft.getInstance();
        mc.textureManager.bindTexture(TEXTURE);

        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GlStateManager.pushMatrix();
        GlStateManager.translated(this.posX - interpPosX, this.posY - interpPosY, this.posZ - interpPosZ);
        
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder bb = tess.getBuffer();
        bb.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX);
        
        GlStateManager.lineWidth(WIDTH);
        GlStateManager.color4f(this.particleRed, this.particleGreen, this.particleBlue, 1.0F);
        for (LineSegment segment : this.segmentList) {
            bb.pos(segment.getStart().x, segment.getStart().y, segment.getStart().z).tex(0.0D, 0.0D).endVertex();
            bb.pos(segment.getEnd().x, segment.getEnd().y, segment.getEnd().z).tex(1.0D, 1.0D).endVertex();
        }
        
        tess.draw();
        
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);

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
