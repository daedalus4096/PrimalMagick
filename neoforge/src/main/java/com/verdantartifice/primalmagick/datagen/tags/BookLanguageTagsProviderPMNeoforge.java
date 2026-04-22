package com.verdantartifice.primalmagick.datagen.tags;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.tags.BookLanguageTagsPM;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Data provider for all the mod's book language tags.
 * 
 * @author Daedalus4096
 */
public class BookLanguageTagsProviderPMNeoforge extends KeyTagProvider<BookLanguage> {
    public BookLanguageTagsProviderPMNeoforge(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, RegistryKeysPM.BOOK_LANGUAGES, future, Constants.MOD_ID);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider pProvider) {
        this.tag(BookLanguageTagsPM.ANCIENT).add(BookLanguagesPM.EARTH).add(BookLanguagesPM.SEA).add(BookLanguagesPM.SKY).add(BookLanguagesPM.SUN).add(BookLanguagesPM.MOON).add(BookLanguagesPM.TRADE).add(BookLanguagesPM.FORBIDDEN).add(BookLanguagesPM.HALLOWED);
        this.tag(BookLanguageTagsPM.LINGUISTICS_UNLOCK).addTag(BookLanguageTagsPM.ANCIENT).add(BookLanguagesPM.BABELTONGUE);
    }
}
