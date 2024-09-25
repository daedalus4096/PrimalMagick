package com.verdantartifice.primalmagick.common.tiles.base;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Interface marking a tile entity as one that can have its contents randomized upon first
 * interaction by assigning a loot table to it.
 * 
 * @author Daedalus4096
 */
public interface IRandomizableContents {
    void setLootTable(ResourceKey<LootTable> lootTable, long lootTableSeed);
    void unpackLootTable(@Nullable Player player);
}
