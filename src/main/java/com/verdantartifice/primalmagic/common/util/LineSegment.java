package com.verdantartifice.primalmagic.common.util;

import javax.annotation.Nonnull;

import net.minecraft.util.math.vector.Vector3d;

/**
 * Definition of a line segment between two points in 3D space.
 * 
 * @author Daedalus4096
 */
public class LineSegment {
    protected Vector3d start;
    protected Vector3d end;
    
    public LineSegment(Vector3d start, Vector3d end) {
        this.start = start;
        this.end = end;
    }
    
    public Vector3d getStart() {
        return this.start;
    }
    
    public Vector3d getEnd() {
        return this.end;
    }
    
    public Vector3d getMiddle() {
        // Calculate the midpoint of the line segment
        double x = (this.start.x + this.end.x) / 2.0D;
        double y = (this.start.y + this.end.y) / 2.0D;
        double z = (this.start.z + this.end.z) / 2.0D;
        return new Vector3d(x, y, z);
    }
    
    public Vector3d getDelta() {
        // Return what the end of the segment would be if it started at (0,0,0)
        return this.end.subtract(this.start);
    }
    
    public void perturb(@Nonnull Vector3d perturbStart, @Nonnull Vector3d perturbEnd) {
        // Displace the start and end of the line segment by the given deltas
        this.start = this.start.add(perturbStart);
        this.end = this.end.add(perturbEnd);
    }
}
