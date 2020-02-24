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
        entityLivingBaseIn.attackEntityFrom(DamageSourcesPM.BLEEDING, 1.0F);
    }
    
    @Override
    public boolean isReady(int duration, int amplifier) {
        int ticks = 40 >> amplifier;
        return (ticks > 0) ? (duration % ticks == 0) : true;
    }
}
