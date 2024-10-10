package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.platform.services.IItemService;
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
public class ItemServiceForge extends AbstractBuiltInRegistryServiceForge<Item> implements IItemService {
    @Override
    protected Supplier<DeferredRegister<Item>> getDeferredRegisterSupplier() {
        return ItemRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<Item> getRegistry() {
        return BuiltInRegistries.ITEM;
    }
}
