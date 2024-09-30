package com.verdantartifice.primalmagick.common.items;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Deferred registry for mod items in Neoforge.
 *
 * @author Daedalus4096
 */
public class ItemRegistration {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Constants.MOD_ID);

    public static DeferredRegister<Item> getDeferredRegister() {
        return ITEMS;
    }

    public static void init() {
        ITEMS.register(PrimalMagick.getEventBus());
    }
}
