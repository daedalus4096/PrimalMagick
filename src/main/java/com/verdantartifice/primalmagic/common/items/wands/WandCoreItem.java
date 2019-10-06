package com.verdantartifice.primalmagic.common.items.wands;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.wands.WandCore;

import net.minecraft.item.Item;

public class WandCoreItem extends Item {
    protected final WandCore core;

    public WandCoreItem(WandCore core, Properties properties) {
        super(properties);
        this.core = core;
        this.setRegistryName(PrimalMagic.MODID, this.core.getTag() + "_wand_core_item");
    }

    public WandCore getWandCore() {
        return this.core;
    }
}
