package com.verdantartifice.primalmagick.common.sources;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.stats.Stat;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
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
    protected static final ResourceLocation UNKNOWN_IMAGE = PrimalMagick.resource("textures/research/research_unknown.png");
    protected static final ResourceLocation UNKNOWN_ATLAS_LOC = PrimalMagick.resource("research/research_unknown");
    
    public static final Codec<Source> CODEC = ResourceLocation.CODEC.xmap(Sources::get, Source::getId);

    protected final ResourceLocation id;            // Unique identifier for the source
    protected final int color;                      // Color to use for graphical effects
    protected final ChatFormatting chatColor;       // Color to use in text components
    protected final double observationMultiplier;   // How many observation points that one affinity of this source is worth
    protected final Stat manaSpentStat;             // Statistic to increment when spending this type of mana
    protected final Optional<ResearchRequirement> discoverRequirement;   // Research requirement for unlocking this source and making it visible to the player
    protected final int sortOrder;                  // Order in which the source should be included in sorted lists
    protected final ResourceLocation image;         // Location of the source's image
    protected final ResourceLocation atlasLoc;      // Location of the source's image in the texture atlas
    
    public Source(ResourceLocation id, int color, ChatFormatting chatColor, double observationMultiplier, Stat manaSpentStat, Optional<ResearchRequirement> discoverRequirement, int sortOrder,
            ResourceLocation image, ResourceLocation atlasLoc) {
        this.id = id;
        this.color = color;
        this.chatColor = chatColor;
        this.observationMultiplier = observationMultiplier;
        this.manaSpentStat = manaSpentStat;
        this.discoverRequirement = discoverRequirement;
        this.sortOrder = sortOrder;
        this.image = image;
        this.atlasLoc = atlasLoc;
        Sources.register(this);
    }
    
    public Source(ResourceLocation id, int color, ChatFormatting chatColor, double observationMultiplier, Stat manaSpentStat, ResourceKey<ResearchEntry> discoverKey, int sortOrder) {
        this(id, color, chatColor, observationMultiplier, manaSpentStat, Optional.of(new ResearchRequirement(new ResearchEntryKey(discoverKey))), sortOrder,
                new ResourceLocation(id.getNamespace(), "textures/sources/" + id.getPath() + ".png"), new ResourceLocation(id.getNamespace(), "sources/" + id.getPath()));
    }
    
    public Source(ResourceLocation id, int color, ChatFormatting chatColor, double observationMultiplier, Stat manaSpentStat, int sortOrder) {
        this(id, color, chatColor, observationMultiplier, manaSpentStat, Optional.empty(), sortOrder,
                new ResourceLocation(id.getNamespace(), "textures/sources/" + id.getPath() + ".png"), new ResourceLocation(id.getNamespace(), "sources/" + id.getPath()));
    }
    
    @Nonnull
    public ResourceLocation getId() {
        return this.id;
    }
    
    @Nonnull
    public String getNameTranslationKey() {
        return getNameTranslationKey(this.id);
    }
    
    private static String getNameTranslationKey(ResourceLocation id) {
        return String.join(".", "source", id.getNamespace(), id.getPath());
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
    
    public int getSortOrder() {
        return this.sortOrder;
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
        if (this.discoverRequirement.isEmpty()) {
            // A source with no unlock key is automatically discovered
            return true;
        } else {
            return this.discoverRequirement.get().isMetBy(player);
        }
    }
    
    @Nonnull
    public static String getUnknownTranslationKey() {
        return getNameTranslationKey(PrimalMagick.resource("unknown"));
    }
    
    @Nonnull
    public static ResourceLocation getUnknownImage() {
        return UNKNOWN_IMAGE;
    }
    
    @Nonnull
    public static ResourceLocation getUnknownAtlasLocation() {
        return UNKNOWN_ATLAS_LOC;
    }
    
    @Override
    public String getSerializedName() {
        return this.getId().toString();
    }
}
