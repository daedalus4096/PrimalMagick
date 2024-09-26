package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.blocks.BlockRegistration;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.platform.services.IItemService;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

/**
 * Forge implementation of the item registry service.
 *
 * @author Daedalus4096
 */
public class ItemServiceForge extends AbstractRegistryServiceForge<Item> implements IItemService {
    @Override
    protected Supplier<DeferredRegister<Item>> getDeferredRegisterSupplier() {
        return ItemRegistration::getDeferredRegister;
    }

    @Override
    protected IForgeRegistry<Item> getRegistry() {
        return ForgeRegistries.ITEMS;
    }
}
