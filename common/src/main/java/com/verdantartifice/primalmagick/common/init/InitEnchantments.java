package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.common.enchantments.effects.EnchantmentEntityEffectsPM;
import com.verdantartifice.primalmagick.common.enchantments.effects.EnchantmentLocationBasedEffectsPM;
import net.minecraft.core.registries.BuiltInRegistries;

public class InitEnchantments {
    public static void initEffects() {
        EnchantmentEntityEffectsPM.bootstrap(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE);
        EnchantmentLocationBasedEffectsPM.bootstrap(BuiltInRegistries.ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE);
    }
}
