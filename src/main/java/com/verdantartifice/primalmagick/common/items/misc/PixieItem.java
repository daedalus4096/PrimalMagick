package com.verdantartifice.primalmagick.common.items.misc;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;

/**
 * Definition of a spawn item for a pixie companion.
 * 
 * @author Daedalus4096
 */
public class PixieItem extends ForgeSpawnEggItem {
    public PixieItem(Supplier<EntityType<? extends Mob>> typeSupplier, Source source, Item.Properties properties) {
        super(typeSupplier, 0xFFFFFF, source.getColor(), properties);
    }
}
