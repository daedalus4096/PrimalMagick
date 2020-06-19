package com.verdantartifice.primalmagic.common.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.effects.EffectsPM;

import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for miscellaneous entity events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class EntityEvents {
    @SubscribeEvent
    public static void onEnderTeleport(EnderTeleportEvent event) {
        // Prevent the teleport if the teleporter is afflicted with Enderlock
        if (event.isCancelable() && event.getEntityLiving().isPotionActive(EffectsPM.ENDERLOCK.get())) {
            event.setCanceled(true);
        }
    }
}
