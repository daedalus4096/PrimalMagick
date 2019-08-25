package com.verdantartifice.primalmagic.common.misc;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ItemGroupPM extends ItemGroup {
    public ItemGroupPM() {
        super("primalmagic");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(BlocksPM.MARBLE_RAW);
    }
}
