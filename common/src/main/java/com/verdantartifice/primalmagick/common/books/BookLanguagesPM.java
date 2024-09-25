package com.verdantartifice.primalmagick.common.books;

import java.util.Optional;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
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
        return ResourceKey.create(RegistryKeysPM.BOOK_LANGUAGES, ResourceUtils.loc(name));
    }
    
    public static void bootstrap(BootstrapContext<BookLanguage> context) {
        context.register(BookLanguagesPM.DEFAULT, new BookLanguage(ResourceUtils.loc("default"), Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace("default")), 0, false));
        context.register(BookLanguagesPM.GALACTIC, new BookLanguage(ResourceUtils.loc("galactic"), Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace("alt")), -1, false));
        context.register(BookLanguagesPM.ILLAGER, new BookLanguage(ResourceUtils.loc("illager"), Style.EMPTY.withFont(ResourceLocation.withDefaultNamespace("illageralt")), -1, false));
        context.register(BookLanguagesPM.BABELTONGUE, new BookLanguage(ResourceUtils.loc("babeltongue"), Style.EMPTY.withObfuscated(true), 60, true));
        context.register(BookLanguagesPM.EARTH, new BookLanguage(ResourceUtils.loc("earth"), Style.EMPTY.withFont(ResourceUtils.loc("earth")), 60, false));
        context.register(BookLanguagesPM.SEA, new BookLanguage(ResourceUtils.loc("sea"), Style.EMPTY.withFont(ResourceUtils.loc("sea")), 60, false));
        context.register(BookLanguagesPM.SKY, new BookLanguage(ResourceUtils.loc("sky"), Style.EMPTY.withFont(ResourceUtils.loc("sky")), 60, false));
        context.register(BookLanguagesPM.SUN, new BookLanguage(ResourceUtils.loc("sun"), Style.EMPTY.withFont(ResourceUtils.loc("sun")), 60, false));
        context.register(BookLanguagesPM.MOON, new BookLanguage(ResourceUtils.loc("moon"), Style.EMPTY.withFont(ResourceUtils.loc("moon")), 60, false));
        context.register(BookLanguagesPM.TRADE, new BookLanguage(ResourceUtils.loc("trade"), Style.EMPTY.withFont(ResourceUtils.loc("trade")), 60, false));
        context.register(BookLanguagesPM.FORBIDDEN, new BookLanguage(ResourceUtils.loc("forbidden"), Style.EMPTY.withFont(ResourceUtils.loc("forbidden")), 60, false));
        context.register(BookLanguagesPM.HALLOWED, new BookLanguage(ResourceUtils.loc("hallowed"), Style.EMPTY.withFont(ResourceUtils.loc("hallowed")), 60, false));
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
