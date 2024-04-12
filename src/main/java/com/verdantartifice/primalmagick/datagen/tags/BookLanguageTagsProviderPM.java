package com.verdantartifice.primalmagick.datagen.tags;

import java.util.concurrent.CompletableFuture;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.tags.BookLanguageTagsPM;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Data provider for all the mod's book language tags.
 * 
 * @author Daedalus4096
 */
public class BookLanguageTagsProviderPM extends TagsProvider<BookLanguage> {
    public BookLanguageTagsProviderPM(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, RegistryKeysPM.BOOK_LANGUAGES, future, PrimalMagick.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BookLanguageTagsPM.ANCIENT).add(BookLanguagesPM.EARTH, BookLanguagesPM.SEA, BookLanguagesPM.SKY, BookLanguagesPM.SUN, BookLanguagesPM.MOON, BookLanguagesPM.TRADE, BookLanguagesPM.FORBIDDEN, BookLanguagesPM.HALLOWED);
        this.tag(BookLanguageTagsPM.LINGUISTICS_UNLOCK).addTag(BookLanguageTagsPM.ANCIENT).add(BookLanguagesPM.BABELTONGUE);
    }
}
