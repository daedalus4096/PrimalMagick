package com.verdantartifice.primalmagick.common.books;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;

/**
 * Definition of an ancient culture.  Defines the loot table used to populate ancient libraries
 * for that culture.
 * 
 * @author Daedalus4096
 */
public record Culture(ResourceLocation cultureId, ResourceLocation lootTable) {
    public static final Codec<Culture> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("languageId").forGetter(Culture::cultureId),
            ResourceLocation.CODEC.fieldOf("lootTable").forGetter(Culture::lootTable)
        ).apply(instance, Culture::new));
    public static final Codec<Culture> NETWORK_CODEC = DIRECT_CODEC;    // TODO Modify if some culture data is not necessary on the client
}
