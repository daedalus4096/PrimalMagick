package com.verdantartifice.primalmagick.common.enchantments.effects;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.Registry;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;

public class EnchantmentEntityEffectsPM {
    public static void bootstrap(Registry<MapCodec<? extends EnchantmentEntityEffect>> registry) {
        register(registry, "lifesteal", Lifesteal.CODEC);
    }
    
    private static void register(Registry<MapCodec<? extends EnchantmentEntityEffect>> registry, String name, MapCodec<? extends EnchantmentEntityEffect> codec) {
        Registry.register(registry, PrimalMagick.resource(name), codec);
    }
}
