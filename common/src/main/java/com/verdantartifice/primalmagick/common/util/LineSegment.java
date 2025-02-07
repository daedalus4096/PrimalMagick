package com.verdantartifice.primalmagick.common.util;

import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

/**
 * Definition of a line segment between two points in 3D space.
 * 
 * @author Daedalus4096
 */
public class LineSegment {
    protected Vec3 start;
    protected Vec3 end;
    
    public LineSegment(Vec3 start, Vec3 end) {
        this.start = start;
        this.end = end;
    }
    
    public Vec3 getStart() {
        return this.start;
    }
    
    public Vec3 getEnd() {
        return this.end;
    }
    
    public Vec3 getMiddle() {
        // Calculate the midpoint of the line segment
        double x = (this.start.x + this.end.x) / 2.0D;
        double y = (this.start.y + this.end.y) / 2.0D;
        double z = (this.start.z + this.end.z) / 2.0D;
        return new Vec3(x, y, z);
    }
    
    public Vec3 getDelta() {
        // Return what the end of the segment would be if it started at (0,0,0)
        return this.end.subtract(this.start);
    }
    
    public void perturb(@Nonnull Vec3 perturbStart, @Nonnull Vec3 perturbEnd) {
        // Displace the start and end of the line segment by the given deltas
        this.start = this.start.add(perturbStart);
        this.end = this.end.add(perturbEnd);
    }
}
