package com.verdantartifice.primalmagic.common.sources;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class Source {
    public static final Map<String, Source> SOURCES = new HashMap<>();
    
    public static final Source EARTH = new Source("earth", 0x228B22, TextFormatting.DARK_GREEN);
    public static final Source SEA = new Source("sea", 0x004080, TextFormatting.DARK_BLUE);
    public static final Source SKY = new Source("sky", 0x87CEEB, TextFormatting.AQUA);
    public static final Source SUN = new Source("sun", 0xF9D71C, TextFormatting.YELLOW);
    public static final Source MOON = new Source("moon", 0xD0D5D2, TextFormatting.GRAY);
    public static final Source BLOOD = new Source("blood", 0x8A0303, TextFormatting.DARK_RED);
    public static final Source INFERNAL = new Source("infernal", 0xE25822, TextFormatting.GOLD);
    public static final Source VOID = new Source("void", 0x551A8B, TextFormatting.DARK_PURPLE);
    public static final Source HALLOWED = new Source("hallowed", 0xEEEBD9, TextFormatting.WHITE);
    
    public static final List<Source> SORTED_SOURCES = Arrays.asList(Source.EARTH, Source.SEA, Source.SKY, Source.SUN, Source.MOON, Source.BLOOD, Source.INFERNAL, Source.VOID, Source.HALLOWED);

    protected String tag;
    protected int color;
    protected TextFormatting chatColor;
    protected ResourceLocation image;
    
    public Source(@Nonnull String tag, int color, @Nonnull TextFormatting chatColor) {
        this(tag, color, chatColor, new ResourceLocation(PrimalMagic.MODID, "textures/sources/" + tag.toLowerCase() + ".png"));
    }
    
    public Source(@Nonnull String tag, int color, @Nonnull TextFormatting chatColor, @Nonnull ResourceLocation image) {
        if (SOURCES.containsKey(tag)) {
            throw new IllegalArgumentException("Source " + tag + " already registered!");
        }
        this.tag = tag;
        this.color = color;
        this.chatColor = chatColor;
        this.image = image;
        SOURCES.put(tag, this);
    }
    
    @Nonnull
    public String getTag() {
        return this.tag;
    }
    
    @Nonnull
    public String getNameTranslationKey() {
        return PrimalMagic.MODID + ".source." + this.tag;
    }
    
    public int getColor() {
        return this.color;
    }
    
    @Nonnull
    public TextFormatting getChatColor() {
        return this.chatColor;
    }
    
    @Nonnull
    public ResourceLocation getImage() {
        return this.image;
    }
    
    @Nullable
    public static Source getSource(@Nullable String tag) {
        return SOURCES.get(tag);
    }
}
