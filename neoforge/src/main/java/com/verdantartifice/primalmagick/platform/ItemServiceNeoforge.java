package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.platform.services.IItemService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ItemServiceNeoforge extends AbstractRegistryServiceNeoforge<Item> implements IItemService {
    @Override
    protected Supplier<DeferredRegister<Item>> getDeferredRegisterSupplier() {
        return ItemRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<Item> getRegistry() {
        return BuiltInRegistries.ITEM;
    }
}
