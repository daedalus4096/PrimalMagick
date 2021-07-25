package com.verdantartifice.primalmagic.common.items.concoctions;

import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Definition of a bomb casing.  Used as a base item for making alchemical bombs.
 * 
 * @author Daedalus4096
 */
public class BombCasingItem extends AbstractConcoctionContainerItem {
    public BombCasingItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    protected ItemStack getConcoctionContainerItem() {
        return ItemsPM.ALCHEMICAL_BOMB.get().getDefaultInstance().copy();
    }
}
