package com.verdantartifice.primalmagick;

import com.verdantartifice.primalmagick.common.config.ConfigNeoforge;
import com.verdantartifice.primalmagick.common.init.InitRegistriesNeoforge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import javax.annotation.Nullable;

/**
 * Main class of the Primal Magick mod.  Most initialization doesn't happen here,
 * but rather in response to Neoforge events.
 *
 * @see com.verdantartifice.primalmagick.common.events.ModLifecycleEventListeners
 * @see com.verdantartifice.primalmagick.common.events.ServerLifecycleEventListeners
 * @see com.verdantartifice.primalmagick.client.events.ClientModLifecycleEventListeners
 *
 * @author Daedalus4096
 */
@Mod(Constants.MOD_ID)
public class PrimalMagick {
    private static IEventBus eventBus;

    public PrimalMagick(IEventBus eventBus, ModContainer container) {
        PrimalMagick.eventBus = eventBus;
        ConfigNeoforge.register(container);
        InitRegistriesNeoforge.initDeferredRegistries();
    }

    @Nullable
    public static IEventBus getEventBus() {
        return PrimalMagick.eventBus;
    }
}