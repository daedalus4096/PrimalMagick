package com.verdantartifice.primalmagick.common.enchantments.effects;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.PrimalMagick;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.Registry;
import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;

public class EnchantmentLocationBasedEffectsPM {
    public static void bootstrap(Registry<MapCodec<? extends EnchantmentLocationBasedEffect>> registry) {
        register(registry, "lifesteal", Lifesteal.CODEC);
        register(registry, "apply_constant_mob_effect", ApplyConstantMobEffect.CODEC);
        register(registry, "apply_stacking_mob_effect", ApplyStackingMobEffect.CODEC);
    }
    
    private static void register(Registry<MapCodec<? extends EnchantmentLocationBasedEffect>> registry, String name, MapCodec<? extends EnchantmentLocationBasedEffect> codec) {
        Registry.register(registry, ResourceUtils.loc(name), codec);
    }
}
