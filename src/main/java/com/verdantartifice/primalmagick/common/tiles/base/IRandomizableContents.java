package com.verdantartifice.primalmagick.common.tiles.base;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Interface marking a tile entity as one that can have its contents randomized upon first
 * interaction by assigning a loot table to it.
 * 
 * @author Daedalus4096
 */
public interface IRandomizableContents {
    void setLootTable(ResourceLocation lootTable, long lootTableSeed);
    void unpackLootTable(@Nullable Player player);
}
