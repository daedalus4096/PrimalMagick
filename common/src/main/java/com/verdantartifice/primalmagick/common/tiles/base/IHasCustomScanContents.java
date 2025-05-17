package com.verdantartifice.primalmagick.common.tiles.base;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

/**
 * Interface denoting a block entity that has a custom list of contents which should be scanned when the block entity is
 * scanned by the Arcanometer.  Used in situations where the block entity does not or cannot implement a standard item
 * handler capability.
 */
public interface IHasCustomScanContents {
    NonNullList<ItemStack> getCustomScanContents();
}
