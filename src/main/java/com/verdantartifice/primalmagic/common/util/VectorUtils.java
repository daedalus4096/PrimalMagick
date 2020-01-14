package com.verdantartifice.primalmagic.common.util;

import java.util.Random;

import net.minecraft.util.math.Vec3d;

public class VectorUtils {
    public static Vec3d getRandomUnitVector(Random rng) {
        return new Vec3d(rng.nextGaussian(), rng.nextGaussian(), rng.nextGaussian()).normalize();
    }
}
