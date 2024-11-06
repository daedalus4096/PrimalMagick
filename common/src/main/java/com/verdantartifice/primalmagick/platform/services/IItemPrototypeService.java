package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagick.common.items.misc.ManaFontBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface IItemPrototypeService {
    Supplier<ArcanometerItem> arcanometer();
    <T extends Block> Supplier<ManaFontBlockItem> manaFont(Supplier<T> blockSupplier, Item.Properties properties);
}
