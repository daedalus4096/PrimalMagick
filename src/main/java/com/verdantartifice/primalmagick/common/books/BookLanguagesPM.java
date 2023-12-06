package com.verdantartifice.primalmagick.common.books;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.tags.BookDefinitionTagsPM;

import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for languages in which mod books can be written/encoded.
 * 
 * @author Daedalus4096
 */
public class BookLanguagesPM {
    private static final DeferredRegister<BookLanguage> DEFERRED_LANGUAGES = DeferredRegister.create(RegistryKeysPM.BOOK_LANGUAGES, PrimalMagick.MODID);

    public static final Supplier<IForgeRegistry<BookLanguage>> LANGUAGES = DEFERRED_LANGUAGES.makeRegistry(() -> new RegistryBuilder<BookLanguage>().hasTags());
    
    public static void init() {
        DEFERRED_LANGUAGES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    protected static RegistryObject<BookLanguage> register(String name, Supplier<BookLanguage> langSupplier) {
        return DEFERRED_LANGUAGES.register(name, langSupplier);
    }
    
    // Register book languages
    public static final RegistryObject<BookLanguage> DEFAULT = register("default", () -> new BookLanguage(PrimalMagick.resource("default"), Style.EMPTY.withFont(new ResourceLocation("default")), 0, BookDefinitionTagsPM.DEFAULT_BOOKS));
    public static final RegistryObject<BookLanguage> GALACTIC = register("galactic", () -> new BookLanguage(PrimalMagick.resource("galactic"), Style.EMPTY.withFont(new ResourceLocation("alt")), -1, BookDefinitionTagsPM.GALACTIC_BOOKS));
    public static final RegistryObject<BookLanguage> ILLAGER = register("illager", () -> new BookLanguage(PrimalMagick.resource("illager"), Style.EMPTY.withFont(new ResourceLocation("illageralt")), 20, BookDefinitionTagsPM.ILLAGER_BOOKS));
    public static final RegistryObject<BookLanguage> EARTH = register("earth", () -> new BookLanguage(PrimalMagick.resource("earth"), Style.EMPTY.withFont(PrimalMagick.resource("earth")), 20, BookDefinitionTagsPM.EARTH_BOOKS));
    public static final RegistryObject<BookLanguage> SEA = register("sea", () -> new BookLanguage(PrimalMagick.resource("sea"), Style.EMPTY.withFont(PrimalMagick.resource("sea")), 20, BookDefinitionTagsPM.SEA_BOOKS));
    public static final RegistryObject<BookLanguage> SKY = register("sky", () -> new BookLanguage(PrimalMagick.resource("sky"), Style.EMPTY.withFont(PrimalMagick.resource("sky")), 20, BookDefinitionTagsPM.SKY_BOOKS));
    public static final RegistryObject<BookLanguage> SUN = register("sun", () -> new BookLanguage(PrimalMagick.resource("sun"), Style.EMPTY.withFont(PrimalMagick.resource("sun")), 20, BookDefinitionTagsPM.SUN_BOOKS));
    public static final RegistryObject<BookLanguage> MOON = register("moon", () -> new BookLanguage(PrimalMagick.resource("moon"), Style.EMPTY.withFont(PrimalMagick.resource("moon")), 20, BookDefinitionTagsPM.MOON_BOOKS));
    public static final RegistryObject<BookLanguage> TRADE = register("trade", () -> new BookLanguage(PrimalMagick.resource("trade"), Style.EMPTY.withFont(PrimalMagick.resource("trade")), 20, BookDefinitionTagsPM.TRADE_BOOKS));
    public static final RegistryObject<BookLanguage> FORBIDDEN = register("forbidden", () -> new BookLanguage(PrimalMagick.resource("forbidden"), Style.EMPTY.withFont(PrimalMagick.resource("forbidden")), 20, BookDefinitionTagsPM.FORBIDDEN_BOOKS));
    public static final RegistryObject<BookLanguage> HALLOWED = register("hallowed", () -> new BookLanguage(PrimalMagick.resource("hallowed"), Style.EMPTY.withFont(PrimalMagick.resource("hallowed")), 20, BookDefinitionTagsPM.HALLOWED_BOOKS));
}
