package com.verdantartifice.primalmagick.client.books;

import java.util.List;
import java.util.function.BiFunction;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraftforge.registries.ForgeRegistries;

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
    
    private static final BiFunction<ResourceKey<?>, Font, List<FormattedCharSequence>> MEMOIZED_TEXT_LINES = Util.memoize(BookHelper::getTextLinesInner);

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
    
    public static List<FormattedCharSequence> getTextLines(ResourceKey<?> bookKey, Font font) {
        return MEMOIZED_TEXT_LINES.apply(bookKey, font);
    }
    
    private static List<FormattedCharSequence> getTextLinesInner(ResourceKey<?> bookKey, Font font) {
        String textTranslationKey = getTextTranslationKey(bookKey);
        Component fullText = Component.translatable(textTranslationKey);
        return font.split(fullText, TEXT_WIDTH);
    }
    
    public static List<FormattedCharSequence> getTextPage(ResourceKey<?> bookKey, int page, Font font) {
        List<FormattedCharSequence> lines = getTextLines(bookKey, font);
        int lowLine = Mth.clamp(page * MAX_LINES_PER_PAGE, 0, lines.size());
        int highLine = Mth.clamp((page + 1) * MAX_LINES_PER_PAGE, 0, lines.size());
        return ImmutableList.copyOf(lines.subList(lowLine, highLine));
    }
    
    public static int getNumPages(ResourceKey<?> bookKey, Font font) {
        return Mth.ceil((float)getTextLines(bookKey, font).size() / (float)MAX_LINES_PER_PAGE);
    }
}
