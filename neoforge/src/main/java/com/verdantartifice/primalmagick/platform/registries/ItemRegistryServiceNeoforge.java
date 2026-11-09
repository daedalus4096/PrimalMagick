package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.registries.RegistryItemNeoforge;
import com.verdantartifice.primalmagick.platform.services.registries.IItemRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Forge implementation of the item registry service.
 *
 * @author Daedalus4096
 */
public class ItemRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<Item> implements IItemRegistryService {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<Item>> getDeferredRegisterSupplier() {
        return () -> ITEMS;
    }

    @Override
    protected Registry<Item> getRegistry() {
        return BuiltInRegistries.ITEM;
    }

    @Override
    public IRegistryItem<Item, BlockItem> registerSimpleBlockItem(String name, Supplier<? extends Block> blockSupplier) {
        return new RegistryItemNeoforge<>(ITEMS.registerSimpleBlockItem(name, blockSupplier));
    }

    @Override
    public IRegistryItem<Item, BlockItem> registerSimpleBlockItem(String name, Supplier<? extends Block> blockSupplier, Supplier<Item.Properties> propertiesSupplier) {
        return new RegistryItemNeoforge<>(ITEMS.registerSimpleBlockItem(name, blockSupplier, propertiesSupplier));
    }

    @Override
    public <I extends Item> IRegistryItem<Item, I> registerItem(String name, Function<Item.Properties, ? extends I> itemFactory, Supplier<Item.Properties> propertiesSupplier) {
        return new RegistryItemNeoforge<>(ITEMS.registerItem(name, itemFactory, propertiesSupplier));
    }
}
