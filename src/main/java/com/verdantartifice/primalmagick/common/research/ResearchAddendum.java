package com.verdantartifice.primalmagick.common.research;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.AndRequirement;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
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
        List<ResourceLocation> recipes, List<ResearchEntryKey> siblings, SourceList attunements) {
    public static final Codec<ResearchAddendum> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResearchEntryKey.CODEC.fieldOf("parentKey").forGetter(ResearchAddendum::parentKey),
            Codec.STRING.fieldOf("textTranslationKey").forGetter(ResearchAddendum::textTranslationKey),
            AbstractRequirement.CODEC.optionalFieldOf("completionRequirementOpt").forGetter(ResearchAddendum::completionRequirementOpt),
            ResourceLocation.CODEC.listOf().fieldOf("recipes").forGetter(ResearchAddendum::recipes),
            ResearchEntryKey.CODEC.listOf().fieldOf("siblings").forGetter(ResearchAddendum::siblings),
            SourceList.CODEC.optionalFieldOf("attunements", SourceList.EMPTY).forGetter(ResearchAddendum::attunements)
        ).apply(instance, ResearchAddendum::new));
    
    @Nonnull
    public static ResearchAddendum fromNetwork(FriendlyByteBuf buf) {
        ResearchEntryKey parentKey = ResearchEntryKey.fromNetwork(buf);
        String textKey = buf.readUtf();
        Optional<AbstractRequirement<?>> compReqOpt = buf.readOptional(AbstractRequirement::fromNetwork);
        List<ResourceLocation> recipes = buf.readList(b -> b.readResourceLocation());
        List<ResearchEntryKey> siblings = buf.readList(ResearchEntryKey::fromNetwork);
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
        protected final ResearchEntryKey parentKey;
        protected final int addendumIndex;
        protected final List<ResourceLocation> recipes = new ArrayList<>();
        protected final List<ResearchEntryKey> siblings = new ArrayList<>();
        protected final List<AbstractRequirement<?>> requirements = new ArrayList<>();
        protected final SourceList.Builder attunements = SourceList.builder();
        
        public Builder(String modId, ResearchEntryKey parentKey, int addendumIndex) {
            this.modId = modId;
            this.parentKey = parentKey;
            this.addendumIndex = addendumIndex;
        }
        
        public Builder(ResearchEntryKey parentKey, int addendumIndex) {
            this(PrimalMagick.MODID, parentKey, addendumIndex);
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
        
        public Builder requirement(AbstractRequirement<?> req) {
            this.requirements.add(req);
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
        
        protected String getTextTranslationKey() {
            return String.join(".", "research", this.modId.toLowerCase(), this.parentKey.getRootKey().toLowerCase(), "text", "addenda", Integer.toString(this.addendumIndex));
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
            if (this.modId == null || this.modId.isBlank()) {
                throw new IllegalStateException("Mod ID may not be empty");
            } else if (this.parentKey == null) {
                throw new IllegalStateException("Parent entry key may not be null");
            } else if (this.addendumIndex < 0) {
                throw new IllegalStateException("Addendum index must be non-negative");
            }
        }
        
        public ResearchAddendum build() {
            this.validate();
            return new ResearchAddendum(this.parentKey, this.getTextTranslationKey(), this.getFinalRequirement(), this.recipes, this.siblings, this.attunements.build());
        }
    }
}
