package com.verdantartifice.primalmagick.common.items;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Deferred registry for mod items.
 * 
 * @author Daedalus4096
 */
public class ItemRegistration {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

    public static DeferredRegister<Item> getDeferredRegister() {
        return ITEMS;
    }
    
    public static void init() {
        ITEMS.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
}
