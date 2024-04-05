package com.verdantartifice.primalmagick.common.books;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod books.
 * 
 * @author Daedalus4096
 */
public class BooksPM {
    private static final DeferredRegister<BookDefinition> DEFERRED_BOOKS = DeferredRegister.create(RegistryKeysPM.BOOKS, PrimalMagick.MODID);
    
    public static final Supplier<IForgeRegistry<BookDefinition>> BOOKS = DEFERRED_BOOKS.makeRegistry(() -> new RegistryBuilder<BookDefinition>().hasTags());
    
    public static void init() {
        DEFERRED_BOOKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static ResourceKey<BookDefinition> create(String name) {
        return ResourceKey.create(RegistryKeysPM.BOOKS, PrimalMagick.resource(name));
    }
    
    protected static RegistryObject<BookDefinition> register(String name, Supplier<BookDefinition> bookSupplier) {
        return DEFERRED_BOOKS.register(name, bookSupplier);
    }
    
    public static TagKey<BookDefinition> createTagKey(String name) {
        return DEFERRED_BOOKS.createTagKey(name);
    }
    
    // Register static books
    public static final ResourceKey<BookDefinition> TEST_BOOK = create("test");
    public static final ResourceKey<BookDefinition> DREAM_JOURNAL = create("dream_journal");
    public static final ResourceKey<BookDefinition> WELCOME = create("welcome");
    public static final ResourceKey<BookDefinition> SOURCE_PRIMER = create("source_primer");
    
    public static void bootstrap(BootstapContext<BookDefinition> context) {
        context.register(BooksPM.TEST_BOOK, new BookDefinition(PrimalMagick.resource("test")));
        context.register(BooksPM.DREAM_JOURNAL, new BookDefinition(PrimalMagick.resource("dream_journal")));
        context.register(BooksPM.WELCOME, new BookDefinition(PrimalMagick.resource("welcome")));
        context.register(BooksPM.SOURCE_PRIMER, new BookDefinition(PrimalMagick.resource("source_primer")));
    }
}
