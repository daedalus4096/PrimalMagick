package com.verdantartifice.primalmagick.common.enchantments.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of an enchantment effect that applies a given mob effect with the given duration
 * when attacked with the enchanted item.  If the effect is already present on the target, its
 * duration is refreshed and its amplifier increased.
 * 
 * @author Daedalus4096
 */
public record ApplyStackingMobEffect(Holder<MobEffect> toApply, LevelBasedValue duration, LevelBasedValue amplifierIncreasePerHit, LevelBasedValue maxAmplifier) implements EnchantmentEntityEffect {
    public static final MapCodec<ApplyStackingMobEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            MobEffect.CODEC.fieldOf("toApply").forGetter(ApplyStackingMobEffect::toApply),
            LevelBasedValue.CODEC.fieldOf("duration").forGetter(ApplyStackingMobEffect::duration),
            LevelBasedValue.CODEC.fieldOf("amplifierIncreasePerHit").forGetter(ApplyStackingMobEffect::amplifierIncreasePerHit),
            LevelBasedValue.CODEC.fieldOf("maxAmplifier").forGetter(ApplyStackingMobEffect::maxAmplifier)
        ).apply(instance, ApplyStackingMobEffect::new));
    
    @Override
    public void apply(ServerLevel pLevel, int pEnchantmentLevel, EnchantedItemInUse pItem, Entity pEntity, Vec3 pOrigin) {
        if (pEntity instanceof LivingEntity livingTarget) {
            MobEffectInstance effectInstance = livingTarget.getEffect(this.toApply);
            int duration = Math.round(this.duration.calculate(pEnchantmentLevel));
            int ampIncrease = Math.round(this.amplifierIncreasePerHit.calculate(pEnchantmentLevel));
            int maxAmp = Math.round(this.maxAmplifier.calculate(pEnchantmentLevel));
            int newAmplifier = (effectInstance == null) ? 0 : Mth.clamp(ampIncrease + effectInstance.getAmplifier(), 0, maxAmp - 1);
            livingTarget.addEffect(new MobEffectInstance(this.toApply, duration, newAmplifier));
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
