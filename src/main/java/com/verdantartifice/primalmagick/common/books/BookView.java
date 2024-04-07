package com.verdantartifice.primalmagick.common.books;

import java.util.Optional;

import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;

/**
 * Record containing all the parameters needed to render a player's view of an encoded static book.
 * 
 * @author Daedalus4096
 */
public record BookView(ResourceKey<?> bookKey, ResourceKey<BookLanguage> languageId, int comprehension) {
    public BookView withComprehension(int newComprehension) {
        return new BookView(this.bookKey, this.languageId, newComprehension);
    }
    
    /**
     * Optionally gets a reference holder for this view's book definition, should that definition be found.
     * 
     * @param registryAccess a registry accessor
     * @return an optional reference holder for this view's book definition
     */
    public Optional<Holder.Reference<BookDefinition>> getBookDefinition(RegistryAccess registryAccess) {
        return this.bookKey().cast(RegistryKeysPM.BOOKS).flatMap(key -> BooksPM.getBookDefinition(key, registryAccess));
    }
    
    /**
     * Gets a reference holder for this view's book definitions, or the given default if the view's definition cannot be found.
     * Throws if the given default also cannot be found.
     * 
     * @param registryAccess a registry accessor
     * @param defaultBook the default book definition to use if the view's is not found
     * @return a reference holder for this view's book definition, or the given default
     */
    public Holder.Reference<BookDefinition> getBookDefinitionOrDefault(RegistryAccess registryAccess, ResourceKey<BookDefinition> defaultBook) {
        return BooksPM.getBookDefinitionOrDefault(this.languageId().cast(RegistryKeysPM.BOOKS).orElse(defaultBook), registryAccess, defaultBook);
    }
    
    /**
     * Gets a reference holder for this view's book definition, or throws if the view's book definition cannot be found.
     * 
     * @param registryAccess a registry accessor
     * @return a reference holder for this view's book definition
     */
    public Holder.Reference<BookDefinition> getBookDefinitionOrThrow(RegistryAccess registryAccess) {
        return BooksPM.getBookDefinitionOrThrow(this.bookKey().cast(RegistryKeysPM.BOOKS).orElseThrow(), registryAccess);
    }
    
    /**
     * Optionally gets a reference holder for this view's book language, should that language be found.
     * 
     * @param registryAccess a registry accessor
     * @return an optional reference holder for this view's book language
     */
    public Optional<Holder.Reference<BookLanguage>> getLanguage(RegistryAccess registryAccess) {
        return BookLanguagesPM.getLanguage(this.languageId(), registryAccess);
    }
    
    /**
     * Gets a reference holder for this view's book language, or the given default if the view's language cannot be found.
     * Throws if the given default also cannot be found.
     * 
     * @param registryAccess a registry accessor
     * @param defaultLang the default language to use if the view's is not found
     * @return a reference holder for this view's book language, or the given default
     */
    public Holder.Reference<BookLanguage> getLanguageOrDefault(RegistryAccess registryAccess, ResourceKey<BookLanguage> defaultLang) {
        return BookLanguagesPM.getLanguageOrDefault(this.languageId(), registryAccess, defaultLang);
    }
    
    /**
     * Gets a reference holder for this view's book language, or throws if the view's language cannot be found.
     * 
     * @param registryAccess a registry accessor
     * @return a reference holder for this view's book language
     */
    public Holder.Reference<BookLanguage> getLanguageOrThrow(RegistryAccess registryAccess) {
        return BookLanguagesPM.getLanguageOrThrow(this.languageId(), registryAccess);
    }
}
