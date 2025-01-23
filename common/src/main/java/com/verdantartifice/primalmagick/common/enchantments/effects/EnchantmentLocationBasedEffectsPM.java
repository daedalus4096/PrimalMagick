package com.verdantartifice.primalmagick.common.enchantments.effects;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;

import java.util.function.Supplier;

public class EnchantmentLocationBasedEffectsPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.ENCHANTMENT_LOCATION_BASED_EFFECTS_REGISTRY.init();
    }

    public static final IRegistryItem<MapCodec<? extends EnchantmentLocationBasedEffect>, MapCodec<Lifesteal>> LIFESTEAL = register("lifesteal", () -> Lifesteal.CODEC);
    public static final IRegistryItem<MapCodec<? extends EnchantmentLocationBasedEffect>, MapCodec<ApplyConstantMobEffect>> APPLY_CONSTANT_MOB_EFFECT = register("apply_constant_mob_effect", () -> ApplyConstantMobEffect.CODEC);
    public static final IRegistryItem<MapCodec<? extends EnchantmentLocationBasedEffect>, MapCodec<ApplyStackingMobEffect>> APPLY_STACKING_MOB_EFFECT = register("apply_stacking_mob_effect", () -> ApplyStackingMobEffect.CODEC);

    private static <T extends EnchantmentLocationBasedEffect> IRegistryItem<MapCodec<? extends EnchantmentLocationBasedEffect>, MapCodec<T>> register(String name, Supplier<MapCodec<T>> supplier) {
        return Services.ENCHANTMENT_LOCATION_BASED_EFFECTS_REGISTRY.register(name, supplier);
    }
}
