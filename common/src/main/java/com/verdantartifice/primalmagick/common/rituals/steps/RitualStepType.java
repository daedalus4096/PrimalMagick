package com.verdantartifice.primalmagick.common.rituals.steps;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualAltarTileEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.function.BiFunction;

public record RitualStepType<T extends AbstractRitualStep<T>>(Identifier id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec,
        BiFunction<RitualAltarTileEntity, T, Boolean> action) {
}
