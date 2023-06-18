package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.creative.CreativeModeTabsPM;

import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for mod creative tab registration events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class CreativeModeTabEvents {
    @SubscribeEvent
    public static void onCreativeModeTabRegister(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabsPM.TAB.getKey()) {
            CreativeModeTabsPM.getRegisteredTabItems().forEach(itemSupplier -> event.accept(itemSupplier));
        }
    }
}
