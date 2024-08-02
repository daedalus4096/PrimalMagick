package com.verdantartifice.primalmagick.common.books;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;

/**
 * Definition for a mod static book.
 * 
 * @author Daedalus4096
 */
public record BookDefinition(ResourceLocation bookId) {
    public static final Codec<BookDefinition> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("bookId").forGetter(BookDefinition::bookId)
        ).apply(instance, BookDefinition::new));
    public static final Codec<BookDefinition> NETWORK_CODEC = DIRECT_CODEC; // TODO Modify if some book data is not necessary on the client
    
    public static final Codec<Holder<BookDefinition>> HOLDER_CODEC = RegistryFixedCodec.create(RegistryKeysPM.BOOKS);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<BookDefinition>> STREAM_CODEC = ByteBufCodecs.holderRegistry(RegistryKeysPM.BOOKS);
}
