package com.verdantartifice.primalmagick.platform.services.registries;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;

public interface IEnchantmentLocationBasedEffectTypeRegistryService extends IRegistryService<MapCodec<? extends EnchantmentLocationBasedEffect>> {
}
