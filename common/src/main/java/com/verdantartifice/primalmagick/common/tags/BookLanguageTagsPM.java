package com.verdantartifice.primalmagick.common.tags;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.tags.TagKey;

/**
 * Collection of custom-defined book language tags for the mod.  Used to determine tag contents
 * and for data file generation.
 * 
 * @author Daedalus4096
 */
public class BookLanguageTagsPM {
    public static final TagKey<BookLanguage> ANCIENT = create("ancient");
    public static final TagKey<BookLanguage> LINGUISTICS_UNLOCK = create("linguistics_unlock");

    private static TagKey<BookLanguage> create(String name) {
        return TagKey.create(RegistryKeysPM.BOOK_LANGUAGES, ResourceUtils.loc(name));
    }
}
