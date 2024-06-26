package com.verdantartifice.primalmagick.common.books;

import java.util.Optional;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Datapack registry for languages in which mod books can be written/encoded.
 * 
 * @author Daedalus4096
 */
public class BookLanguagesPM {
    // Register book languages
    public static final ResourceKey<BookLanguage> DEFAULT = create("default");
    public static final ResourceKey<BookLanguage> GALACTIC = create("galactic");
    public static final ResourceKey<BookLanguage> ILLAGER = create("illager");
    public static final ResourceKey<BookLanguage> BABELTONGUE = create("babeltongue");
    public static final ResourceKey<BookLanguage> EARTH = create("earth");
    public static final ResourceKey<BookLanguage> SEA = create("sea");
    public static final ResourceKey<BookLanguage> SKY = create("sky");
    public static final ResourceKey<BookLanguage> SUN = create("sun");
    public static final ResourceKey<BookLanguage> MOON = create("moon");
    public static final ResourceKey<BookLanguage> TRADE = create("trade");
    public static final ResourceKey<BookLanguage> FORBIDDEN = create("forbidden");
    public static final ResourceKey<BookLanguage> HALLOWED = create("hallowed");
    
    public static ResourceKey<BookLanguage> create(String name) {
        return ResourceKey.create(RegistryKeysPM.BOOK_LANGUAGES, PrimalMagick.resource(name));
    }
    
    public static void bootstrap(BootstrapContext<BookLanguage> context) {
        context.register(BookLanguagesPM.DEFAULT, new BookLanguage(PrimalMagick.resource("default"), Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace("default")), 0, false));
        context.register(BookLanguagesPM.GALACTIC, new BookLanguage(PrimalMagick.resource("galactic"), Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace("alt")), -1, false));
        context.register(BookLanguagesPM.ILLAGER, new BookLanguage(PrimalMagick.resource("illager"), Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace("illageralt")), -1, false));
        context.register(BookLanguagesPM.BABELTONGUE, new BookLanguage(PrimalMagick.resource("babeltongue"), Style.EMPTY.withObfuscated(true), 60, true));
        context.register(BookLanguagesPM.EARTH, new BookLanguage(PrimalMagick.resource("earth"), Style.EMPTY.withFont(PrimalMagick.resource("earth")), 60, false));
        context.register(BookLanguagesPM.SEA, new BookLanguage(PrimalMagick.resource("sea"), Style.EMPTY.withFont(PrimalMagick.resource("sea")), 60, false));
        context.register(BookLanguagesPM.SKY, new BookLanguage(PrimalMagick.resource("sky"), Style.EMPTY.withFont(PrimalMagick.resource("sky")), 60, false));
        context.register(BookLanguagesPM.SUN, new BookLanguage(PrimalMagick.resource("sun"), Style.EMPTY.withFont(PrimalMagick.resource("sun")), 60, false));
        context.register(BookLanguagesPM.MOON, new BookLanguage(PrimalMagick.resource("moon"), Style.EMPTY.withFont(PrimalMagick.resource("moon")), 60, false));
        context.register(BookLanguagesPM.TRADE, new BookLanguage(PrimalMagick.resource("trade"), Style.EMPTY.withFont(PrimalMagick.resource("trade")), 60, false));
        context.register(BookLanguagesPM.FORBIDDEN, new BookLanguage(PrimalMagick.resource("forbidden"), Style.EMPTY.withFont(PrimalMagick.resource("forbidden")), 60, false));
        context.register(BookLanguagesPM.HALLOWED, new BookLanguage(PrimalMagick.resource("hallowed"), Style.EMPTY.withFont(PrimalMagick.resource("hallowed")), 60, false));
    }
    
    /**
     * Optionally gets a reference holder for the given book language, should that language be found.
     * 
     * @param registryAccess a registry accessor
     * @return an optional reference holder for this view's book language
     */
    public static Optional<Holder.Reference<BookLanguage>> getLanguage(ResourceKey<BookLanguage> langKey, RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(RegistryKeysPM.BOOK_LANGUAGES).getHolder(langKey);
    }
    
    /**
     * Gets a reference holder for the given book language, or the given default if the given language cannot be found.
     * Throws if the given default also cannot be found.
     * 
     * @param registryAccess a registry accessor
     * @param defaultLang the default language to use if the given is not found
     * @return a reference holder for the given book language, or the given default
     */
    public static Holder.Reference<BookLanguage> getLanguageOrDefault(ResourceKey<BookLanguage> langKey, RegistryAccess registryAccess, ResourceKey<BookLanguage> defaultLang) {
        Registry<BookLanguage> registry = registryAccess.registryOrThrow(RegistryKeysPM.BOOK_LANGUAGES);
        return registry.getHolder(langKey).orElse(registry.getHolderOrThrow(defaultLang));
    }
    
    /**
     * Gets a reference holder for the given book language, or throws if the given language cannot be found.
     * 
     * @param registryAccess a registry accessor
     * @return a reference holder for this view's book language
     */
    public static Holder.Reference<BookLanguage> getLanguageOrThrow(ResourceKey<BookLanguage> langKey, RegistryAccess registryAccess) {
        return registryAccess.registryOrThrow(RegistryKeysPM.BOOK_LANGUAGES).getHolderOrThrow(langKey);
    }
}
