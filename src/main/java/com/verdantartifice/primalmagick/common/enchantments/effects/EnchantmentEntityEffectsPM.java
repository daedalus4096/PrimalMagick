package com.verdantartifice.primalmagick.common.enchantments.effects;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.Registry;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;

public class EnchantmentEntityEffectsPM {
    public static void bootstrap(Registry<MapCodec<? extends EnchantmentEntityEffect>> registry) {
        register(registry, "lifesteal", Lifesteal.CODEC);
        register(registry, "apply_constant_mob_effect", ApplyConstantMobEffect.CODEC);
        register(registry, "apply_stacking_mob_effect", ApplyStackingMobEffect.CODEC);
    }
    
    private static void register(Registry<MapCodec<? extends EnchantmentEntityEffect>> registry, String name, MapCodec<? extends EnchantmentEntityEffect> codec) {
        Registry.register(registry, PrimalMagick.resource(name), codec);
    }
}
