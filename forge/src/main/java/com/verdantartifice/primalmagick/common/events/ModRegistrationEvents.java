package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.Culture;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.runes.RuneEnchantmentDefinition;
import com.verdantartifice.primalmagick.common.theorycrafting.ProjectTemplate;

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
    public static void onNewDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(RegistryKeysPM.RESEARCH_DISCIPLINES, ResearchDiscipline.codec(), ResearchDiscipline.codec());
        event.dataPackRegistry(RegistryKeysPM.RESEARCH_ENTRIES, ResearchEntry.codec(), ResearchEntry.codec());
        event.dataPackRegistry(RegistryKeysPM.PROJECT_TEMPLATES, ProjectTemplate.codec(), ProjectTemplate.codec());
        event.dataPackRegistry(RegistryKeysPM.RUNE_ENCHANTMENT_DEFINITIONS, RuneEnchantmentDefinition.codec(), RuneEnchantmentDefinition.codec());
        event.dataPackRegistry(RegistryKeysPM.BOOKS, BookDefinition.DIRECT_CODEC, BookDefinition.NETWORK_CODEC);
        event.dataPackRegistry(RegistryKeysPM.BOOK_LANGUAGES, BookLanguage.DIRECT_CODEC, BookLanguage.NETWORK_CODEC);
        event.dataPackRegistry(RegistryKeysPM.CULTURES, Culture.DIRECT_CODEC, Culture.NETWORK_CODEC);
    }
}
