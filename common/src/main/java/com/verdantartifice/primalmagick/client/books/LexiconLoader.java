package com.verdantartifice.primalmagick.client.books;

import com.mojang.datafixers.util.Either;
import com.verdantartifice.primalmagick.common.books.BookHelper;
import com.verdantartifice.primalmagick.common.books.BookView;
import com.verdantartifice.primalmagick.common.books.Lexicon;
import com.verdantartifice.primalmagick.common.books.LexiconManager;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Resource loader for client lexicons.  Reads datapack-provided JSON files as well as parses lang assets
 * for book languages.
 * 
 * @author Daedalus4096
 */
public class LexiconLoader extends SimpleJsonResourceReloadListener<Lexicon> {
    private static final Logger LOGGER = LogManager.getLogger();
    
    private static LexiconLoader INSTANCE;
    
    protected LexiconLoader() {
        super(Lexicon.CODEC, FileToIdConverter.json("lexicons"));
    }
    
    public static LexiconLoader getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Cannot retrieve LexiconLoader until resources are loaded at least once");
        } else {
            return INSTANCE;
        }
    }
    
    public static LexiconLoader getOrCreateInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LexiconLoader();
        }
        return INSTANCE;
    }

    @Override
    protected void apply(@NotNull Map<Identifier, Lexicon> pObject, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
        // Load lexicons explicitly defined in resource packs
        for (Map.Entry<Identifier, Lexicon> entry : pObject.entrySet()) {
            if (entry.getValue() == null) {
                LOGGER.error("Failed to load lexicon {}", entry.getKey());
            } else {
                LexiconManager.setLexicon(entry.getKey(), entry.getValue());
            }
        }

        // Invalidate book helper caches
        BookHelper.invalidate();
        ClientBookHelper.invalidate();

        LOGGER.info("Loaded {} lexicons", LexiconManager.getAllLexicons().size());
    }

    public void updateWithTagData(RegistryAccess registryAccess) {
        LOGGER.info("Updating lexicons with tagged data");
        registryAccess.lookupOrThrow(RegistryKeysPM.BOOK_LANGUAGES).listElements().forEach(langHolder -> {
            Lexicon lexicon = new Lexicon();
            registryAccess.lookupOrThrow(RegistryKeysPM.BOOKS).listElements()
                    .map(bookHolder -> new BookView(Either.left(bookHolder), langHolder, 0))
                    .forEach(view -> lexicon.addWords(ClientBookHelper.getUnencodedWords(view)));
            LexiconManager.setLexicon(langHolder.key().identifier(), lexicon);
        });
    }
}
