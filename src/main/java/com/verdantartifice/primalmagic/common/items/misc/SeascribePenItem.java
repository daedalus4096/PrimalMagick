package com.verdantartifice.primalmagic.common.items.misc;

import com.verdantartifice.primalmagic.common.theorycrafting.IWritingImplement;

import net.minecraft.item.Item;

/**
 * Item definition for a seascribe pen.  Serves as a writing implement on a research table.  Has
 * an infinite amount of ink.
 * 
 * @author Daedalus4096
 */
public class SeascribePenItem extends Item implements IWritingImplement {
    public SeascribePenItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isDamagedOnUse() {
        return false;
    }
}
