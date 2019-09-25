package com.verdantartifice.primalmagic.common.misc;

import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ItemGroupPM extends ItemGroup {
    public ItemGroupPM() {
        super("primalmagic");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemsPM.GRIMOIRE);
    }
}
