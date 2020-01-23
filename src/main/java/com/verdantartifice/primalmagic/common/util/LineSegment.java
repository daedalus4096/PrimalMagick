package com.verdantartifice.primalmagic.common.util;

import javax.annotation.Nonnull;

import net.minecraft.util.math.Vec3d;

/**
 * Definition of a line segment between two points in 3D space.
 * 
 * @author Daedalus4096
 */
public class LineSegment {
    protected Vec3d start;
    protected Vec3d end;
    
    public LineSegment(Vec3d start, Vec3d end) {
        this.start = start;
        this.end = end;
    }
    
    public Vec3d getStart() {
        return this.start;
    }
    
    public Vec3d getEnd() {
        return this.end;
    }
    
    public Vec3d getMiddle() {
        // Calculate the midpoint of the line segment
        double x = (this.start.x + this.end.x) / 2.0D;
        double y = (this.start.y + this.end.y) / 2.0D;
        double z = (this.start.z + this.end.z) / 2.0D;
        return new Vec3d(x, y, z);
    }
    
    public Vec3d getDelta() {
        // Return what the end of the segment would be if it started at (0,0,0)
        return this.end.subtract(this.start);
    }
    
    public void perturb(@Nonnull Vec3d perturbStart, @Nonnull Vec3d perturbEnd) {
        // Displace the start and end of the line segment by the given deltas
        this.start = this.start.add(perturbStart);
        this.end = this.end.add(perturbEnd);
    }
}
