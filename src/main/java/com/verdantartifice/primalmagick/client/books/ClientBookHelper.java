package com.verdantartifice.primalmagick.client.books;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang3.function.TriFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BookHelper;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BookType;
import com.verdantartifice.primalmagick.common.books.BookView;
import com.verdantartifice.primalmagick.common.books.Lexicon;
import com.verdantartifice.primalmagick.common.books.LexiconManager;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.util.FunctionUtils;

import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.StringDecomposer;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Helper methods for dealing with static books on the client side.
 * 
 * @author Daedalus4096
 */
public class ClientBookHelper {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    public static final int TEXT_WIDTH = 114;
    public static final int TEXT_HEIGHT = 128;
    public static final int LINE_HEIGHT = 9;
    public static final int MAX_LINES_PER_PAGE = TEXT_HEIGHT / LINE_HEIGHT;
    
    private static final Style FOREWORD_TEXT_STYLE = Style.EMPTY.withItalic(true);
    private static final Style AFTERWORD_TEXT_STYLE = Style.EMPTY.withItalic(true);
    
    private static final Map<BookType, BookSprites> SPRITES = ImmutableMap.<BookType, BookSprites>builder()
            .put(BookType.BOOK, BookSprites.VANILLA)
            .put(BookType.TABLET, BookSprites.TABLET)
            .build();

    private static TriFunction<BookView, RegistryAccess, Font, List<FormattedCharSequence>> memoizedTextLines = FunctionUtils.memoize(ClientBookHelper::getTextLinesInner);
    private static Function<ResourceKey<BookDefinition>, List<String>> memoizedUnencodedWords = Util.memoize(ClientBookHelper::getUnencodedWordsInner);
    private static BiFunction<BookView, RegistryAccess, Double> memoizedBookComprehension = Util.memoize(ClientBookHelper::getBookComprehensionInner);

    public static void invalidate() {
        memoizedTextLines = FunctionUtils.memoize(ClientBookHelper::getTextLinesInner);
        memoizedUnencodedWords = Util.memoize(ClientBookHelper::getUnencodedWordsInner);
        memoizedBookComprehension = Util.memoize(ClientBookHelper::getBookComprehensionInner);
    }
    
    public static BookSprites getSprites(BookType type) {
        return SPRITES.getOrDefault(type, BookSprites.VANILLA);
    }
    
    private static String getForewordTranslationKey(ResourceKey<?> bookKey) {
        if (bookKey.isFor(RegistryKeysPM.BOOKS)) {
            return String.join(".", "written_book", bookKey.location().getNamespace(), bookKey.location().getPath(), "foreword");
        } else {
            return "tooltip.primalmagick.question_marks";
        }
    }

    private static String getAfterwordTranslationKey(ResourceKey<?> bookKey) {
        if (bookKey.isFor(RegistryKeysPM.BOOKS)) {
            return String.join(".", "written_book", bookKey.location().getNamespace(), bookKey.location().getPath(), "afterword");
        } else {
            return "tooltip.primalmagick.question_marks";
        }
    }

    private static String getAuthorTranslationKey(ResourceKey<?> bookKey) {
        if (bookKey.isFor(RegistryKeysPM.BOOKS)) {
            return String.join(".", "written_book", bookKey.location().getNamespace(), bookKey.location().getPath(), "author");
        } else {
            return "tooltip.primalmagick.question_marks";
        }
    }

    private static String getTextTranslationKey(ResourceKey<?> bookKey) {
        if (bookKey.isFor(RegistryKeysPM.BOOKS)) {
            return String.join(".", "written_book", bookKey.location().getNamespace(), bookKey.location().getPath(), "text");
        } else if (bookKey.isFor(ForgeRegistries.Keys.ENCHANTMENTS)) {
            String key = String.join(".", "enchantment", bookKey.location().getNamespace(), bookKey.location().getPath(), "desc");
            if (I18n.exists(key)) {
                return key;
            } else {
                key = String.join(".", "enchantment", bookKey.location().getNamespace(), bookKey.location().getPath(), "rune_enchantment", "text");
                if (I18n.exists(key)) {
                    return key;
                } else {
                    return String.join(".", "enchantment", bookKey.location().getNamespace(), bookKey.location().getPath());
                }
            }
        }
        return "tooltip.primalmagick.question_marks";
    }
    
    public static List<FormattedCharSequence> getTextLines(BookView view, RegistryAccess registryAccess, Font font) {
        return memoizedTextLines.apply(view, registryAccess, font);
    }
    
    private static List<FormattedCharSequence> getTextLinesInner(BookView view, RegistryAccess registryAccess, Font font) {
        LOGGER.debug("Calculating text lines for book {}, lang {}, comprehension {}", view.bookKey(), view.languageId(), view.comprehension());
        List<FormattedCharSequence> retVal = new ArrayList<>();
        String textTranslationKey = getTextTranslationKey(view.bookKey());
        
        final Holder.Reference<BookLanguage> lang = view.getLanguageOrDefault(registryAccess, BookLanguagesPM.DEFAULT);
        final Lexicon langLex = LexiconManager.getLexicon(lang.key().location()).orElseThrow();
        final Lexicon loremLex = LexiconManager.getLexicon(LexiconManager.LOREM_IPSUM).orElseThrow();
        final Optional<StyleGuide> langStyleGuideOpt = StyleGuideManager.getStyleGuide(lang.key().location());

        // Add the un-encoded foreword
        if (view.bookKey().isFor(RegistryKeysPM.BOOKS)) {
            String key = getForewordTranslationKey(view.bookKey());
            if (I18n.exists(key)) {
                retVal.addAll(font.split(ComponentUtils.wrapInSquareBrackets(Component.translatable(key)).withStyle(FOREWORD_TEXT_STYLE), TEXT_WIDTH));
                retVal.add(FormattedCharSequence.EMPTY);
            }
        }
        
        // Add the encoded main text
        List<Component> words = new ArrayList<>();
        Stream.of(BookHelper.WORD_BOUNDARY.split(StringDecomposer.getPlainText(Component.translatable(textTranslationKey)))).forEach(word -> {
            if (BookHelper.SEPARATOR_ONLY.matcher(word).matches()) {
                // If the word is just a separator (e.g. whitespace, punctuation) then add it directly
                words.add(Component.literal(word).withStyle(BookHelper.BASE_TEXT_STYLE));
            } else if (lang.get().isTranslatable() && langLex.isWordTranslated(word, view.comprehension(), lang.get().complexity())) {
                // If the word has been translated, then add it directly
                langStyleGuideOpt.ifPresentOrElse(styleGuide -> {
                    words.add(Component.literal(word).withStyle(styleGuide.getStyle(word, BookHelper.BASE_TEXT_STYLE)));
                }, () -> {
                    words.add(Component.literal(word).withStyle(BookHelper.BASE_TEXT_STYLE));
                });
            } else {
                // If the word has not been translated, then add an encoded replacement word
                langStyleGuideOpt.ifPresentOrElse(styleGuide -> {
                    words.add(Component.literal(loremLex.getReplacementWord(word)).withStyle(styleGuide.getStyle(word, lang.get().style())));
                }, () -> {
                    words.add(Component.literal(loremLex.getReplacementWord(word)).withStyle(lang.get().style()));
                });
            }
        });
        retVal.addAll(Language.getInstance().getVisualOrder(font.getSplitter().splitLines(FormattedText.composite(words), TEXT_WIDTH, Style.EMPTY)));
        
        // Add the un-encoded afterword
        if (view.bookKey().isFor(RegistryKeysPM.BOOKS)) {
            String key = getAfterwordTranslationKey(view.bookKey());
            if (I18n.exists(key)) {
                retVal.add(FormattedCharSequence.EMPTY);
                retVal.addAll(font.split(ComponentUtils.wrapInSquareBrackets(Component.translatable(key)).withStyle(AFTERWORD_TEXT_STYLE), TEXT_WIDTH));
            }
        }
        
        return retVal;
    }
    
    public static List<FormattedCharSequence> getTextPage(BookView view, int page, RegistryAccess registryAccess, Font font) {
        List<FormattedCharSequence> lines = getTextLines(view, registryAccess, font);
        int lowLine = Mth.clamp(page * MAX_LINES_PER_PAGE, 0, lines.size());
        int highLine = Mth.clamp((page + 1) * MAX_LINES_PER_PAGE, 0, lines.size());
        return ImmutableList.copyOf(lines.subList(lowLine, highLine));
    }
    
    public static int getNumPages(BookView view, RegistryAccess registryAccess, Font font) {
        return Mth.ceil((float)getTextLines(view, registryAccess, font).size() / (float)MAX_LINES_PER_PAGE);
    }
    
    public static List<String> getUnencodedWords(ResourceKey<BookDefinition> bookKey) {
        return memoizedUnencodedWords.apply(bookKey);
    }
    
    private static List<String> getUnencodedWordsInner(ResourceKey<BookDefinition> bookKey) {
        List<String> words = new ArrayList<>();
        gatherUnencodedWords(getTextTranslationKey(bookKey), words::add);
        gatherUnencodedWords(BookHelper.getTitleTranslationKey(bookKey), words::add);
        gatherUnencodedWords(getAuthorTranslationKey(bookKey), words::add);
        return words;
    }
    
    private static void gatherUnencodedWords(String translationKey, Consumer<String> adder) {
        BookHelper.WORD_BOUNDARY.splitAsStream(StringDecomposer.getPlainText(Component.translatable(translationKey))).filter(word -> !BookHelper.SEPARATOR_ONLY.matcher(word).matches()).forEach(adder);
    }
    
    /**
     * Returns the percentage of the given book, in the given language, that the player understands with the given
     * comprehension score, as a value between 0 and 1, inclusive.  Note that this may be different from the player's
     * overall language comprehension, as the contents of the book may only be a subset of the language's overall
     * lexicon.
     * 
     * @param view the book view whose contents are to be tested
     * @return the percentage of the book that the player understands
     */
    public static double getBookComprehension(BookView view, RegistryAccess registryAccess) {
        return memoizedBookComprehension.apply(view, registryAccess);
    }
    
    private static double getBookComprehensionInner(BookView view, RegistryAccess registryAccess) {
        final Holder.Reference<BookLanguage> bookLang = view.getLanguageOrDefault(registryAccess, BookLanguagesPM.DEFAULT);

        if (!bookLang.get().isTranslatable()) {
            // Non-translatable languages are never understood
            return 0;
        } else if (bookLang.get().complexity() == 0) {
            // Zero-complexity languages are always fully understood
            return 1;
        } else {
            List<String> bookWords = getUnencodedWords(view.bookKey().cast(RegistryKeysPM.BOOKS).orElseThrow());
            final Lexicon langLex = LexiconManager.getLexicon(bookLang.key().location()).orElseThrow();
            int totalCount = bookWords.size();
            int translatedCount = (int)bookWords.stream().filter(word -> langLex.isWordTranslated(word, view.comprehension(), bookLang.get().complexity())).count();
            return Mth.clamp((double)translatedCount / (double)totalCount, 0, 1);
        }
    }
}
