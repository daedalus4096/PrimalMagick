package com.verdantartifice.primalmagick.client.books;

import java.util.List;
import java.util.function.BiFunction;

import com.google.common.collect.ImmutableList;

import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

/**
 * Helper methods for dealing with static books on the client side.
 * 
 * @author Daedalus4096
 */
public class BookHelper {
    public static final int TEXT_WIDTH = 114;
    public static final int TEXT_HEIGHT = 128;
    public static final int LINE_HEIGHT = 9;
    public static final int MAX_LINES_PER_PAGE = TEXT_HEIGHT / LINE_HEIGHT;
    
    private static final BiFunction<ResourceLocation, Font, List<FormattedCharSequence>> MEMOIZED_TEXT_LINES = Util.memoize(BookHelper::getTextLinesInner);

    public static String getTranslationKey(ResourceLocation bookId, String sectionName) {
        return String.join(".", "written_book", bookId.getNamespace(), bookId.getPath(), sectionName);
    }
    
    public static List<FormattedCharSequence> getTextLines(ResourceLocation bookId, Font font) {
        return MEMOIZED_TEXT_LINES.apply(bookId, font);
    }
    
    private static List<FormattedCharSequence> getTextLinesInner(ResourceLocation bookId, Font font) {
        String textTranslationKey = getTranslationKey(bookId, "text");
        Component fullText = Component.translatable(textTranslationKey);
        return font.split(fullText, TEXT_WIDTH);
    }
    
    public static List<FormattedCharSequence> getTextPage(ResourceLocation bookId, int page, Font font) {
        List<FormattedCharSequence> lines = getTextLines(bookId, font);
        int lowLine = Mth.clamp(page * MAX_LINES_PER_PAGE, 0, lines.size());
        int highLine = Mth.clamp((page + 1) * MAX_LINES_PER_PAGE, 0, lines.size());
        return ImmutableList.copyOf(lines.subList(lowLine, highLine));
    }
    
    public static int getNumPages(ResourceLocation bookId, Font font) {
        return Mth.ceil((float)getTextLines(bookId, font).size() / (float)MAX_LINES_PER_PAGE);
    }
}
