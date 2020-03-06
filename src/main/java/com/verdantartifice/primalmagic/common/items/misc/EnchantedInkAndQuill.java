package com.verdantartifice.primalmagic.common.items.misc;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.theorycrafting.IWritingImplement;

import net.minecraft.item.Item;

/**
 * Item definition for an enchanted ink and quill.  Serves as a writing implement on a research table.
 * 
 * @author Daedalus4096
 */
public class EnchantedInkAndQuill extends Item implements IWritingImplement {
    public EnchantedInkAndQuill() {
        super(new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxDamage(63));
    }

    @Override
    public boolean isDamagedOnUse() {
        return true;
    }
}
