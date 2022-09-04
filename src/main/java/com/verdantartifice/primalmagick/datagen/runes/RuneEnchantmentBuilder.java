package com.verdantartifice.primalmagick.datagen.runes;

import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.runes.NounRune;
import com.verdantartifice.primalmagick.common.runes.SourceRune;
import com.verdantartifice.primalmagick.common.runes.VerbRune;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

public class RuneEnchantmentBuilder {
    protected final Enchantment result;
    protected CompoundResearchKey requiredResearch;
    protected VerbRune verb;
    protected NounRune noun;
    protected SourceRune source;
    
    protected RuneEnchantmentBuilder(@Nonnull Enchantment result) {
        this.result = result;
    }
    
    public static RuneEnchantmentBuilder enchantment(@Nonnull Enchantment result) {
        return new RuneEnchantmentBuilder(result);
    }
    
    public RuneEnchantmentBuilder verb(@Nonnull VerbRune verb) {
        this.verb = verb;
        return this;
    }
    
    public RuneEnchantmentBuilder noun(@Nonnull NounRune noun) {
        this.noun = noun;
        return this;
    }
    
    public RuneEnchantmentBuilder source(@Nonnull SourceRune source) {
        this.source = source;
        return this;
    }
    
    public RuneEnchantmentBuilder requiredResearch(@Nullable CompoundResearchKey research) {
        this.requiredResearch = research;
        return this;
    }
    
    private void validate(ResourceLocation id) {
        if (this.result == null) {
            throw new IllegalStateException("No result for rune enchantment " + id.toString());
        }
        if (this.verb == null) {
            throw new IllegalStateException("No verb rune for rune enchantment " + id.toString());
        }
        if (this.noun == null) {
            throw new IllegalStateException("No noun rune for rune enchantment " + id.toString());
        }
        if (this.source == null) {
            throw new IllegalStateException("No source rune for rune enchantment " + id.toString());
        }
    }
    
    public void build(Consumer<IFinishedRuneEnchantment> consumer) {
        this.validate(ForgeRegistries.ENCHANTMENTS.getKey(this.result));
        consumer.accept(new RuneEnchantmentBuilder.Result(this.result, this.verb, this.noun, this.source, this.requiredResearch));
    }
    
    public static class Result implements IFinishedRuneEnchantment {
        protected final Enchantment result;
        protected final CompoundResearchKey requiredResearch;
        protected final VerbRune verb;
        protected final NounRune noun;
        protected final SourceRune source;
        
        public Result(@Nonnull Enchantment result, @Nonnull VerbRune verb, @Nonnull NounRune noun, @Nonnull SourceRune source, @Nullable CompoundResearchKey requiredResearch) {
            this.result = result;
            this.verb = verb;
            this.noun = noun;
            this.source = source;
            this.requiredResearch = requiredResearch;
        }

        @Override
        public ResourceLocation getId() {
            return ForgeRegistries.ENCHANTMENTS.getKey(this.result);
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("result", this.getId().toString());
            json.addProperty("verb", this.verb.getId().toString());
            json.addProperty("noun", this.noun.getId().toString());
            json.addProperty("source", this.source.getId().toString());
            if (this.requiredResearch != null) {
                json.addProperty("required_research", this.requiredResearch.toString());
            }
        }
    }
}
