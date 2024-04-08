package com.verdantartifice.primalmagick.common.books;

import java.util.Optional;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

/**
 * Datapack registry for mod books.
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
        
        // FIXME Remove once library testing is complete
        for (int index = 1; index <= 50; index++) {
            ResourceKey<BookDefinition> tempKey = create("loremipsum" + index);
            context.register(tempKey, new BookDefinition(PrimalMagick.resource("loremipsum" + index)));
        }
    }
    
    /**
     * Optionally gets a reference holder for the given book definition, should that definition be found.
     * 
     * @param registryAccess a registry accessor
     * @return an optional reference holder for this view's book definition
     */
    public static Optional<Holder.Reference<BookDefinition>> getBookDefinition(ResourceKey<BookDefinition> bookKey, RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(RegistryKeysPM.BOOKS).getHolder(bookKey);
    }
    
    /**
     * Gets a reference holder for the given book definitions, or the given default if the given definition cannot be found.
     * Throws if the given default also cannot be found.
     * 
     * @param registryAccess a registry accessor
     * @param defaultBook the default book definition to use if the view's is not found
     * @return a reference holder for the given book definition, or the given default
     */
    public static Holder.Reference<BookDefinition> getBookDefinitionOrDefault(ResourceKey<BookDefinition> bookKey, RegistryAccess registryAccess, ResourceKey<BookDefinition> defaultBook) {
        Registry<BookDefinition> registry = registryAccess.registryOrThrow(RegistryKeysPM.BOOKS);
        return registry.getHolder(bookKey).orElse(registry.getHolderOrThrow(defaultBook));
    }
    
    /**
     * Gets a reference holder for the given book definition, or throws if the given book definition cannot be found.
     * 
     * @param registryAccess a registry accessor
     * @return a reference holder for the given book definition
     */
    public static Holder.Reference<BookDefinition> getBookDefinitionOrThrow(ResourceKey<BookDefinition> bookKey, RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(RegistryKeysPM.BOOKS).getHolderOrThrow(bookKey);
    }
}
