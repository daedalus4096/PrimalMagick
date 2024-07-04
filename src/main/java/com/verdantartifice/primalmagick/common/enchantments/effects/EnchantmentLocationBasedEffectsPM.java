package com.verdantartifice.primalmagick.common.enchantments.effects;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.Registry;
import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;

public class EnchantmentLocationBasedEffectsPM {
    public static void bootstrap(Registry<MapCodec<? extends EnchantmentLocationBasedEffect>> registry) {
        register(registry, "lifesteal", Lifesteal.CODEC);
        register(registry, "apply_constant_mob_effect", ApplyConstantMobEffect.CODEC);
    }
    
    private static void register(Registry<MapCodec<? extends EnchantmentLocationBasedEffect>> registry, String name, MapCodec<? extends EnchantmentLocationBasedEffect> codec) {
        Registry.register(registry, PrimalMagick.resource(name), codec);
    }
}
