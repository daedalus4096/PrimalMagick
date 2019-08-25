package com.verdantartifice.primalmagic.common.items.base;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class BlockItemPM extends BlockItem {
    public BlockItemPM(Block block) {
        this(block, new Item.Properties().group(PrimalMagic.ITEM_GROUP));
    }
    
    public BlockItemPM(Block block, Item.Properties props) {
        super(block, props);
        this.setRegistryName(block.getRegistryName());
    }
}
