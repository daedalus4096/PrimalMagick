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

/**
 * Particle type shown when casting a bolt-vehicle spell.
 * 
 * @author Michael Bunting
 */
@OnlyIn(Dist.CLIENT)
public class SpellBoltParticle extends Particle {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/particle/spell_bolt.png");
    protected static final float WIDTH = 6F;
    protected static final double MAX_DISPLACEMENT = 0.5D;
    protected static final double PERTURB_DISTANCE = 0.002D;
    protected static final int GENERATIONS = 5;
    
    protected final Vec3d delta;
    protected final List<LineSegment> segmentList;
    protected final List<Vec3d> perturbList;
    
    protected SpellBoltParticle(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Vec3d target) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.delta = target.subtract(new Vec3d(x, y, z));
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.maxAge = 10;
        this.segmentList = this.calcSegments();
        this.perturbList = this.calcPerturbs();
    }
    
    @Nonnull
    protected List<LineSegment> calcSegments() {
        List<LineSegment> retVal = new ArrayList<>();
        double curDisplacement = MAX_DISPLACEMENT;
        
        // Fractally generate a series of line segments, splitting each at the midpoint and adding an orthogonal displacement
        retVal.add(new LineSegment(Vec3d.ZERO, this.delta));
        for (int gen = 0; gen < GENERATIONS; gen++) {
            List<LineSegment> tempList = new ArrayList<>();
            for (LineSegment segment : retVal) {
                Vec3d midpoint = segment.getMiddle();
                midpoint = midpoint.add(VectorUtils.getRandomOrthogonalUnitVector(segment.getDelta(), this.world.rand).scale(curDisplacement));
                tempList.add(new LineSegment(segment.getStart(), midpoint));
                tempList.add(new LineSegment(midpoint, segment.getEnd()));
            }
            retVal = tempList;
            curDisplacement /= 2.0D;
        }
        return retVal;
    }
    
    @Nonnull
    protected List<Vec3d> calcPerturbs() {
        // Generate a perturbation vector for each point in the segment list, except for the start and end points
        List<Vec3d> retVal = new ArrayList<>();
        retVal.add(Vec3d.ZERO);
        for (LineSegment segment : this.segmentList) {
            retVal.add(segment.getEnd().equals(this.delta) ? Vec3d.ZERO : VectorUtils.getRandomUnitVector(this.world.rand).scale(PERTURB_DISTANCE * this.world.rand.nextDouble()));
        }
        return retVal;
    }
    
    @Override
    public void renderParticle(BufferBuilder buffer, ActiveRenderInfo entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        Minecraft.getInstance().textureManager.bindTexture(TEXTURE);

        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GlStateManager.pushMatrix();
        GlStateManager.translated(this.posX - interpPosX, this.posY - interpPosY, this.posZ - interpPosZ);
        
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder bb = tess.getBuffer();
        bb.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_TEX_COLOR);
        
        // Draw each line segment as an OpenGL line
        GlStateManager.lineWidth(WIDTH);
        for (int index = 0; index < this.segmentList.size(); index++) {
            LineSegment segment = this.segmentList.get(index);
            
            // Move the endpoints of each segment along their computed motion path before rendering to make the bolt move
            segment.perturb(this.perturbList.get(index), this.perturbList.get(index + 1));
            
            bb.pos(segment.getStart().x, segment.getStart().y, segment.getStart().z).tex(0.0D, 0.0D).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).endVertex();
            bb.pos(segment.getEnd().x, segment.getEnd().y, segment.getEnd().z).tex(1.0D, 1.0D).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).endVertex();
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
