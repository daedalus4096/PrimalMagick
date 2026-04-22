package com.verdantartifice.primalmagick.common.effects;

import com.verdantartifice.primalmagick.common.damagesource.DamageSourcesPM;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Definition for a potion effect type that does armor-ignoring damage over time.  Not effective
 * against the undead.
 * 
 * @author Daedalus4096
 */
public class BleedingEffect extends MobEffect {
    public BleedingEffect(MobEffectCategory typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    @Override
    public boolean applyEffectTick(@NotNull ServerLevel level, @NotNull LivingEntity entityLivingBaseIn, int amplifier) {
        entityLivingBaseIn.hurtServer(level, DamageSourcesPM.bleeding(level.registryAccess()), (float)(1 << Math.max(0, amplifier)));
        return true;
    }
    
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return (duration % 40 == 0);
    }
}
