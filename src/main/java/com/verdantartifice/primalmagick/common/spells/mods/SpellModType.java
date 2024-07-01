package com.verdantartifice.primalmagick.common.spells.mods;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record SpellModType<T extends AbstractSpellMod<T>>(ResourceLocation id, Supplier<SpellProperty> tiebreaker, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
}
