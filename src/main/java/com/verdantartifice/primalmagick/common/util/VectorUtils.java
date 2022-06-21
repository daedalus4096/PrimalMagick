package com.verdantartifice.primalmagick.common.util;

import javax.annotation.Nonnull;

import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

/**
 * Collection of utility methods pertaining to 3D vectors.
 * 
 * @author Daedalus4096
 */
public class VectorUtils {
    /**
     * Compute a random unit vector in 3D space.
     * 
     * @param rng the random number generator to use
     * @return a random unit vector in 3D space
     */
    public static Vec3 getRandomUnitVector(@Nonnull RandomSource rng) {
        return new Vec3(rng.nextGaussian(), rng.nextGaussian(), rng.nextGaussian()).normalize();
    }
    
    /**
     * Generate a random unit vector that is orthogonal to the given 3D vector.
     * 
     * @param vec the initial vector
     * @param rng the random number generator to use
     * @return a random unit vector that is orthogonal to the given 3D vector
     */
    public static Vec3 getRandomOrthogonalUnitVector(@Nonnull Vec3 vec, @Nonnull RandomSource rng) {
        // Generate a random other vector
        Vec3 other = getRandomUnitVector(rng);
        
        // Ensure that the given vector and the other vector are not co-linear
        Vec3 normVec = vec.normalize();
        if (other.equals(normVec) || other.equals(normVec.scale(-1.0D))) {
            other = (other.y == 0.0D && other.z == 0.0D) ? other.add(0, 1, 0) : other.add(1, 0, 0);
        }
        
        // Return the normalized cross-product of the given vector and the other vector
        return other.cross(normVec).normalize();
    }
}
