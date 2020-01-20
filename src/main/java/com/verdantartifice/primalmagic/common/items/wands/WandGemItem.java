package com.verdantartifice.primalmagic.common.items.wands;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.wands.WandGem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;

/**
 * Item definition for a wand gem.  May be used to construct modular wands.
 * 
 * @author Daedalus4096
 */
public class WandGemItem extends Item {
    protected final WandGem gem;

    public WandGemItem(WandGem gem, Properties properties) {
        super(properties);
        this.gem = gem;
        this.setRegistryName(PrimalMagic.MODID, this.gem.getTag() + "_wand_gem_item");
    }

    public WandGem getWandGem() {
        return this.gem;
    }
    
    @Override
    public Rarity getRarity(ItemStack stack) {
        return this.gem.getRarity();
    }
}
