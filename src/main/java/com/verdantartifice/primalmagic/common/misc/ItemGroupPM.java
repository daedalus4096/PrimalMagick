package com.verdantartifice.primalmagic.common.misc;

import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/**
 * Definition of the mod's item group.  Creates a custom creative tab for this mod's items.
 * 
 * @author Daedalus4096
 */
public class ItemGroupPM extends ItemGroup {
    public ItemGroupPM() {
        super("primalmagic");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemsPM.GRIMOIRE.get());
    }
}
