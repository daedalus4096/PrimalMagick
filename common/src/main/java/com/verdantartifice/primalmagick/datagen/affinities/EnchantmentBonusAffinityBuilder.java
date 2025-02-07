package com.verdantartifice.primalmagick.datagen.affinities;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.affinities.AffinityType;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class EnchantmentBonusAffinityBuilder {
    protected final HolderLookup.Provider registries;
    protected final ResourceKey<Enchantment> targetId;
    protected SourceList.Builder multiplierValues = SourceList.builder();

    protected EnchantmentBonusAffinityBuilder(HolderLookup.Provider registries, ResourceKey<Enchantment> target) {
        this.registries = registries;
        this.targetId = target;
    }
    
    public static EnchantmentBonusAffinityBuilder enchantmentBonusAffinity(HolderLookup.Provider registries, ResourceKey<Enchantment> target) {
        return new EnchantmentBonusAffinityBuilder(registries, target);
    }
    
    public EnchantmentBonusAffinityBuilder multiplier(SourceList multiplierValues) {
        this.multiplierValues.with(multiplierValues);
        return this;
    }
    
    public EnchantmentBonusAffinityBuilder multiplier(Source source, int amount) {
        this.multiplierValues.with(source, amount);
        return this;
    }
    
    public EnchantmentBonusAffinityBuilder multiplier(Source source) {
        return this.multiplier(source, 1);
    }
    
    private void validate(ResourceLocation id) {
        if (this.targetId == null) {
            throw new IllegalStateException("No target enchantment for affinity " + id.toString());
        }
        if (this.registries.lookupOrThrow(Registries.ENCHANTMENT).get(this.targetId).isEmpty()) {
            throw new IllegalStateException("Unknown target enchantment " + this.targetId.toString() + " for affinity " + id.toString());
        }
    }
    
    public void build(Consumer<IFinishedAffinity> consumer) {
        this.build(consumer, this.targetId.location());
    }
    
    public void build(Consumer<IFinishedAffinity> consumer, String name) {
        this.build(consumer, ResourceLocation.parse(name));
    }
    
    public void build(Consumer<IFinishedAffinity> consumer, ResourceLocation id) {
        this.validate(id);
        consumer.accept(new EnchantmentBonusAffinityBuilder.Result(id, this.targetId.location(), this.multiplierValues.build()));
    }
    
    public static class Result implements IFinishedAffinity {
        protected final ResourceLocation id;
        protected final ResourceLocation targetId;
        protected final SourceList multiplierValues;
        
        public Result(@Nonnull ResourceLocation id, @Nonnull ResourceLocation targetId, @Nullable SourceList multiplierValues) {
            this.id = id;
            this.targetId = targetId;
            this.multiplierValues = multiplierValues;
        }

        @Override
        public AffinityType getType() {
            return AffinityType.ENCHANTMENT_BONUS;
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("target", this.targetId.toString());
            if (this.multiplierValues != null && !this.multiplierValues.isEmpty()) {
                json.add("multiplier", this.multiplierValues.serializeJson());
            }
        }
    }
}
