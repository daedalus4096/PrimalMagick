package com.verdantartifice.primalmagick.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public record IdentifiedScoreEntry(Identifier id, int score) {
    public static final Codec<IdentifiedScoreEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("id").forGetter(IdentifiedScoreEntry::id),
            Codec.INT.fieldOf("score").forGetter(IdentifiedScoreEntry::score)
    ).apply(instance, IdentifiedScoreEntry::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, IdentifiedScoreEntry> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, IdentifiedScoreEntry::id,
            ByteBufCodecs.VAR_INT, IdentifiedScoreEntry::score,
            IdentifiedScoreEntry::new);
}
