package com.verdantartifice.primalmagick.datagen.tags;

import java.util.concurrent.CompletableFuture;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.tags.BookLanguageTagsPM;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Data provider for all the mod's book language tags.
 * 
 * @author Daedalus4096
 */
public class BookLanguageTagsProviderPM extends IntrinsicHolderTagsProvider<BookLanguage> {
    public BookLanguageTagsProviderPM(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, RegistryKeysPM.BOOK_LANGUAGES, future, langDef -> BookLanguagesPM.LANGUAGES.get().getResourceKey(langDef).orElseThrow(), PrimalMagick.MODID, helper);
    }

    @Override
    protected void addTags(Provider pProvider) {
        this.tag(BookLanguageTagsPM.ANCIENT).add(BookLanguagesPM.EARTH.get(), BookLanguagesPM.SEA.get(), BookLanguagesPM.SKY.get(), BookLanguagesPM.SUN.get(), BookLanguagesPM.MOON.get(), BookLanguagesPM.TRADE.get(), BookLanguagesPM.FORBIDDEN.get(), BookLanguagesPM.HALLOWED.get());
    }
}
