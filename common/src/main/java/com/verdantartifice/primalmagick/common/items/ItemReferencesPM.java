package com.verdantartifice.primalmagick.common.items;

import com.verdantartifice.primalmagick.PrimalMagick;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ItemReferencesPM {
    public static final ResourceKey<Item> HYDROMELON_SEEDS = createKey("hydromelon_seeds");
    
    private static ResourceKey<Item> createKey(String id) {
        return ResourceKey.create(Registries.ITEM, ResourceUtils.loc(id));
    }
}
