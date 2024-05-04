package com.verdantartifice.primalmagick.common.research;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Definition of a research stage, a portion of a research entry.  A research stage contains text to be
 * displayed in the grimoire, an optional list of granted recipes, an optional list of attunement 
 * sources, and an optional set of completion requirements.  A player must satisfy those requirements in
 * order to complete a stage and progress to the next one in the entry.  In the most common case, a 
 * research entry contains two stages: one with a set of requirements, and a second that grants recipes.
 * 
 * @author Daedalus4096
 */
public record ResearchStage(ResearchEntryKey parentKey, String textTranslationKey, Optional<AbstractRequirement> completionRequirementOpt, List<ResourceLocation> recipes,
        List<ResearchEntryKey> siblings, List<ResearchEntryKey> revelations, List<AbstractResearchKey> hints, SourceList attunements) {
    public static final Codec<ResearchStage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResearchEntryKey.CODEC.fieldOf("parentKey").forGetter(ResearchStage::parentKey),
            Codec.STRING.fieldOf("textTranslationKey").forGetter(ResearchStage::textTranslationKey),
            AbstractRequirement.CODEC.optionalFieldOf("completionRequirementOpt").forGetter(ResearchStage::completionRequirementOpt),
            ResourceLocation.CODEC.listOf().fieldOf("recipes").forGetter(ResearchStage::recipes),
            ResearchEntryKey.CODEC.listOf().fieldOf("siblings").forGetter(ResearchStage::siblings),
            ResearchEntryKey.CODEC.listOf().fieldOf("revelations").forGetter(ResearchStage::revelations),
            AbstractResearchKey.CODEC.listOf().fieldOf("hints").forGetter(ResearchStage::hints),
            SourceList.CODEC.optionalFieldOf("attunements", SourceList.EMPTY).forGetter(ResearchStage::attunements)
        ).apply(instance, ResearchStage::new));
    
    @Nonnull
    public static ResearchStage fromNetwork(FriendlyByteBuf buf) {
        ResearchEntryKey parentKey = null;  // TODO Deserialize parent key
        String textKey = buf.readUtf();
        Optional<AbstractRequirement> compReqOpt = null;    // TODO Deserialize optional requirements
        List<ResourceLocation> recipes = buf.readList(b -> b.readResourceLocation());
        List<ResearchEntryKey> siblings = null; // TODO Deserialize sibling list
        List<ResearchEntryKey> revelations = null;  // TODO Deserialize revelation list
        List<AbstractResearchKey> hints = null; // TODO Deserialize hint indicator list
        SourceList attunements = SourceList.fromNetwork(buf);
        
        return new ResearchStage(parentKey, textKey, compReqOpt, recipes, siblings, revelations, hints, attunements);
    }
    
    public static void toNetwork(FriendlyByteBuf buf, ResearchStage stage) {
        // TODO Serialize parent key
        buf.writeUtf(stage.textTranslationKey);
        // TODO Serialize optional requirements
        buf.writeCollection(stage.recipes, (b, l) -> b.writeResourceLocation(l));
        // TODO Serialize sibling list
        // TODO Serialize revelation list
        // TODO Serialize hint indicator list
        SourceList.toNetwork(buf, stage.attunements);
    }
    
    public boolean hasPrerequisites() {
        return this.completionRequirementOpt.isPresent();
    }
    
    public boolean arePrerequisitesMet(@Nullable Player player) {
        if (player == null) {
            return false;
        } else {
            MutableBoolean retVal = new MutableBoolean(false);
            this.completionRequirementOpt.ifPresent(req -> {
                retVal.setValue(req.isMetBy(player));
            });
            return retVal.booleanValue();
        }
    }
    
    public List<AbstractRequirement> getRequirementsByCategory(RequirementCategory category) {
        if (this.completionRequirementOpt.isEmpty()) {
            return Collections.emptyList();
        } else {
            return this.completionRequirementOpt.get().streamByCategory(category).toList();
        }
    }
    
    public List<Boolean> getRequirementCompletionByCategory(@Nullable Player player, RequirementCategory category) {
        if (this.completionRequirementOpt.isEmpty()) {
            return Collections.emptyList();
        } else {
            Stream<AbstractRequirement> reqStream = this.completionRequirementOpt.get().streamByCategory(category);
            if (player == null) {
                return Collections.nCopies((int)reqStream.count(), false);
            } else {
                return reqStream.map(req -> req.isMetBy(player)).toList();
            }
        }
    }
}
