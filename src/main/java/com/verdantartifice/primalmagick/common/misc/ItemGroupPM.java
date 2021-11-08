package com.verdantartifice.primalmagick.common.misc;

import com.verdantartifice.primalmagick.common.items.ItemsPM;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

/**
 * Definition of the mod's item group.  Creates a custom creative tab for this mod's items.
 * 
 * @author Daedalus4096
 */
public class ItemGroupPM extends CreativeModeTab {
    public ItemGroupPM() {
        super("primalmagic");
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ItemsPM.GRIMOIRE.get());
    }
}
