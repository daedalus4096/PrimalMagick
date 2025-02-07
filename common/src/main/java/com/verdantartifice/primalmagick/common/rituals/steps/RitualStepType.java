package com.verdantartifice.primalmagick.common.rituals.steps;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualAltarTileEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;

public record RitualStepType<T extends AbstractRitualStep<T>>(ResourceLocation id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, 
        BiFunction<RitualAltarTileEntity, T, Boolean> action) {
}
