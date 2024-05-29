package com.verdantartifice.primalmagick.common.runes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.AndRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Class encapsulating a data-defined definition for a rune enchantment.  These definitions specify the
 * runes used to imbue specific enchantments on items at a runescribing altar.
 * 
 * @author Daedalus4096
 */
public record RuneEnchantmentDefinition(Enchantment result, VerbRune verb, NounRune noun, SourceRune source, Optional<AbstractRequirement<?>> requirementOpt) {
    public static final Codec<RuneEnchantmentDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("result").xmap(loc -> ForgeRegistries.ENCHANTMENTS.getValue(loc), ench -> ForgeRegistries.ENCHANTMENTS.getKey(ench)).forGetter(RuneEnchantmentDefinition::result),
            VerbRune.CODEC.fieldOf("verb").forGetter(RuneEnchantmentDefinition::verb),
            NounRune.CODEC.fieldOf("noun").forGetter(RuneEnchantmentDefinition::noun),
            SourceRune.CODEC.fieldOf("source").forGetter(RuneEnchantmentDefinition::source),
            AbstractRequirement.CODEC.optionalFieldOf("requirement").forGetter(RuneEnchantmentDefinition::requirementOpt)
        ).apply(instance, RuneEnchantmentDefinition::new));
    
    public ResourceLocation getId() {
        return ForgeRegistries.ENCHANTMENTS.getKey(this.result);
    }
    
    public List<Rune> getRunes() {
        return List.of(this.verb(), this.noun(), this.source());
    }
    
    public static Builder builder(Enchantment ench) {
        return new Builder(ench);
    }
    
    public static class Builder {
        protected final Enchantment result;
        protected VerbRune verb;
        protected NounRune noun;
        protected SourceRune source;
        protected final List<AbstractRequirement<?>> requirements = new ArrayList<>();
        
        public Builder(Enchantment result) {
            this.result = Preconditions.checkNotNull(result);
        }
        
        public Builder verb(@Nonnull VerbRune verb) {
            this.verb = verb;
            return this;
        }
        
        public Builder noun(@Nonnull NounRune noun) {
            this.noun = noun;
            return this;
        }
        
        public Builder source(@Nonnull SourceRune source) {
            this.source = source;
            return this;
        }
        
        public Builder requirement(AbstractRequirement<?> req) {
            this.requirements.add(req);
            return this;
        }
        
        public Builder requiredResearch(ResourceKey<ResearchEntry> rawKey) {
            return this.requirement(new ResearchRequirement(new ResearchEntryKey(rawKey)));
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
            if (this.verb == null) {
                throw new IllegalStateException("No verb rune for rune enchantment");
            }
            if (this.noun == null) {
                throw new IllegalStateException("No noun rune for rune enchantment");
            }
            if (this.source == null) {
                throw new IllegalStateException("No source rune for rune enchantment");
            }
        }
        
        public RuneEnchantmentDefinition build() {
            this.validate();
            return new RuneEnchantmentDefinition(this.result, this.verb, this.noun, this.source, this.getFinalRequirement());
        }
    }
}
