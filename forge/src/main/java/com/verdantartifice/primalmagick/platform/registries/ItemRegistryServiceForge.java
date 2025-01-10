package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IItemRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the item registry service.
 *
 * @author Daedalus4096
 */
public class ItemRegistryServiceForge extends AbstractBuiltInRegistryServiceForge<Item> implements IItemRegistryService {
    @Override
    protected Supplier<DeferredRegister<Item>> getDeferredRegisterSupplier() {
        return ItemRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<Item> getRegistry() {
        return BuiltInRegistries.ITEM;
    }
}
