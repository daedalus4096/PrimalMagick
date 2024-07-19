package com.verdantartifice.primalmagick.client.fx.particles;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.verdantartifice.primalmagick.client.renderers.types.ThickLinesRenderType;
import com.verdantartifice.primalmagick.common.util.LineSegment;
import com.verdantartifice.primalmagick.common.util.VectorUtils;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;

/**
 * Particle type shown when casting a bolt-vehicle spell.
 * 
 * @author Daedalus4096
 */
public class SpellBoltParticle extends Particle {
    protected static final float WIDTH = 6F;
    protected static final double MAX_DISPLACEMENT = 0.5D;
    protected static final double PERTURB_DISTANCE = 0.002D;
    protected static final int GENERATIONS = 5;
    
    protected final Vec3 delta;
    protected final List<LineSegment> segmentList;
    protected final List<Vec3> perturbList;
    
    protected SpellBoltParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Vec3 target) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.delta = target.subtract(new Vec3(x, y, z));
        this.xd = 0.0D;
        this.yd = 0.0D;
        this.zd = 0.0D;
        this.lifetime = 10;
        this.segmentList = this.calcSegments();
        this.perturbList = this.calcPerturbs();
    }
    
    @Nonnull
    protected List<LineSegment> calcSegments() {
        List<LineSegment> retVal = new ArrayList<>();
        double curDisplacement = MAX_DISPLACEMENT;
        
        // Fractally generate a series of line segments, splitting each at the midpoint and adding an orthogonal displacement
        retVal.add(new LineSegment(Vec3.ZERO, this.delta));
        for (int gen = 0; gen < GENERATIONS; gen++) {
            List<LineSegment> tempList = new ArrayList<>();
            for (LineSegment segment : retVal) {
                Vec3 midpoint = segment.getMiddle();
                midpoint = midpoint.add(VectorUtils.getRandomOrthogonalUnitVector(segment.getDelta(), this.level.random).scale(curDisplacement));
                tempList.add(new LineSegment(segment.getStart(), midpoint));
                tempList.add(new LineSegment(midpoint, segment.getEnd()));
            }
            retVal = tempList;
            curDisplacement /= 2.0D;
        }
        return retVal;
    }
    
    @Nonnull
    protected List<Vec3> calcPerturbs() {
        // Generate a perturbation vector for each point in the segment list, except for the start and end points
        List<Vec3> retVal = new ArrayList<>();
        retVal.add(Vec3.ZERO);
        for (LineSegment segment : this.segmentList) {
            retVal.add(segment.getEnd().equals(this.delta) ? Vec3.ZERO : VectorUtils.getRandomUnitVector(this.level.random).scale(PERTURB_DISTANCE * this.level.random.nextDouble()));
        }
        return retVal;
    }
    
    @Override
    public void render(VertexConsumer builder, Camera entityIn, float partialTicks) {
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        RenderSystem.disableCull();

        PoseStack stack = RenderSystem.getModelViewStack();
        stack.pushPose();
        stack.translate(this.x - entityIn.getPosition().x, this.y - entityIn.getPosition().y, this.z - entityIn.getPosition().z);

        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer lineBuilder = buffer.getBuffer(ThickLinesRenderType.THICK_LINES);
        
        // Draw each line segment
        for (int index = 0; index < this.segmentList.size(); index++) {
            LineSegment segment = this.segmentList.get(index);
            
            // Move the endpoints of each segment along their computed motion path before rendering to make the bolt move
            segment.perturb(this.perturbList.get(index), this.perturbList.get(index + 1));
            
            lineBuilder.addVertex((float)segment.getStart().x, (float)segment.getStart().y, (float)segment.getStart().z).setColor(this.rCol, this.gCol, this.bCol, 0.5F);
            lineBuilder.addVertex((float)segment.getEnd().x, (float)segment.getEnd().y, (float)segment.getEnd().z).setColor(this.rCol, this.gCol, this.bCol, 0.5F);
        }
        buffer.endBatch(ThickLinesRenderType.THICK_LINES);
        
        RenderSystem.enableCull();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);

        stack.popPose();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public static class Factory implements ParticleProvider<SpellBoltParticleData> {
        public Factory(SpriteSet spriteSet) {}
        
        @Override
        public Particle createParticle(SpellBoltParticleData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SpellBoltParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getTargetVec());
        }
    }
}
