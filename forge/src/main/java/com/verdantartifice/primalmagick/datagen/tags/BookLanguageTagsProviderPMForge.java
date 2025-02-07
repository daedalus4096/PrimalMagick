package com.verdantartifice.primalmagick.datagen.tags;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.tags.BookLanguageTagsPM;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

/**
 * Data provider for all the mod's book language tags.
 * 
 * @author Daedalus4096
 */
public class BookLanguageTagsProviderPMForge extends TagsProvider<BookLanguage> {
    public BookLanguageTagsProviderPMForge(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, RegistryKeysPM.BOOK_LANGUAGES, future, Constants.MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BookLanguageTagsPM.ANCIENT).add(BookLanguagesPM.EARTH, BookLanguagesPM.SEA, BookLanguagesPM.SKY, BookLanguagesPM.SUN, BookLanguagesPM.MOON, BookLanguagesPM.TRADE, BookLanguagesPM.FORBIDDEN, BookLanguagesPM.HALLOWED);
        this.tag(BookLanguageTagsPM.LINGUISTICS_UNLOCK).addTag(BookLanguageTagsPM.ANCIENT).add(BookLanguagesPM.BABELTONGUE);
    }
}
