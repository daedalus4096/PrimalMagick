package com.verdantartifice.primalmagic.common.util;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.util.math.Vec3d;

public class VectorUtils {
    public static Vec3d getRandomUnitVector(@Nonnull Random rng) {
        return new Vec3d(rng.nextGaussian(), rng.nextGaussian(), rng.nextGaussian()).normalize();
    }
    
    public static Vec3d getRandomOrthogonalUnitVector(@Nonnull Vec3d vec, @Nonnull Random rng) {
        // Generate a random other vector
        Vec3d other = getRandomUnitVector(rng);
        
        // Ensure that the given vector and the other vector are not co-linear
        Vec3d normVec = vec.normalize();
        if (other.equals(normVec) || other.equals(normVec.scale(-1.0D))) {
            other = (other.y == 0.0D && other.z == 0.0D) ? other.add(0, 1, 0) : other.add(1, 0, 0);
        }
        
        // Return the normalized cross-product of the given vector and the other vector
        return other.crossProduct(normVec).normalize();
    }
}
