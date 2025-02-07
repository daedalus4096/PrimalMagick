package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Forge listeners for mod lifecycle related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid= Constants.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModLifecycleEventListeners {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        ModLifecycleEvents.commonSetup(event::enqueueWork);
    }
}
