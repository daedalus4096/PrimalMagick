package com.verdantartifice.primalmagic.common.items.wands;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.wands.WandGem;

import net.minecraft.item.Item;

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
}
