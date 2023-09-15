package com.verdantartifice.primalmagick.common.tags;

import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BooksPM;

import net.minecraft.tags.TagKey;

/**
 * Collection of custom-defined book definition tags for the mod.  Used to determine tag contents
 * and for data file generation.  Intended to define what books are found in which languages.
 * 
 * @author Daedalus4096
 */
public class BookDefinitionTagsPM {
    public static final TagKey<BookDefinition> DEFAULT_BOOKS = create("in_language/default");
    public static final TagKey<BookDefinition> GALACTIC_BOOKS = create("in_language/galactic");
    public static final TagKey<BookDefinition> ILLAGER_BOOKS = create("in_language/illager");
    
    private static TagKey<BookDefinition> create(String name) {
        return BooksPM.createTagKey(name);
    }
}
