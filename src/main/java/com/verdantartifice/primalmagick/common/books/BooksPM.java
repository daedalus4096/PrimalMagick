package com.verdantartifice.primalmagick.common.books;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

/**
 * Deferred registry for mod books.
 * 
 * @author Daedalus4096
 */
public class BooksPM {
    // Register static books
    public static final ResourceKey<BookDefinition> TEST_BOOK = create("test");
    public static final ResourceKey<BookDefinition> DREAM_JOURNAL = create("dream_journal");
    public static final ResourceKey<BookDefinition> WELCOME = create("welcome");
    public static final ResourceKey<BookDefinition> SOURCE_PRIMER = create("source_primer");
    
    public static ResourceKey<BookDefinition> create(String name) {
        return ResourceKey.create(RegistryKeysPM.BOOKS, PrimalMagick.resource(name));
    }
    
    public static void bootstrap(BootstapContext<BookDefinition> context) {
        context.register(BooksPM.TEST_BOOK, new BookDefinition(PrimalMagick.resource("test")));
        context.register(BooksPM.DREAM_JOURNAL, new BookDefinition(PrimalMagick.resource("dream_journal")));
        context.register(BooksPM.WELCOME, new BookDefinition(PrimalMagick.resource("welcome")));
        context.register(BooksPM.SOURCE_PRIMER, new BookDefinition(PrimalMagick.resource("source_primer")));
    }
}
