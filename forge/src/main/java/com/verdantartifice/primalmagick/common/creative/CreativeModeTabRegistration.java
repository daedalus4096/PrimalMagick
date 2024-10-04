package com.verdantartifice.primalmagick.common.creative;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Deferred registry for mod creative mode tabs in Forge.
 *
 * @author Daedalus4096
 */
public class CreativeModeTabRegistration {
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    public static DeferredRegister<CreativeModeTab> getDeferredRegister() {
        return TABS;
    }

    public static void init() {
        TABS.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
}
