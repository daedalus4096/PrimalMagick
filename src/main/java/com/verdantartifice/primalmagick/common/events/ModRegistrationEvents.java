package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.Culture;
import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerArcaneRecipeBook;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerAttunements;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCompanions;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagick.common.capabilities.ITileResearchCache;
import com.verdantartifice.primalmagick.common.capabilities.IWorldEntitySwappers;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataPackRegistryEvent;

/**
 * Handlers for mod registration events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistrationEvents {
    @SubscribeEvent
    public static void onRegisterCapability(RegisterCapabilitiesEvent event) {
        event.register(IPlayerKnowledge.class);
        event.register(IPlayerCooldowns.class);
        event.register(IPlayerStats.class);
        event.register(IPlayerAttunements.class);
        event.register(IPlayerCompanions.class);
        event.register(IPlayerArcaneRecipeBook.class);
        event.register(IWorldEntitySwappers.class);
        event.register(IManaStorage.class);
        event.register(ITileResearchCache.class);
    }
    
    @SubscribeEvent
    public static void onNewDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(RegistryKeysPM.BOOKS, BookDefinition.DIRECT_CODEC, BookDefinition.NETWORK_CODEC);
        event.dataPackRegistry(RegistryKeysPM.BOOK_LANGUAGES, BookLanguage.DIRECT_CODEC, BookLanguage.NETWORK_CODEC);
        event.dataPackRegistry(RegistryKeysPM.CULTURES, Culture.DIRECT_CODEC, Culture.NETWORK_CODEC);
    }
}
