package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IItemService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the item registry service.
 *
 * @author Daedalus4096
 */
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
