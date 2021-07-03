package com.verdantartifice.primalmagic.common.effects;

import com.verdantartifice.primalmagic.common.misc.DamageSourcesPM;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

/**
 * Definition for a potion effect type that does armor-ignoring damage over time.  Not effective
 * against the undead.
 * 
 * @author Daedalus4096
 */
public class BleedingEffect extends Effect {
    public BleedingEffect(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        entityLivingBaseIn.attackEntityFrom(DamageSourcesPM.BLEEDING, (float)(1 << Math.max(0, amplifier)));
    }
    
    @Override
    public boolean isReady(int duration, int amplifier) {
        return (duration % 40 == 0);
    }
}
