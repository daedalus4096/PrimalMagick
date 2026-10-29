package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCauldronInteractionEvent;

/**
 * Neoforge listeners for cauldron related events.
 *
 * @author Daedalus4096
 */
@EventBusSubscriber(modid = Constants.MOD_ID)
public class CauldronEventListeners {
    @SubscribeEvent
    public static void onRegisterInteractions(RegisterCauldronInteractionEvent.Interaction event) {
        CauldronEvents.onRegisterInteractions(event::register);
    }
}
