package com.verdantartifice.primalmagick.common.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * Extension of the vanilla effect type with a public constructor.
 * 
 * @author Daedalus4096
 */
public class EffectPM extends MobEffect {
    // Super constructor is protected, so expose it
    public EffectPM(MobEffectCategory typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }
}
