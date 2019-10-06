package com.verdantartifice.primalmagic.common.items.wands;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.itemstack.MundaneWandTEISR;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MundaneWandItem extends AbstractWandItem implements IWand {
    public MundaneWandItem() {
        super(new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).setTEISR(() -> MundaneWandTEISR::new));
        this.setRegistryName(PrimalMagic.MODID, "mundane_wand");
    }

    @Override
    public int getMaxMana(ItemStack stack) {
        return 25;
    }
}
