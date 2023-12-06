package com.verdantartifice.primalmagick.datagen.tags;

import java.util.concurrent.CompletableFuture;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BooksPM;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.tags.BookDefinitionTagsPM;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Data provider for all the mod's book definition tags.
 * 
 * @author Daedalus4096
 */
public class BookDefinitionTagsProviderPM extends IntrinsicHolderTagsProvider<BookDefinition> {
    public BookDefinitionTagsProviderPM(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, RegistryKeysPM.BOOKS, future, bookDef -> BooksPM.BOOKS.get().getResourceKey(bookDef).orElseThrow(), PrimalMagick.MODID, helper);
    }

    @Override
    protected void addTags(Provider pProvider) {
        this.tag(BookDefinitionTagsPM.DEFAULT_BOOKS).add(BooksPM.TEST_BOOK.get(), BooksPM.DREAM_JOURNAL.get(), BooksPM.SOURCE_PRIMER.get());
        this.tag(BookDefinitionTagsPM.GALACTIC_BOOKS).add(BooksPM.TEST_BOOK.get(), BooksPM.DREAM_JOURNAL.get());
        this.tag(BookDefinitionTagsPM.ILLAGER_BOOKS).add(BooksPM.TEST_BOOK.get(), BooksPM.DREAM_JOURNAL.get(), BooksPM.SOURCE_PRIMER.get());
        this.tag(BookDefinitionTagsPM.EARTH_BOOKS).add(BooksPM.TEST_BOOK.get(), BooksPM.DREAM_JOURNAL.get(), BooksPM.SOURCE_PRIMER.get());
        this.tag(BookDefinitionTagsPM.SEA_BOOKS).add(BooksPM.TEST_BOOK.get(), BooksPM.DREAM_JOURNAL.get(), BooksPM.SOURCE_PRIMER.get());
        this.tag(BookDefinitionTagsPM.SKY_BOOKS).add(BooksPM.TEST_BOOK.get(), BooksPM.DREAM_JOURNAL.get(), BooksPM.SOURCE_PRIMER.get());
        this.tag(BookDefinitionTagsPM.SUN_BOOKS).add(BooksPM.TEST_BOOK.get(), BooksPM.DREAM_JOURNAL.get(), BooksPM.SOURCE_PRIMER.get());
        this.tag(BookDefinitionTagsPM.MOON_BOOKS).add(BooksPM.TEST_BOOK.get(), BooksPM.DREAM_JOURNAL.get(), BooksPM.SOURCE_PRIMER.get());
        this.tag(BookDefinitionTagsPM.TRADE_BOOKS).add(BooksPM.TEST_BOOK.get(), BooksPM.DREAM_JOURNAL.get(), BooksPM.SOURCE_PRIMER.get());
        this.tag(BookDefinitionTagsPM.FORBIDDEN_BOOKS).add(BooksPM.TEST_BOOK.get(), BooksPM.DREAM_JOURNAL.get(), BooksPM.SOURCE_PRIMER.get());
        this.tag(BookDefinitionTagsPM.HALLOWED_BOOKS).add(BooksPM.TEST_BOOK.get(), BooksPM.DREAM_JOURNAL.get(), BooksPM.SOURCE_PRIMER.get());
    }
}
