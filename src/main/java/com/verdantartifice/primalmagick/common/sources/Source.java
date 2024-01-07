package com.verdantartifice.primalmagick.common.sources;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.stats.Stat;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;

/**
 * Definition of a primal source.  Each source represents a different type of magick in the mod, and
 * each has its own styling and unlock requirements.  Also contains a static registry of all sources
 * in the mod.
 * 
 * @author Daedalus4096
 */
public class Source implements StringRepresentable {
    public static final Map<String, Source> SOURCES = new HashMap<>();
    protected static final Map<SimpleResearchKey, Source> DISCOVER_KEYS = new HashMap<>();
    
    // FIXME Don't use SRK::parse for discover and sibling keys
    public static final Source EARTH = new Source("earth", 0x20702B, 0.5D, ChatFormatting.DARK_GREEN, StatsPM.MANA_SPENT_EARTH);
    public static final Source SEA = new Source("sea", 0x117899, 1.0D, ChatFormatting.BLUE, StatsPM.MANA_SPENT_SEA);
    public static final Source SKY = new Source("sky", 0x87CEEB, 1.0D, ChatFormatting.AQUA, StatsPM.MANA_SPENT_SKY);
    public static final Source SUN = new Source("sun", 0xF9C81C, 0.75D, ChatFormatting.YELLOW, StatsPM.MANA_SPENT_SUN);
    public static final Source MOON = new Source("moon", 0xD1DDE3, 1.0D, ChatFormatting.GRAY, StatsPM.MANA_SPENT_MOON);
    public static final Source BLOOD = new Source("blood", 0x8A0303, 1.5D, ChatFormatting.DARK_RED, StatsPM.MANA_SPENT_BLOOD, SimpleResearchKey.parse("t_discover_blood"), Arrays.asList(SimpleResearchKey.parse("t_discover_forbidden"), SimpleResearchKey.parse("m_sotu_discover_blood")));
    public static final Source INFERNAL = new Source("infernal", 0xED2F1B, 2.0D, ChatFormatting.GOLD, StatsPM.MANA_SPENT_INFERNAL, SimpleResearchKey.parse("t_discover_infernal"), Arrays.asList(SimpleResearchKey.parse("t_discover_forbidden"), SimpleResearchKey.parse("m_sotu_discover_infernal")));
    public static final Source VOID = new Source("void", 0x551A8B, 2.0D, ChatFormatting.DARK_PURPLE, StatsPM.MANA_SPENT_VOID, SimpleResearchKey.parse("t_discover_void"), Arrays.asList(SimpleResearchKey.parse("t_discover_forbidden"), SimpleResearchKey.parse("m_sotu_discover_void")));
    public static final Source HALLOWED = new Source("hallowed", 0xEEEBD9, 3.0D, ChatFormatting.WHITE, StatsPM.MANA_SPENT_HALLOWED, SimpleResearchKey.parse("t_discover_hallowed"));
    
    // List of sources in the order in which they should be displayed
    public static final List<Source> SORTED_SOURCES = Arrays.asList(Source.EARTH, Source.SEA, Source.SKY, Source.SUN, Source.MOON, Source.BLOOD, Source.INFERNAL, Source.VOID, Source.HALLOWED);
    
    public static final ResourceLocation UNKNOWN_IMAGE = PrimalMagick.resource("textures/research/research_unknown.png");
    protected static final ResourceLocation UNKNOWN_ATLAS_LOC = PrimalMagick.resource("research/research_unknown");
    
    public static final Codec<Source> CODEC = Codec.STRING.xmap(Source::getSource, Source::getTag);

    protected final String tag; // Unique identifier for the source
    protected final int color;  // Color to use for graphical effects
    protected final double observationMultiplier;   // How many observation points that one affinity of this source is worth
    protected final ChatFormatting chatColor;       // Color to use in text components
    protected final Stat manaSpentStat;             // Statistic to increment when spending this type of mana
    protected final SimpleResearchKey discoverKey;  // Research necessary to unlock this source and make it visible to the player
    protected final List<SimpleResearchKey> siblings;   // Research to unlock at the same time as this source
    protected final ResourceLocation image;         // Location of the source's image
    protected final ResourceLocation atlasLoc;      // Location of the source's image in the texture atlas
    
    public Source(@Nonnull String tag, int color, double multiplier, @Nonnull ChatFormatting chatColor, @Nonnull Stat manaSpentStat) {
        this(tag, color, multiplier, chatColor, manaSpentStat, null);
    }
    
    public Source(@Nonnull String tag, int color, double multiplier, @Nonnull ChatFormatting chatColor, @Nonnull Stat manaSpentStat, @Nullable SimpleResearchKey discoverKey) {
        this(tag, color, multiplier, chatColor, manaSpentStat, discoverKey, Collections.emptyList());
    }
    
    public Source(@Nonnull String tag, int color, double multiplier, @Nonnull ChatFormatting chatColor, @Nonnull Stat manaSpentStat, @Nullable SimpleResearchKey discoverKey, @Nonnull List<SimpleResearchKey> siblings) {
        this(tag, color, multiplier, chatColor, manaSpentStat, discoverKey, siblings, PrimalMagick.resource("textures/sources/" + tag.toLowerCase() + ".png"), PrimalMagick.resource("sources/" + tag.toLowerCase()));
    }
    
    public Source(@Nonnull String tag, int color, double multiplier, @Nonnull ChatFormatting chatColor, @Nonnull Stat manaSpentStat, @Nullable SimpleResearchKey discoverKey, @Nonnull List<SimpleResearchKey> siblings, @Nonnull ResourceLocation image, @Nonnull ResourceLocation atlasLoc) {
        if (SOURCES.containsKey(tag)) {
            // Don't allow a given source to be registered more than once
            throw new IllegalArgumentException("Source " + tag + " already registered!");
        }
        this.tag = tag;
        this.color = color;
        this.observationMultiplier = multiplier;
        this.chatColor = chatColor;
        this.manaSpentStat = manaSpentStat;
        this.discoverKey = discoverKey;
        this.siblings = siblings;
        this.image = image;
        this.atlasLoc = atlasLoc;
        if (this.discoverKey != null) {
            DISCOVER_KEYS.put(this.discoverKey, this);
        }
        SOURCES.put(tag, this);
    }
    
    @Nonnull
    public String getTag() {
        return this.tag;
    }
    
    @Nonnull
    public String getNameTranslationKey() {
        return getNameTranslationKey(this.tag);
    }
    
    private static String getNameTranslationKey(String tag) {
        return String.join(".", "source", PrimalMagick.MODID, tag);
    }
    
    public int getColor() {
        return this.color;
    }
    
    public double getObservationMultiplier() {
        return this.observationMultiplier;
    }
    
    @Nonnull
    public ChatFormatting getChatColor() {
        return this.chatColor;
    }
    
    @Nonnull
    public Component getNameText() {
        return this.getNameText(this.getChatColor());
    }
    
    @Nonnull
    public Component getNameText(ChatFormatting format) {
        return Component.translatable(this.getNameTranslationKey()).withStyle(format);
    }
    
    @Nonnull
    public Stat getManaSpentStat() {
        return this.manaSpentStat;
    }
    
    @Nullable
    public SimpleResearchKey getDiscoverKey() {
        return this.discoverKey;
    }
    
    @Nonnull
    public List<SimpleResearchKey> getSiblings() {
        return Collections.unmodifiableList(this.siblings);
    }
    
    @Nonnull
    public ResourceLocation getImage() {
        return this.image;
    }
    
    @Nonnull
    public ResourceLocation getAtlasLocation() {
        return this.atlasLoc;
    }
    
    public boolean isDiscovered(@Nullable Player player) {
        if (this.discoverKey == null) {
            // A source with no unlock key is automatically discovered
            return true;
        } else {
            return this.discoverKey.isKnownByStrict(player);
        }
    }
    
    @Nonnull
    public static String getUnknownTranslationKey() {
        return getNameTranslationKey("unknown");
    }
    
    @Nonnull
    public static ResourceLocation getUnknownImage() {
        return UNKNOWN_IMAGE;
    }
    
    @Nonnull
    public static ResourceLocation getUnknownAtlasLocation() {
        return UNKNOWN_ATLAS_LOC;
    }
    
    @Nullable
    public static Source getSource(@Nullable String tag) {
        return SOURCES.get(tag);
    }
    
    public static boolean isSourceDiscoverKey(@Nullable SimpleResearchKey key) {
        return DISCOVER_KEYS.containsKey(key);
    }
    
    @Nullable
    public static Source getSource(@Nullable SimpleResearchKey discoverKey) {
        return DISCOVER_KEYS.get(discoverKey);
    }

    @Override
    public String getSerializedName() {
        return this.getTag();
    }
}
