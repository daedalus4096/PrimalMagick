package com.verdantartifice.primalmagick.common.affinities;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

/**
 * Type of affinity entry.
 * 
 * @author Daedalus4096
 */
public record AffinityType<T extends AbstractAffinity<T>>(Identifier id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, String folder) {
}
