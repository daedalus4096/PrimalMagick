package com.verdantartifice.primalmagick.common.tags;

import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;

import net.minecraft.tags.TagKey;

/**
 * Collection of custom-defined book language tags for the mod.  Used to determine tag contents
 * and for data file generation.
 * 
 * @author Daedalus4096
 */
public class BookLanguageTagsPM {
    public static final TagKey<BookLanguage> ANCIENT = create("ancient");

    private static TagKey<BookLanguage> create(String name) {
        return BookLanguagesPM.createTagKey(name);
    }
}
