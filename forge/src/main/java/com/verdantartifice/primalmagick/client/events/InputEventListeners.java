package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Forge events for client-only input-related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class InputEventListeners {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        InputEvents.updateFlyingCarpetInputs();
    }
    
    @SubscribeEvent
    public static void onClientTickEvent(TickEvent.ClientTickEvent.Pre event) {
        InputEvents.onClientTickEvent();
    }
}
