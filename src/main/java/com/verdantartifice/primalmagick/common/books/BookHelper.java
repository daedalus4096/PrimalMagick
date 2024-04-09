package com.verdantartifice.primalmagick.common.books;

import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.function.TriFunction;

import com.verdantartifice.primalmagick.client.books.ClientBookHelper;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.util.FunctionUtils;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringDecomposer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Helper methods for dealing with static books on the client and server sides.
 * 
 * @author Daedalus4096
 */
public class BookHelper {
    public static final Pattern WORD_BOUNDARY = Pattern.compile("\\b");
    public static final Pattern SEPARATOR_ONLY = Pattern.compile("^[\\p{P} \\n]+$");

    public static final Style BASE_TEXT_STYLE = Style.EMPTY;

    private static BiFunction<BookView, RegistryAccess, Component> memoizedTitleText = Util.memoize(BookHelper::getTitleTextInner);
    private static TriFunction<BookView, Component, RegistryAccess, Component> memoizedAuthorText = FunctionUtils.memoize(BookHelper::getAuthorTextInner);

    public static void invalidate() {
        memoizedTitleText = Util.memoize(BookHelper::getTitleTextInner);
        memoizedAuthorText = FunctionUtils.memoize(BookHelper::getAuthorTextInner);
    }
    
    public static String getTitleTranslationKey(ResourceKey<?> bookKey) {
        if (bookKey.isFor(RegistryKeysPM.BOOKS)) {
            return String.join(".", "written_book", bookKey.location().getNamespace(), bookKey.location().getPath(), "title");
        } else {
            return "tooltip.primalmagick.question_marks";
        }
    }

    public static Component getTitleText(BookView view, RegistryAccess registryAccess) {
        return memoizedTitleText.apply(view, registryAccess);
    }
    
    private static Component getTitleTextInner(BookView view, RegistryAccess registryAccess) {
        MutableComponent retVal = Component.empty();
        String titleTranslationKey = getTitleTranslationKey(view.bookKey());
        final Holder.Reference<BookLanguage> lang = view.getLanguageOrDefault(registryAccess, BookLanguagesPM.DEFAULT);
        final Lexicon langLex = LexiconManager.getLexicon(lang.key().location()).orElseThrow();
        final Lexicon loremLex = LexiconManager.getLexicon(LexiconManager.LOREM_IPSUM).orElseThrow();
        
        // Add the encoded title text
        Stream.of(WORD_BOUNDARY.split(StringDecomposer.getPlainText(Component.translatable(titleTranslationKey)))).forEach(word -> {
            if (SEPARATOR_ONLY.matcher(word).matches()) {
                // If the word is just a separator (e.g. whitespace, punctuation) then add it directly
                retVal.append(Component.literal(word).withStyle(BASE_TEXT_STYLE));
            } else if (lang.get().autoTranslate()) {
                // If the language is auto-translating, then add the title text directly
                retVal.append(Component.literal(word).withStyle(BASE_TEXT_STYLE));
            } else if (lang.get().isTranslatable() && langLex.isWordTranslated(word, view.comprehension(), lang.get().complexity())) {
                // If the word has been translated, then add it directly
                retVal.append(Component.literal(word).withStyle(BASE_TEXT_STYLE));
            } else {
                // If the word has not been translated, then add an encoded replacement word
                retVal.append(Component.literal(loremLex.getReplacementWord(word)).withStyle(lang.get().style()));
            }
        });
        return retVal;
    }
    
    public static Component getAuthorText(BookView view, Component unencodedText, RegistryAccess registryAccess) {
        return memoizedAuthorText.apply(view, unencodedText, registryAccess);
    }
    
    private static Component getAuthorTextInner(BookView view, Component unencodedText, RegistryAccess registryAccess) {
        MutableComponent retVal = Component.empty();
        final Holder.Reference<BookLanguage> lang = view.getLanguageOrDefault(registryAccess, BookLanguagesPM.DEFAULT);
        final Lexicon langLex = LexiconManager.getLexicon(lang.key().location()).orElseThrow();
        final Lexicon loremLex = LexiconManager.getLexicon(LexiconManager.LOREM_IPSUM).orElseThrow();
        
        // Add the encoded title text
        Stream.of(WORD_BOUNDARY.split(StringDecomposer.getPlainText(unencodedText))).forEach(word -> {
            if (SEPARATOR_ONLY.matcher(word).matches()) {
                // If the word is just a separator (e.g. whitespace, punctuation) then add it directly
                retVal.append(Component.literal(word).withStyle(BASE_TEXT_STYLE));
            } else if (lang.get().autoTranslate()) {
                // If the language is auto-translating, then add the author text directly
                retVal.append(Component.literal(word).withStyle(BASE_TEXT_STYLE));
            } else if (lang.get().isTranslatable() && langLex.isWordTranslated(word, view.comprehension(), lang.get().complexity())) {
                // If the word has been translated, then add it directly
                retVal.append(Component.literal(word).withStyle(BASE_TEXT_STYLE));
            } else {
                // If the word has not been translated, then add an encoded replacement word
                retVal.append(Component.literal(loremLex.getReplacementWord(word)).withStyle(lang.get().style()));
            }
        });
        return retVal;
    }
    
    public static double getBookComprehension(BookView view, RegistryAccess registryAccess) {
        return (FMLEnvironment.dist == Dist.CLIENT) ? ClientBookHelper.getBookComprehension(view, registryAccess) : 0;
    }
}
