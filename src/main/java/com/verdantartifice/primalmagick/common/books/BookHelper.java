package com.verdantartifice.primalmagick.common.books;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.verdantartifice.primalmagick.client.books.ClientBookHelper;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringDecomposer;
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

    private static Function<BookView, Component> memoizedTitleText = Util.memoize(BookHelper::getTitleTextInner);
    private static BiFunction<BookView, Component, Component> memoizedAuthorText = Util.memoize(BookHelper::getAuthorTextInner);

    public static void invalidate() {
        memoizedTitleText = Util.memoize(BookHelper::getTitleTextInner);
        memoizedAuthorText = Util.memoize(BookHelper::getAuthorTextInner);
    }
    
    public static String getTitleTranslationKey(BookView view) {
        return view.bookDef().map(bookHolder -> {
            ResourceKey<BookDefinition> bookKey = bookHolder.unwrapKey().get();
            return String.join(".", "written_book", bookKey.location().getNamespace(), bookKey.location().getPath(), "title");
        }, enchHolder -> {
            return "tooltip.primalmagick.question_marks";
        });
    }

    public static Component getTitleText(BookView view) {
        return memoizedTitleText.apply(view);
    }
    
    private static Component getTitleTextInner(BookView view) {
        MutableComponent retVal = Component.empty();
        String titleTranslationKey = getTitleTranslationKey(view);
        final Lexicon langLex = LexiconManager.getLexicon(view.language().unwrapKey().get().location()).orElseThrow();
        final Lexicon loremLex = LexiconManager.getLexicon(LexiconManager.LOREM_IPSUM).orElseThrow();
        
        // Add the encoded title text
        Stream.of(WORD_BOUNDARY.split(StringDecomposer.getPlainText(Component.translatable(titleTranslationKey)))).forEach(word -> {
            if (SEPARATOR_ONLY.matcher(word).matches()) {
                // If the word is just a separator (e.g. whitespace, punctuation) then add it directly
                retVal.append(Component.literal(word).withStyle(BASE_TEXT_STYLE));
            } else if (view.language().value().autoTranslate()) {
                // If the language is auto-translating, then add the title text directly
                retVal.append(Component.literal(word).withStyle(BASE_TEXT_STYLE));
            } else if (view.language().value().isTranslatable() && langLex.isWordTranslated(word, view.comprehension(), view.language().value().complexity())) {
                // If the word has been translated, then add it directly
                retVal.append(Component.literal(word).withStyle(BASE_TEXT_STYLE));
            } else {
                // If the word has not been translated, then add an encoded replacement word
                retVal.append(Component.literal(loremLex.getReplacementWord(word)).withStyle(view.language().value().style()));
            }
        });
        return retVal;
    }
    
    public static Component getAuthorText(BookView view, Component unencodedText) {
        return memoizedAuthorText.apply(view, unencodedText);
    }
    
    private static Component getAuthorTextInner(BookView view, Component unencodedText) {
        MutableComponent retVal = Component.empty();
        final Lexicon langLex = LexiconManager.getLexicon(view.language().unwrapKey().get().location()).orElseThrow();
        final Lexicon loremLex = LexiconManager.getLexicon(LexiconManager.LOREM_IPSUM).orElseThrow();
        
        // Add the encoded title text
        Stream.of(WORD_BOUNDARY.split(StringDecomposer.getPlainText(unencodedText))).forEach(word -> {
            if (SEPARATOR_ONLY.matcher(word).matches()) {
                // If the word is just a separator (e.g. whitespace, punctuation) then add it directly
                retVal.append(Component.literal(word).withStyle(BASE_TEXT_STYLE));
            } else if (view.language().value().autoTranslate()) {
                // If the language is auto-translating, then add the author text directly
                retVal.append(Component.literal(word).withStyle(BASE_TEXT_STYLE));
            } else if (view.language().value().isTranslatable() && langLex.isWordTranslated(word, view.comprehension(), view.language().value().complexity())) {
                // If the word has been translated, then add it directly
                retVal.append(Component.literal(word).withStyle(BASE_TEXT_STYLE));
            } else {
                // If the word has not been translated, then add an encoded replacement word
                retVal.append(Component.literal(loremLex.getReplacementWord(word)).withStyle(view.language().value().style()));
            }
        });
        return retVal;
    }
    
    public static double getBookComprehension(BookView view) {
        return FMLEnvironment.dist.isClient() ? ClientBookHelper.getBookComprehension(view) : 0;
    }
}
