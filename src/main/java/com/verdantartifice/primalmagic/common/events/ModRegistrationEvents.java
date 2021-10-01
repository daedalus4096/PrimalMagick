package com.verdantartifice.primalmagic.common.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerAttunements;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerCompanions;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagic.common.capabilities.ITileResearchCache;
import com.verdantartifice.primalmagic.common.capabilities.IWorldEntitySwappers;

import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for mod registration events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistrationEvents {
    @SubscribeEvent
    public static void onRegisterCapability(RegisterCapabilitiesEvent event) {
        event.register(IPlayerKnowledge.class);
        event.register(IPlayerCooldowns.class);
        event.register(IPlayerStats.class);
        event.register(IPlayerAttunements.class);
        event.register(IPlayerCompanions.class);
        event.register(IWorldEntitySwappers.class);
        event.register(IManaStorage.class);
        event.register(ITileResearchCache.class);
    }
}
