package com.verdantartifice.primalmagick.platform.services.registries;

import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IItemRegistryService extends IRegistryService<Item> {
    IRegistryItem<Item, Item> registerSimpleItem(String name, Supplier<Item.Properties> propertiesSupplier);
    IRegistryItem<Item, Item> registerSimpleItem(String name);
    IRegistryItem<Item, BlockItem> registerSimpleBlockItem(String name, Supplier<? extends Block> blockSupplier);
    IRegistryItem<Item, BlockItem> registerSimpleBlockItem(String name, Supplier<? extends Block> blockSupplier, Supplier<Item.Properties> propertiesSupplier);
    <I extends Item> IRegistryItem<Item, I> registerItem(String name, Function<Item.Properties, ? extends I> itemFactory, Supplier<Item.Properties> propertiesSupplier);
    <I extends Item> IRegistryItem<Item, I> registerItem(String name, Function<Item.Properties, ? extends I> itemFactory);
}
