package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.common.capabilities.CapabilitiesNeoforge;
import com.verdantartifice.primalmagick.common.loot.modifiers.LootModifierSerializersPM;

/**
 * Point of initialization for mod deferred registries.
 *
 * @author Daedalus4096
 */
public class InitRegistriesNeoforge {
    public static void initDeferredRegistries() {
        // Platform implementations of cross-platform registries
        InitRegistries.initDeferredRegistries();

        // Platform specific registries
        LootModifierSerializersPM.init();
        CapabilitiesNeoforge.init();
    }
}
