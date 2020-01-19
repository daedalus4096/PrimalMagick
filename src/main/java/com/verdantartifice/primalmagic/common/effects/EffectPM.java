package com.verdantartifice.primalmagic.common.effects;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

/**
 * Extension of the vanilla effect type with a public constructor.
 * 
 * @author Daedalus4096
 */
public class EffectPM extends Effect {
    // Super constructor is protected, so expose it
    public EffectPM(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }
}
