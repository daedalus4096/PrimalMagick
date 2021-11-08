package com.verdantartifice.primalmagick.common.items.misc;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

/**
 * Definition of a spawn item for a pixie companion.
 * 
 * @author Daedalus4096
 */
public class PixieItem extends LazySpawnEggItem {
    public PixieItem(Supplier<EntityType<?>> typeSupplier, Source source, Item.Properties properties) {
        super(typeSupplier, 0xFFFFFF, source.getColor(), properties);
    }
}
