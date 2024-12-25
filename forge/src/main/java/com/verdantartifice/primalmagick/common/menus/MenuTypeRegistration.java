package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Deferred registry for mod menu types in Forge.
 * 
 * @author Daedalus4096
 */
public class MenuTypeRegistration {
    private static final DeferredRegister<MenuType<?>> TYPES = DeferredRegister.create(Registries.MENU, Constants.MOD_ID);

    public static DeferredRegister<MenuType<?>> getDeferredRegister() {
        return TYPES;
    }
    
    public static void init() {
        TYPES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
}
