package com.verdantartifice.primalmagick.common.events;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.Culture;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.runes.RuneEnchantmentDefinition;
import com.verdantartifice.primalmagick.common.theorycrafting.ProjectTemplate;
import com.verdantartifice.primalmagick.common.tips.TipDefinition;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

/**
 * Handlers for mod registration events.
 * 
 * @author Daedalus4096
 */
public class RegistryEvents {
    public static void onNewDatapackRegistry(SyncedDataPackRegistrar registrar) {
        registrar.register(RegistryKeysPM.RESEARCH_DISCIPLINES, ResearchDiscipline.codec(), ResearchDiscipline.codec());
        registrar.register(RegistryKeysPM.RESEARCH_ENTRIES, ResearchEntry.codec(), ResearchEntry.codec());
        registrar.register(RegistryKeysPM.PROJECT_TEMPLATES, ProjectTemplate.codec(), ProjectTemplate.codec());
        registrar.register(RegistryKeysPM.RUNE_ENCHANTMENT_DEFINITIONS, RuneEnchantmentDefinition.codec(), RuneEnchantmentDefinition.codec());
        registrar.register(RegistryKeysPM.BOOKS, BookDefinition.DIRECT_CODEC, BookDefinition.NETWORK_CODEC);
        registrar.register(RegistryKeysPM.BOOK_LANGUAGES, BookLanguage.DIRECT_CODEC, BookLanguage.NETWORK_CODEC);
        registrar.register(RegistryKeysPM.CULTURES, Culture.DIRECT_CODEC, Culture.NETWORK_CODEC);
        registrar.register(RegistryKeysPM.TIPS, TipDefinition.codec(), TipDefinition.codec());
    }

    public interface SyncedDataPackRegistrar {
        <T> void register(ResourceKey<Registry<T>> registryKey, Codec<T> codec, @Nullable Codec<T> networkCodec);
    }
}
