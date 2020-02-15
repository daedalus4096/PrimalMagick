package com.verdantartifice.primalmagic.common.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.init.InitItems;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for registration related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class RegistrationEvents {
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        InitItems.initBlockItems(event.getRegistry());
        InitItems.initItems(event.getRegistry());
    }
}
