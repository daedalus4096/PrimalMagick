package com.verdantartifice.primalmagick.common.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.keys.StackCraftedKey;
import com.verdantartifice.primalmagick.common.research.keys.TagCraftedKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.AndRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ExpertiseRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ItemStackRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ItemTagRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.KnowledgeRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.StatRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.VanillaCustomStatRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.VanillaItemUsedStatRequirement;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.stats.Stat;
import com.verdantartifice.primalmagick.common.util.StreamCodecUtils;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
        List<AbstractResearchKey<?>> siblings, List<ResearchEntryKey> revelations, SourceList attunements) {
    public static Codec<ResearchStage> codec() { 
        return RecordCodecBuilder.create(instance -> instance.group(
            ResearchEntryKey.CODEC.codec().fieldOf("parentKey").forGetter(ResearchStage::parentKey),
            Codec.STRING.fieldOf("textTranslationKey").forGetter(ResearchStage::textTranslationKey),
            AbstractRequirement.dispatchCodec().optionalFieldOf("completionRequirementOpt").forGetter(ResearchStage::completionRequirementOpt),
            ResourceLocation.CODEC.listOf().fieldOf("recipes").forGetter(ResearchStage::recipes),
            AbstractResearchKey.dispatchCodec().listOf().fieldOf("siblings").forGetter(ResearchStage::siblings),
            ResearchEntryKey.CODEC.codec().listOf().fieldOf("revelations").forGetter(ResearchStage::revelations),
            SourceList.CODEC.optionalFieldOf("attunements", SourceList.EMPTY).forGetter(ResearchStage::attunements)
        ).apply(instance, ResearchStage::new));
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, ResearchStage> streamCodec() {
        return StreamCodecUtils.composite(
                ResearchEntryKey.STREAM_CODEC,
                ResearchStage::parentKey,
                ByteBufCodecs.STRING_UTF8,
                ResearchStage::textTranslationKey,
                ByteBufCodecs.optional(AbstractRequirement.dispatchStreamCodec()),
                ResearchStage::completionRequirementOpt,
                ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()),
                ResearchStage::recipes,
                AbstractResearchKey.dispatchStreamCodec().apply(ByteBufCodecs.list()),
                ResearchStage::siblings,
                ResearchEntryKey.STREAM_CODEC.apply(ByteBufCodecs.list()),
                ResearchStage::revelations,
                SourceList.STREAM_CODEC,
                ResearchStage::attunements,
                ResearchStage::new);
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
        protected final List<AbstractResearchKey<?>> siblings = new ArrayList<>();
        protected final List<ResearchEntryKey> revelations = new ArrayList<>();
        protected final SourceList.Builder attunements = SourceList.builder();
        
        public Builder(String modId, ResearchEntry.Builder entryBuilder, ResearchEntryKey parentKey, int stageIndex) {
            this.modId = Preconditions.checkNotNull(modId);
            this.entryBuilder = Preconditions.checkNotNull(entryBuilder);
            this.parentKey = Preconditions.checkNotNull(parentKey);
            this.stageIndex = stageIndex;
        }
        
        public Builder(ResearchEntry.Builder entryBuilder, ResearchEntryKey parentKey, int stageIndex) {
            this(Constants.MOD_ID, entryBuilder, parentKey, stageIndex);
        }
        
        public Builder requirement(AbstractRequirement<?> req) {
            this.requirements.add(req);
            return this;
        }
        
        public Builder requiredCraft(ItemStack stack) {
            return this.requirement(new ResearchRequirement(new StackCraftedKey(stack.copy())));
        }
        
        public Builder requiredCraft(ItemLike item) {
            return this.requiredCraft(new ItemStack(item.asItem()));
        }
        
        public Builder requiredCraft(TagKey<Item> tag) {
            return this.requirement(new ResearchRequirement(new TagCraftedKey(tag)));
        }
        
        public Builder requiredItem(ItemStack stack) {
            return this.requirement(new ItemStackRequirement(stack.copy()));
        }
        
        public Builder requiredItem(ItemLike item, int count) {
            return this.requiredItem(new ItemStack(item.asItem(), count));
        }
        
        public Builder requiredItem(ItemLike item) {
            return this.requiredItem(item, 1);
        }
        
        public Builder requiredItem(TagKey<Item> tag, int count) {
            return this.requirement(new ItemTagRequirement(tag, count));
        }
        
        public Builder requiredItem(TagKey<Item> tag) {
            return this.requiredItem(tag, 1);
        }
        
        public Builder requiredResearch(ResourceKey<ResearchEntry> entryKey) {
            return this.requirement(new ResearchRequirement(new ResearchEntryKey(entryKey)));
        }
        
        protected Builder requiredKnowledge(KnowledgeType type, int levels) {
            return this.requirement(new KnowledgeRequirement(type, levels));
        }
        
        public Builder requiredObservations(int levels) {
            return this.requiredKnowledge(KnowledgeType.OBSERVATION, levels);
        }
        
        public Builder requiredTheories(int levels) {
            return this.requiredKnowledge(KnowledgeType.THEORY, levels);
        }
        
        public Builder requiredStat(Stat stat, int value) {
            return this.requirement(new StatRequirement(stat, value));
        }
        
        public Builder requiredVanillaCustomStat(ResourceLocation statLoc, int value, IconDefinition iconDef) {
            return this.requirement(new VanillaCustomStatRequirement(statLoc, value, iconDef));
        }
        
        public Builder requiredVanillaItemUsedStat(ItemLike item, int value) {
            return this.requirement(new VanillaItemUsedStatRequirement(item.asItem(), value));
        }
        
        public Builder requiredExpertise(ResourceKey<ResearchDiscipline> discipline, ResearchTier tier) {
            return this.requirement(new ExpertiseRequirement(discipline, tier));
        }
        
        public Builder requiredExpertise(ResourceKey<ResearchDiscipline> discipline, ResearchTier tier, int threshold) {
            return this.requirement(new ExpertiseRequirement(discipline, tier, threshold));
        }
        
        public Builder recipe(String name) {
            return this.recipe(ResourceUtils.loc(name));
        }
        
        public Builder recipe(String modId, String name) {
            return this.recipe(ResourceLocation.fromNamespaceAndPath(modId, name));
        }
        
        public Builder recipe(ItemLike itemLike) {
            return this.recipe(ForgeRegistries.ITEMS.getKey(itemLike.asItem()));
        }
        
        public Builder recipe(ResourceLocation recipe) {
            this.recipes.add(recipe);
            return this;
        }
        
        public Builder sibling(ResourceKey<ResearchEntry> siblingKey) {
            this.siblings.add(new ResearchEntryKey(siblingKey));
            return this;
        }
        
        public Builder reveals(ResourceKey<ResearchEntry> revelationKey) {
            this.revelations.add(new ResearchEntryKey(revelationKey));
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
            return new ResearchStage(this.parentKey, this.getTextTranslationKey(), this.getFinalRequirement(), this.recipes, this.siblings, this.revelations, this.attunements.build());
        }
        
        public ResearchEntry.Builder end() {
            return this.entryBuilder;
        }
    }
}
