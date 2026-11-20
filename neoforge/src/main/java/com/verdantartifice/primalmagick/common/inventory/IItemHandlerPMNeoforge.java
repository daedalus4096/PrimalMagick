package com.verdantartifice.primalmagick.common.inventory;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public interface IItemHandlerPMNeoforge extends IItemHandlerPM, ResourceHandler<ItemResource>, IndexModifier<ItemResource> {
}
