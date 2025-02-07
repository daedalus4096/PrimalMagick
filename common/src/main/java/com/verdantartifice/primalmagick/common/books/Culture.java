package com.verdantartifice.primalmagick.common.books;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Definition of an ancient culture.  Defines the loot table used to populate ancient libraries
 * for that culture.
 * 
 * @author Daedalus4096
 */
public record Culture(ResourceLocation cultureId, ResourceKey<LootTable> shelfLootTable, ResourceKey<LootTable> welcomeLootTable, ResourceKey<LootTable> hiddenLootTable, BlockState accentBlockState) {
    public static final Codec<Culture> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("languageId").forGetter(Culture::cultureId),
            ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("shelfLootTable").forGetter(Culture::shelfLootTable),
            ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("welcomeLootTable").forGetter(Culture::welcomeLootTable),
            ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("hiddenLootTable").forGetter(Culture::hiddenLootTable),
            BlockState.CODEC.fieldOf("accentBlockState").forGetter(Culture::accentBlockState)
        ).apply(instance, Culture::new));
    public static final Codec<Culture> NETWORK_CODEC = DIRECT_CODEC;    // TODO Modify if some culture data is not necessary on the client
}
