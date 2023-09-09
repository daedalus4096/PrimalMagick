package com.verdantartifice.primalmagick.client.books;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
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
public class BookHelper {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    public static final int TEXT_WIDTH = 114;
    public static final int TEXT_HEIGHT = 128;
    public static final int LINE_HEIGHT = 9;
    public static final int MAX_LINES_PER_PAGE = TEXT_HEIGHT / LINE_HEIGHT;
    
    private static final BiFunction<BookView, Font, List<FormattedCharSequence>> MEMOIZED_TEXT_LINES = Util.memoize(BookHelper::getTextLinesInner);
    private static final Style BASE_TEXT_STYLE = Style.EMPTY;
    private static final Style FOREWORD_TEXT_STYLE = Style.EMPTY.withItalic(true);
    private static final Style AFTERWORD_TEXT_STYLE = Style.EMPTY.withItalic(true);
    
    private static final Pattern WORD_BOUNDARY = Pattern.compile("\\b");
    private static final Pattern SEPARATOR_ONLY = Pattern.compile("^[\\p{P} ]+$");
    
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
    
    public static List<FormattedCharSequence> getTextLines(BookView view, Font font) {
        return MEMOIZED_TEXT_LINES.apply(view, font);
    }
    
    private static List<FormattedCharSequence> getTextLinesInner(BookView view, Font font) {
        List<FormattedCharSequence> retVal = new ArrayList<>();
        String textTranslationKey = getTextTranslationKey(view.bookKey());
        
        final BookLanguage lang = BookLanguagesPM.LANGUAGES.get().containsKey(view.languageId()) ?
                BookLanguagesPM.LANGUAGES.get().getValue(view.languageId()) :
                BookLanguagesPM.DEFAULT.get();
        final Lexicon langLex = LexiconManager.getLexicon(lang.languageId()).orElseThrow();
        final Lexicon loremLex = LexiconManager.getLexicon(LexiconManager.LOREM_IPSUM).orElseThrow();

        // Add the un-encoded foreword
        if (view.bookKey().isFor(RegistryKeysPM.BOOKS)) {
            String key = getForewordTranslationKey(view.bookKey());
            if (I18n.exists(key)) {
                retVal.addAll(font.split(Component.translatable(key).withStyle(FOREWORD_TEXT_STYLE), TEXT_WIDTH));
                retVal.add(FormattedCharSequence.EMPTY);
            }
        }
        
        // Add the encoded main text
        font.getSplitter().splitLines(Component.translatable(textTranslationKey), TEXT_WIDTH, Style.EMPTY).forEach(line -> {
            List<Component> words = new ArrayList<>();
            Stream.of(WORD_BOUNDARY.split(StringDecomposer.getPlainText(line))).forEach(word -> {
                if (SEPARATOR_ONLY.matcher(word).matches()) {
                    // If the word is just a separator (e.g. whitespace, punctuation) then add it directly
                    words.add(Component.literal(word).withStyle(BASE_TEXT_STYLE));
                } else if (lang.isTranslatable() && langLex.isWordTranslated(word, 0, lang.complexity())) { // TODO Get comprehension from player capability
                    // If the word has been translated, then add it directly
                    words.add(Component.literal(word).withStyle(BASE_TEXT_STYLE));
                } else {
                    // If the word has not been translated, then add an encoded replacement word
                    words.add(Component.literal(loremLex.getReplacementWord(word)).withStyle(BASE_TEXT_STYLE.withFont(lang.font())));
                }
            });
            retVal.add(Language.getInstance().getVisualOrder(FormattedText.composite(words)));
        });
        
        // Add the un-encoded afterword
        if (view.bookKey().isFor(RegistryKeysPM.BOOKS)) {
            String key = getAfterwordTranslationKey(view.bookKey());
            if (I18n.exists(key)) {
                retVal.add(FormattedCharSequence.EMPTY);
                retVal.addAll(font.split(Component.translatable(key).withStyle(AFTERWORD_TEXT_STYLE), TEXT_WIDTH));
            }
        }
        
        return retVal;
    }
    
    public static List<FormattedCharSequence> getTextPage(BookView view, int page, Font font) {
        List<FormattedCharSequence> lines = getTextLines(view, font);
        int lowLine = Mth.clamp(page * MAX_LINES_PER_PAGE, 0, lines.size());
        int highLine = Mth.clamp((page + 1) * MAX_LINES_PER_PAGE, 0, lines.size());
        return ImmutableList.copyOf(lines.subList(lowLine, highLine));
    }
    
    public static int getNumPages(BookView view, Font font) {
        return Mth.ceil((float)getTextLines(view, font).size() / (float)MAX_LINES_PER_PAGE);
    }
    
    public static List<String> getUnencodedWords(BookDefinition bookDef) {
        List<String> words = new ArrayList<>();
        String textTranslationKey = getTextTranslationKey(ResourceKey.create(RegistryKeysPM.BOOKS, bookDef.bookId()));
        Component fullText = Component.translatable(textTranslationKey);
        WORD_BOUNDARY.splitAsStream(StringDecomposer.getPlainText(fullText)).filter(word -> !SEPARATOR_ONLY.matcher(word).matches()).forEach(words::add);
        return words;
    }
}
