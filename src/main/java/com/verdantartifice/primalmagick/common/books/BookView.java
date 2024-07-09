package com.verdantartifice.primalmagick.common.books;

import com.mojang.datafixers.util.Either;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Record containing all the parameters needed to render a player's view of an encoded static book.
 * 
 * @author Daedalus4096
 */
public record BookView(Either<Holder<BookDefinition>, Holder<Enchantment>> bookDef, Holder<BookLanguage> language, int comprehension) {
    public BookView withComprehension(int newComprehension) {
        return new BookView(this.bookDef, this.language, newComprehension);
    }
    
    public ResourceKey<?> unwrapBookKey() {
        return this.bookDef.map(bookHolder -> bookHolder.unwrapKey().get(), enchHolder -> enchHolder.unwrapKey().get());
    }
}
