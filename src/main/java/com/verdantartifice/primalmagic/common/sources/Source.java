package com.verdantartifice.primalmagic.common.sources;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

/**
 * Definition of a primal source.  Each source represents a different type of magic in the mod, and
 * each has its own styling and unlock requirements.  Also contains a static registry of all sources
 * in the mod.
 * 
 * @author Daedalus4096
 */
public class Source {
    public static final Map<String, Source> SOURCES = new HashMap<>();
    
    public static final Source EARTH = new Source("earth", 0x20702B, 0.5D, TextFormatting.DARK_GREEN);
    public static final Source SEA = new Source("sea", 0x117899, 1.0D, TextFormatting.BLUE);
    public static final Source SKY = new Source("sky", 0x87CEEB, 1.0D, TextFormatting.AQUA);
    public static final Source SUN = new Source("sun", 0xF9C81C, 0.75D, TextFormatting.YELLOW);
    public static final Source MOON = new Source("moon", 0xD1DDE3, 1.0D, TextFormatting.GRAY);
    public static final Source BLOOD = new Source("blood", 0x8A0303, 1.5D, TextFormatting.DARK_RED, SimpleResearchKey.parse("t_discover_blood"));
    public static final Source INFERNAL = new Source("infernal", 0xED2F1B, 2.0D, TextFormatting.GOLD, SimpleResearchKey.parse("t_discover_infernal"));
    public static final Source VOID = new Source("void", 0x551A8B, 2.0D, TextFormatting.DARK_PURPLE, SimpleResearchKey.parse("t_discover_void"));
    public static final Source HALLOWED = new Source("hallowed", 0xEEEBD9, 3.0D, TextFormatting.WHITE, SimpleResearchKey.parse("t_discover_hallowed"));
    
    // List of sources in the order in which they should be displayed
    public static final List<Source> SORTED_SOURCES = Arrays.asList(Source.EARTH, Source.SEA, Source.SKY, Source.SUN, Source.MOON, Source.BLOOD, Source.INFERNAL, Source.VOID, Source.HALLOWED);
    
    protected static final ResourceLocation UNKNOWN_IMAGE = new ResourceLocation(PrimalMagic.MODID, "textures/research/research_unknown.png");

    protected final String tag; // Unique identifier for the source
    protected final int color;  // Color to use for graphical effects
    protected final double observationMultiplier;   // How many observation points that one affinity of this source is worth
    protected final TextFormatting chatColor;       // Color to use in text components
    protected final SimpleResearchKey discoverKey;  // Research necessary to unlock this source and make it visible to the player
    protected final ResourceLocation image;         // Location of the source's image
    
    public Source(@Nonnull String tag, int color, double multiplier, @Nonnull TextFormatting chatColor) {
        this(tag, color, multiplier, chatColor, null);
    }
    
    public Source(@Nonnull String tag, int color, double multiplier, @Nonnull TextFormatting chatColor, @Nullable SimpleResearchKey discoverKey) {
        this(tag, color, multiplier, chatColor, discoverKey, new ResourceLocation(PrimalMagic.MODID, "textures/sources/" + tag.toLowerCase() + ".png"));
    }
    
    public Source(@Nonnull String tag, int color, double multiplier, @Nonnull TextFormatting chatColor, @Nullable SimpleResearchKey discoverKey, @Nonnull ResourceLocation image) {
        if (SOURCES.containsKey(tag)) {
            // Don't allow a given source to be registered more than once
            throw new IllegalArgumentException("Source " + tag + " already registered!");
        }
        this.tag = tag;
        this.color = color;
        this.observationMultiplier = multiplier;
        this.chatColor = chatColor;
        this.discoverKey = discoverKey;
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
    
    public double getObservationMultiplier() {
        return this.observationMultiplier;
    }
    
    @Nonnull
    public TextFormatting getChatColor() {
        return this.chatColor;
    }
    
    @Nullable
    public SimpleResearchKey getDiscoverKey() {
        return this.discoverKey;
    }
    
    @Nonnull
    public ResourceLocation getImage() {
        return this.image;
    }
    
    public boolean isDiscovered(@Nullable PlayerEntity player) {
        if (this.discoverKey == null) {
            // A source with no unlock key is automatically discovered
            return true;
        } else {
            return this.discoverKey.isKnownByStrict(player);
        }
    }
    
    @Nonnull
    public static String getUnknownTranslationKey() {
        return PrimalMagic.MODID + ".source.unknown";
    }
    
    @Nonnull
    public static ResourceLocation getUnknownImage() {
        return UNKNOWN_IMAGE;
    }
    
    @Nullable
    public static Source getSource(@Nullable String tag) {
        return SOURCES.get(tag);
    }
}
