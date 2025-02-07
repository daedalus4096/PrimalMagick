package com.verdantartifice.primalmagick.common.enchantments.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of an enchantment effect that applies a given mob effect with the given duration and
 * amplifier when attacked with the enchanted item.  Differs from Vanilla's ApplyMobEffect in that
 * the effect, duration, and amplifier are not randomized.
 * 
 * @author Daedalus4096
 */
public record ApplyConstantMobEffect(Holder<MobEffect> toApply, LevelBasedValue duration, LevelBasedValue amplifier) implements EnchantmentEntityEffect {
    public static final MapCodec<ApplyConstantMobEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            MobEffect.CODEC.fieldOf("toApply").forGetter(ApplyConstantMobEffect::toApply),
            LevelBasedValue.CODEC.fieldOf("duration").forGetter(ApplyConstantMobEffect::duration),
            LevelBasedValue.CODEC.fieldOf("amplifier").forGetter(ApplyConstantMobEffect::amplifier)
        ).apply(instance, ApplyConstantMobEffect::new));
    
    @Override
    public void apply(ServerLevel pLevel, int pEnchantmentLevel, EnchantedItemInUse pItem, Entity pEntity, Vec3 pOrigin) {
        if (pEntity instanceof LivingEntity livingTarget) {
            livingTarget.addEffect(new MobEffectInstance(this.toApply, Math.round(this.duration.calculate(pEnchantmentLevel)), Math.round(this.amplifier.calculate(pEnchantmentLevel))));
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
