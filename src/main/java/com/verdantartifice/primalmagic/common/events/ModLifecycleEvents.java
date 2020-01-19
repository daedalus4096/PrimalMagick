package com.verdantartifice.primalmagic.common.events;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Handlers for mod lifecycle related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModLifecycleEvents {
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        // Perform common setup on the sided proxy
        PrimalMagic.proxy.commonSetup(event);
    }
}
