package com.verdantartifice.primalmagic.common.items.base;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.item.Item;

public class ItemPM extends Item {
    public ItemPM(String name, Properties properties) {
        super(properties);
        this.setRegistryName(PrimalMagic.MODID, name);
    }
}
