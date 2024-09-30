package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.common.blocks.BlockRegistration;

/**
 * Point of initialization for mod deferred registries.
 *
 * @author Daedalus4096
 */
public class InitRegistries {
    public static void initDeferredRegistries() {
        BlockRegistration.init();
    }
}
