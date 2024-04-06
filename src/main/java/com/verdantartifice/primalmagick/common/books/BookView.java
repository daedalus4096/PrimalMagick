package com.verdantartifice.primalmagick.common.books;

import net.minecraft.resources.ResourceKey;

/**
 * Record containing all the parameters needed to render a player's view of an encoded static book.
 * 
 * @author Daedalus4096
 */
public record BookView(ResourceKey<?> bookKey, ResourceKey<?> languageId, int comprehension) {
    public BookView withComprehension(int newComprehension) {
        return new BookView(this.bookKey, this.languageId, newComprehension);
    }
}
