package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.Constants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;

/**
 * Neoforge events for client-only input-related events.
 * 
 * @author Daedalus4096
 */
@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class InputEventListeners {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        InputEvents.updateFlyingCarpetInputs();
    }
    
    @SubscribeEvent
    public static void onClientTickEvent(ClientTickEvent.Pre event) {
        InputEvents.onClientTickEvent();
    }
}
