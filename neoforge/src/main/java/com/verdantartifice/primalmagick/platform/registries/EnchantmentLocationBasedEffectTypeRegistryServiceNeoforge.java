package com.verdantartifice.primalmagick.platform.registries;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.platform.services.registries.IEnchantmentLocationBasedEffectTypeRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class EnchantmentLocationBasedEffectTypeRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<MapCodec<? extends EnchantmentLocationBasedEffect>> implements IEnchantmentLocationBasedEffectTypeRegistryService {
    private static final DeferredRegister<MapCodec<? extends EnchantmentLocationBasedEffect>> EFFECTS = DeferredRegister.create(Registries.ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE, Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<MapCodec<? extends EnchantmentLocationBasedEffect>>> getDeferredRegisterSupplier() {
        return () -> EFFECTS;
    }

    @Override
    protected Registry<MapCodec<? extends EnchantmentLocationBasedEffect>> getRegistry() {
        return BuiltInRegistries.ENCHANTMENT_LOCATION_BASED_EFFECT_TYPE;
    }
}
