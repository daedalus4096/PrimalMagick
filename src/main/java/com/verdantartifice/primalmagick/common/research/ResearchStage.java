package com.verdantartifice.primalmagick.common.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.AndRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Definition of a research stage, a portion of a research entry.  A research stage contains text to be
 * displayed in the grimoire, an optional list of granted recipes, an optional list of attunement 
 * sources, and an optional set of completion requirements.  A player must satisfy those requirements in
 * order to complete a stage and progress to the next one in the entry.  In the most common case, a 
 * research entry contains two stages: one with a set of requirements, and a second that grants recipes.
 * 
 * @author Daedalus4096
 */
public record ResearchStage(ResearchEntryKey parentKey, String textTranslationKey, Optional<AbstractRequirement<?>> completionRequirementOpt, List<ResourceLocation> recipes,
        List<ResearchEntryKey> siblings, List<ResearchEntryKey> revelations, List<AbstractResearchKey<?>> hints, SourceList attunements) {
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
        ResearchEntryKey parentKey = ResearchEntryKey.fromNetwork(buf);
        String textKey = buf.readUtf();
        Optional<AbstractRequirement<?>> compReqOpt = buf.readOptional(AbstractRequirement::fromNetwork);
        List<ResourceLocation> recipes = buf.readList(b -> b.readResourceLocation());
        List<ResearchEntryKey> siblings = buf.readList(ResearchEntryKey::fromNetwork);
        List<ResearchEntryKey> revelations = buf.readList(ResearchEntryKey::fromNetwork);
        List<AbstractResearchKey<?>> hints = buf.readList(AbstractResearchKey::fromNetwork);
        SourceList attunements = SourceList.fromNetwork(buf);
        return new ResearchStage(parentKey, textKey, compReqOpt, recipes, siblings, revelations, hints, attunements);
    }
    
    public static void toNetwork(FriendlyByteBuf buf, ResearchStage stage) {
        stage.parentKey.toNetwork(buf);
        buf.writeUtf(stage.textTranslationKey);
        buf.writeOptional(stage.completionRequirementOpt, (b, r) -> r.toNetwork(b));
        buf.writeCollection(stage.recipes, (b, l) -> b.writeResourceLocation(l));
        buf.writeCollection(stage.siblings, (b, s) -> s.toNetwork(b));
        buf.writeCollection(stage.revelations, (b, r) -> r.toNetwork(b));
        buf.writeCollection(stage.hints, (b, h) -> h.toNetwork(b));
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
    
    public List<AbstractRequirement<?>> getRequirementsByCategory(RequirementCategory category) {
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
            Stream<AbstractRequirement<?>> reqStream = this.completionRequirementOpt.get().streamByCategory(category);
            if (player == null) {
                return Collections.nCopies((int)reqStream.count(), false);
            } else {
                return reqStream.map(req -> req.isMetBy(player)).toList();
            }
        }
    }
    
    public static class Builder {
        protected final String modId;
        protected final ResearchEntry.Builder entryBuilder;
        protected final ResearchEntryKey parentKey;
        protected final int stageIndex;
        protected final List<AbstractRequirement<?>> requirements = new ArrayList<>();
        protected final List<ResourceLocation> recipes = new ArrayList<>();
        protected final List<ResearchEntryKey> siblings = new ArrayList<>();
        protected final List<ResearchEntryKey> revelations = new ArrayList<>();
        protected final List<AbstractResearchKey<?>> hints = new ArrayList<>();
        protected final SourceList.Builder attunements = SourceList.builder();
        
        public Builder(String modId, ResearchEntry.Builder entryBuilder, ResearchEntryKey parentKey, int stageIndex) {
            this.modId = Preconditions.checkNotNull(modId);
            this.entryBuilder = Preconditions.checkNotNull(entryBuilder);
            this.parentKey = Preconditions.checkNotNull(parentKey);
            this.stageIndex = stageIndex;
        }
        
        public Builder(ResearchEntry.Builder entryBuilder, ResearchEntryKey parentKey, int stageIndex) {
            this(PrimalMagick.MODID, entryBuilder, parentKey, stageIndex);
        }
        
        public Builder requirement(AbstractRequirement<?> req) {
            this.requirements.add(req);
            return this;
        }
        
        public Builder recipe(String name) {
            return this.recipe(PrimalMagick.resource(name));
        }
        
        public Builder recipe(String modId, String name) {
            return this.recipe(new ResourceLocation(modId, name));
        }
        
        public Builder recipe(ItemLike itemLike) {
            return this.recipe(ForgeRegistries.ITEMS.getKey(itemLike.asItem()));
        }
        
        public Builder recipe(ResourceLocation recipe) {
            this.recipes.add(recipe);
            return this;
        }
        
        public Builder sibling(ResearchEntryKey siblingKey) {
            this.siblings.add(siblingKey);
            return this;
        }
        
        public Builder reveals(ResearchEntryKey revelationKey) {
            this.revelations.add(revelationKey);
            return this;
        }
        
        public Builder hint(AbstractResearchKey<?> hintKey) {
            this.hints.add(hintKey);
            return this;
        }
        
        public Builder attunement(SourceList sources) {
            this.attunements.with(sources);
            return this;
        }
        
        public Builder attunement(Source source, int amount) {
            this.attunements.with(source, amount);
            return this;
        }

        private String getTextTranslationKey() {
            return String.join(".", "research", this.modId.toLowerCase(), this.parentKey.getRootKey().location().getPath().toLowerCase(), "text", "stage", Integer.toString(this.stageIndex));
        }
        
        protected Optional<AbstractRequirement<?>> getFinalRequirement() {
            if (this.requirements.isEmpty()) {
                return Optional.empty();
            } else if (this.requirements.size() == 1) {
                return Optional.of(this.requirements.get(0));
            } else {
                return Optional.of(new AndRequirement(this.requirements));
            }
        }
        
        private void validate() {
            if (this.modId.isBlank()) {
                throw new IllegalStateException("Mod ID may not be empty");
            } else if (this.stageIndex < 0) {
                throw new IllegalStateException("Stage index must be non-negative");
            }
        }
        
        ResearchStage build() {
            this.validate();
            return new ResearchStage(this.parentKey, this.getTextTranslationKey(), this.getFinalRequirement(), this.recipes, this.siblings, this.revelations, this.hints, this.attunements.build());
        }
        
        public ResearchEntry.Builder end() {
            return this.entryBuilder;
        }
    }
}
