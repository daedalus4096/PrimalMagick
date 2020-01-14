package com.verdantartifice.primalmagic.common.util;

import net.minecraft.util.math.Vec3d;

public class LineSegment {
    protected final Vec3d start;
    protected final Vec3d end;
    
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
        double x = (this.start.x + this.end.x) / 2.0D;
        double y = (this.start.y + this.end.y) / 2.0D;
        double z = (this.start.z + this.end.z) / 2.0D;
        return new Vec3d(x, y, z);
    }
    
    public Vec3d getDelta() {
        return this.end.subtract(this.start);
    }
}
