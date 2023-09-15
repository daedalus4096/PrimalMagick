package com.verdantartifice.primalmagick.common.books;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

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
    
    protected static RegistryObject<BookDefinition> register(String name, Supplier<BookDefinition> bookSupplier) {
        return DEFERRED_BOOKS.register(name, bookSupplier);
    }
    
    public static TagKey<BookDefinition> createTagKey(String name) {
        return DEFERRED_BOOKS.createTagKey(name);
    }
    
    // Register static books
    public static final RegistryObject<BookDefinition> TEST_BOOK = register("test", () -> new BookDefinition(PrimalMagick.resource("test")));
    public static final RegistryObject<BookDefinition> DREAM_JOURNAL = register("dream_journal", () -> new BookDefinition(PrimalMagick.resource("dream_journal")));
    public static final RegistryObject<BookDefinition> SOURCE_PRIMER = register("source_primer", () -> new BookDefinition(PrimalMagick.resource("source_primer")));
}
