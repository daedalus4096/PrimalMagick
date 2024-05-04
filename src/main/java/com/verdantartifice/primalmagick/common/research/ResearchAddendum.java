package com.verdantartifice.primalmagick.common.research;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

/**
 * Definition of a research addendum.  An addendum is an addon to a research entry that is separately
 * unlocked with additional research if the entry itself is already unlocked.  Addenda have their own
 * text and may grant new recipes and attunements to the player.
 * 
 * @author Daedalus4096
 */
public record ResearchAddendum(ResearchEntryKey parentKey, String textTranslationKey, List<ResourceLocation> recipes, List<ResearchEntryKey> siblings,
        Optional<AbstractRequirement<?>> completionRequirementOpt, SourceList attunements) {
    public static final Codec<ResearchAddendum> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResearchEntryKey.CODEC.fieldOf("parentKey").forGetter(ResearchAddendum::parentKey),
            Codec.STRING.fieldOf("textTranslationKey").forGetter(ResearchAddendum::textTranslationKey),
            ResourceLocation.CODEC.listOf().fieldOf("recipes").forGetter(ResearchAddendum::recipes),
            ResearchEntryKey.CODEC.listOf().fieldOf("siblings").forGetter(ResearchAddendum::siblings),
            AbstractRequirement.CODEC.optionalFieldOf("completionRequirementOpt").forGetter(ResearchAddendum::completionRequirementOpt),
            SourceList.CODEC.optionalFieldOf("attunements", SourceList.EMPTY).forGetter(ResearchAddendum::attunements)
        ).apply(instance, ResearchAddendum::new));
    
    @Nonnull
    public static ResearchAddendum fromNetwork(FriendlyByteBuf buf) {
        ResearchEntryKey parentKey = ResearchEntryKey.fromNetwork(buf);
        String textKey = buf.readUtf();
        List<ResourceLocation> recipes = buf.readList(b -> b.readResourceLocation());
        List<ResearchEntryKey> siblings = buf.readList(ResearchEntryKey::fromNetwork);
        Optional<AbstractRequirement<?>> compReqOpt = buf.readOptional(AbstractRequirement::fromNetwork);
        SourceList attunements = SourceList.fromNetwork(buf);
        return new ResearchAddendum(parentKey, textKey, recipes, siblings, compReqOpt, attunements);
    }
    
    public static void toNetwork(FriendlyByteBuf buf, ResearchAddendum addendum) {
        addendum.parentKey.toNetwork(buf);
        buf.writeUtf(addendum.textTranslationKey);
        buf.writeCollection(addendum.recipes, (b, l) -> b.writeResourceLocation(l));
        buf.writeCollection(addendum.siblings, (b, s) -> s.toNetwork(b));
        buf.writeOptional(addendum.completionRequirementOpt, (b, r) -> r.toNetwork(b));
        SourceList.toNetwork(buf, addendum.attunements);
    }
}
