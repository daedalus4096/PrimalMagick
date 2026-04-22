package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.Constants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;
import net.neoforged.neoforge.client.event.RegisterSelectItemModelPropertyEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class ItemModelPropertyEventListeners {
    @SubscribeEvent
    public static void onSelectItemModelPropertiesInit(RegisterSelectItemModelPropertyEvent event) {
        ItemModelPropertyEvents.onSelectItemModelPropertyInit(event::register);
    }

    @SubscribeEvent
    public static void onRangeSelectItemModelPropertiesInit(RegisterRangeSelectItemModelPropertyEvent event) {
        ItemModelPropertyEvents.onRangeSelectItemModelPropertyInit(event::register);
    }
}
