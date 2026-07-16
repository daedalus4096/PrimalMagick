package com.verdantartifice.primalmagick.client.fx.particles;

import com.verdantartifice.primalmagick.common.util.LineSegment;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.util.VectorUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Particle type shown when casting a bolt-vehicle spell.
 * 
 * @author Daedalus4096
 */
public class SpellBoltParticle extends Particle {
    public static final ParticleRenderType RENDER_TYPE = new ParticleRenderType(ResourceUtils.loc("spell_bolt").toString());

    protected static final double MAX_DISPLACEMENT = 0.5D;
    protected static final double PERTURB_DISTANCE = 0.002D;
    protected static final int GENERATIONS = 5;

    protected final Vec3 start;
    protected final Vec3 delta;
    protected final int color;
    
    protected SpellBoltParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Vec3 target, int color) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.start = new Vec3(x, y, z);
        this.delta = target.subtract(this.start);
        this.xd = 0.0D;
        this.yd = 0.0D;
        this.zd = 0.0D;
        this.lifetime = 10;
        this.color = color;
    }
    
    @NotNull
    protected static List<LineSegment> calcSegments(Vec3 delta, RandomSource random) {
        List<LineSegment> retVal = new ArrayList<>();
        double curDisplacement = MAX_DISPLACEMENT;
        
        // Fractally generate a series of line segments, splitting each at the midpoint and adding an orthogonal displacement
        retVal.add(new LineSegment(Vec3.ZERO, delta));
        for (int gen = 0; gen < GENERATIONS; gen++) {
            List<LineSegment> tempList = new ArrayList<>();
            for (LineSegment segment : retVal) {
                Vec3 midpoint = segment.getMiddle();
                midpoint = midpoint.add(VectorUtils.getRandomOrthogonalUnitVector(segment.getDelta(), random).scale(curDisplacement));
                tempList.add(new LineSegment(segment.getStart(), midpoint));
                tempList.add(new LineSegment(midpoint, segment.getEnd()));
            }
            retVal = tempList;
            curDisplacement /= 2.0D;
        }
        return retVal;
    }
    
    @NotNull
    protected static List<Vec3> calcPerturbs(List<LineSegment> segmentList, Vec3 delta, RandomSource random) {
        // Generate a perturbation vector for each point in the segment list, except for the start and end points
        List<Vec3> retVal = new ArrayList<>();
        retVal.add(Vec3.ZERO);
        for (LineSegment segment : segmentList) {
            retVal.add(segment.getEnd().equals(delta) ? Vec3.ZERO : VectorUtils.getRandomUnitVector(random).scale(PERTURB_DISTANCE * random.nextDouble()));
        }
        return retVal;
    }

    @Override
    @NotNull
    public ParticleRenderType getGroup() {
        return RENDER_TYPE;
    }

    public RenderState getRenderState() {
        List<LineSegment> segmentList = calcSegments(this.delta, this.random);
        List<Vec3> perturbList = calcPerturbs(segmentList, this.delta, this.random);
        return new RenderState(this.start, segmentList, perturbList, this.color);
    }

    /**
     * @param start the start point of the bolt in world space
     * @param segmentList the list of line segments composing the bolt
     * @param perturbList the list of perturbation vectors for the bolt to give the appearance of crackling motion
     * @param color the color of the bolt's line segments
     */
    public record RenderState(Vec3 start, List<LineSegment> segmentList, List<Vec3> perturbList, int color) {}

    public static class Provider implements ParticleProvider<SpellBoltParticleData> {
        @Override
        public Particle createParticle(@NotNull SpellBoltParticleData options, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, @NotNull RandomSource randomSource) {
            return new SpellBoltParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, options.target(), options.color());
        }
    }
}
