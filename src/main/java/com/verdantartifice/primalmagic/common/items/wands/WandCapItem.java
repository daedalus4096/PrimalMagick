package com.verdantartifice.primalmagic.common.items.wands;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.wands.WandCap;

import net.minecraft.item.Item;

public class WandCapItem extends Item {
    protected final WandCap cap;

    public WandCapItem(WandCap cap, Properties properties) {
        super(properties);
        this.cap = cap;
        this.setRegistryName(PrimalMagic.MODID, this.cap.getTag() + "_wand_cap_item");
    }

    public WandCap getWandCap() {
        return this.cap;
    }
}
