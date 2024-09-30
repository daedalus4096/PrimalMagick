package com.verdantartifice.primalmagick;

import com.verdantartifice.primalmagick.common.init.InitRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import javax.annotation.Nullable;

/**
 * Main class of the Primal Magick mod.  Most initialization doesn't happen here,
 * but rather in response to Neoforge events.
 *
 * @author Daedalus4096
 */
@Mod(Constants.MOD_ID)
public class PrimalMagick {
    private static IEventBus eventBus;

    public PrimalMagick(IEventBus eventBus) {
        PrimalMagick.eventBus = eventBus;
        InitRegistries.initDeferredRegistries();
    }

    @Nullable
    public static IEventBus getEventBus() {
        return PrimalMagick.eventBus;
    }
}