package com.verdantartifice.primalmagick.common.enchantments.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of an enchantment effect that has a chance to heal the item's wielder upon dealing damage with it.
 * 
 * @author Daedalus4096
 */
public record Lifesteal(LevelBasedValue chance) implements EnchantmentEntityEffect {
    public static final MapCodec<Lifesteal> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LevelBasedValue.CODEC.fieldOf("chance").forGetter(Lifesteal::chance)
        ).apply(instance, Lifesteal::new));

    @Override
    public void apply(ServerLevel pLevel, int pEnchantmentLevel, EnchantedItemInUse pItem, Entity pEntity, Vec3 pOrigin) {
        if (pEntity instanceof LivingEntity livingTarget) {
            RandomSource rng = livingTarget.getRandom();
            if (pItem.owner() != null && rng.nextFloat() < this.chance.calculate(pEnchantmentLevel)) {
                pItem.owner().heal(1F);
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
