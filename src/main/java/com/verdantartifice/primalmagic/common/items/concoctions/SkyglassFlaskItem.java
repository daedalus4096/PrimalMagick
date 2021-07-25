package com.verdantartifice.primalmagic.common.items.concoctions;

import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Definition of a skyglass flask.  Used as a base item for making tinctures, philters, or elixirs.
 * 
 * @author Daedalus4096
 */
public class SkyglassFlaskItem extends AbstractConcoctionContainerItem {
    public SkyglassFlaskItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    protected ItemStack getConcoctionContainerItem() {
        return ItemsPM.CONCOCTION.get().getDefaultInstance().copy();
    }
}
