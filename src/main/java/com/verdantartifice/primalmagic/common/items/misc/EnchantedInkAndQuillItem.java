package com.verdantartifice.primalmagic.common.items.misc;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.theorycrafting.IWritingImplement;

import net.minecraft.world.item.Item;

/**
 * Item definition for an enchanted ink and quill.  Serves as a writing implement on a research table.
 * 
 * @author Daedalus4096
 */
public class EnchantedInkAndQuillItem extends Item implements IWritingImplement {
    public EnchantedInkAndQuillItem() {
        super(new Item.Properties().tab(PrimalMagic.ITEM_GROUP).durability(63));
    }

    @Override
    public boolean isDamagedOnUse() {
        return true;
    }
}
