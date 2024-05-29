package com.verdantartifice.primalmagick.common.research;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.keys.RuneEnchantmentKey;
import com.verdantartifice.primalmagick.common.research.keys.StackCraftedKey;
import com.verdantartifice.primalmagick.common.research.keys.TagCraftedKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.AndRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ItemStackRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ItemTagRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.KnowledgeRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.StatRequirement;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.stats.Stat;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Definition of a research addendum.  An addendum is an addon to a research entry that is separately
 * unlocked with additional research if the entry itself is already unlocked.  Addenda have their own
 * text and may grant new recipes and attunements to the player.
 * 
 * @author Daedalus4096
 */
public record ResearchAddendum(ResearchEntryKey parentKey, String textTranslationKey, Optional<AbstractRequirement<?>> completionRequirementOpt, 
        List<ResourceLocation> recipes, List<AbstractResearchKey<?>> siblings, SourceList attunements) {
    public static final Codec<ResearchAddendum> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResearchEntryKey.CODEC.fieldOf("parentKey").forGetter(ResearchAddendum::parentKey),
            Codec.STRING.fieldOf("textTranslationKey").forGetter(ResearchAddendum::textTranslationKey),
            AbstractRequirement.CODEC.optionalFieldOf("completionRequirementOpt").forGetter(ResearchAddendum::completionRequirementOpt),
            ResourceLocation.CODEC.listOf().fieldOf("recipes").forGetter(ResearchAddendum::recipes),
            AbstractResearchKey.CODEC.listOf().fieldOf("siblings").forGetter(ResearchAddendum::siblings),
            SourceList.CODEC.optionalFieldOf("attunements", SourceList.EMPTY).forGetter(ResearchAddendum::attunements)
        ).apply(instance, ResearchAddendum::new));
    
    @Nonnull
    public static ResearchAddendum fromNetwork(FriendlyByteBuf buf) {
        ResearchEntryKey parentKey = ResearchEntryKey.fromNetwork(buf);
        String textKey = buf.readUtf();
        Optional<AbstractRequirement<?>> compReqOpt = buf.readOptional(AbstractRequirement::fromNetwork);
        List<ResourceLocation> recipes = buf.readList(b -> b.readResourceLocation());
        List<AbstractResearchKey<?>> siblings = buf.readList(AbstractResearchKey::fromNetwork);
        SourceList attunements = SourceList.fromNetwork(buf);
        return new ResearchAddendum(parentKey, textKey, compReqOpt, recipes, siblings, attunements);
    }
    
    public static void toNetwork(FriendlyByteBuf buf, ResearchAddendum addendum) {
        addendum.parentKey.toNetwork(buf);
        buf.writeUtf(addendum.textTranslationKey);
        buf.writeOptional(addendum.completionRequirementOpt, (b, r) -> r.toNetwork(b));
        buf.writeCollection(addendum.recipes, (b, l) -> b.writeResourceLocation(l));
        buf.writeCollection(addendum.siblings, (b, s) -> s.toNetwork(b));
        SourceList.toNetwork(buf, addendum.attunements);
    }
    
    public static class Builder {
        protected final String modId;
        protected final ResearchEntry.Builder entryBuilder;
        protected final ResearchEntryKey parentKey;
        protected final int addendumIndex;
        protected final List<ResourceLocation> recipes = new ArrayList<>();
        protected final List<AbstractResearchKey<?>> siblings = new ArrayList<>();
        protected final List<AbstractRequirement<?>> requirements = new ArrayList<>();
        protected final SourceList.Builder attunements = SourceList.builder();
        
        public Builder(String modId, ResearchEntry.Builder entryBuilder, ResearchEntryKey parentKey, int addendumIndex) {
            this.modId = Preconditions.checkNotNull(modId);
            this.entryBuilder = Preconditions.checkNotNull(entryBuilder);
            this.parentKey = Preconditions.checkNotNull(parentKey);
            this.addendumIndex = addendumIndex;
        }
        
        public Builder(ResearchEntry.Builder entryBuilder, ResearchEntryKey parentKey, int addendumIndex) {
            this(PrimalMagick.MODID, entryBuilder, parentKey, addendumIndex);
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
        
        public Builder sibling(ResourceKey<ResearchEntry> siblingKey) {
            this.siblings.add(new ResearchEntryKey(siblingKey));
            return this;
        }
        
        public Builder sibling(Enchantment runeEnchantment) {
            this.siblings.add(new RuneEnchantmentKey(runeEnchantment));
            return this;
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
        
        public Builder requiredKnowledge(KnowledgeType type, int levels) {
            return this.requirement(new KnowledgeRequirement(type, levels));
        }
        
        public Builder requiredStat(Stat stat, int value) {
            return this.requirement(new StatRequirement(stat, value));
        }
        
        public Builder attunement(SourceList sources) {
            this.attunements.with(sources);
            return this;
        }
        
        public Builder attunement(Source source, int amount) {
            this.attunements.with(source, amount);
            return this;
        }
        
        protected String getTextTranslationKey() {
            return String.join(".", "research", this.modId.toLowerCase(), this.parentKey.getRootKey().location().getPath().toLowerCase(), "text", "addenda", Integer.toString(this.addendumIndex));
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
            } else if (this.addendumIndex < 0) {
                throw new IllegalStateException("Addendum index must be non-negative");
            }
        }
        
        ResearchAddendum build() {
            this.validate();
            return new ResearchAddendum(this.parentKey, this.getTextTranslationKey(), this.getFinalRequirement(), this.recipes, this.siblings, this.attunements.build());
        }
        
        public ResearchEntry.Builder end() {
            return this.entryBuilder;
        }
    }
}
