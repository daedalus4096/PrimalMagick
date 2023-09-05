package com.verdantartifice.primalmagick.common.books;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

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

    public static final Supplier<IForgeRegistry<BookLanguage>> LANGUAGES = DEFERRED_LANGUAGES.makeRegistry(RegistryBuilder::new);
    
    public static void init() {
        DEFERRED_LANGUAGES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    protected static RegistryObject<BookLanguage> register(String name, Supplier<BookLanguage> langSupplier) {
        return DEFERRED_LANGUAGES.register(name, langSupplier);
    }
    
    // Register book languages
    public static final RegistryObject<BookLanguage> DEFAULT = register("default", () -> new BookLanguage(PrimalMagick.resource("default"), new ResourceLocation("default"), -1));
    public static final RegistryObject<BookLanguage> GALACTIC = register("galactic", () -> new BookLanguage(PrimalMagick.resource("galactic"), new ResourceLocation("alt"), -1));
    public static final RegistryObject<BookLanguage> ILLAGER = register("illager", () -> new BookLanguage(PrimalMagick.resource("illager"), new ResourceLocation("illageralt"), 20));
}
