package com.verdantartifice.primalmagick.datagen.affinities;

import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.affinities.AffinityType;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentBonusAffinityBuilder {
    protected final ResourceLocation targetId;
    protected SourceList.Builder multiplierValues = SourceList.builder();

    protected EnchantmentBonusAffinityBuilder(@Nonnull Enchantment target) {
        this.targetId = ForgeRegistries.ENCHANTMENTS.getKey(target);
    }
    
    public static EnchantmentBonusAffinityBuilder enchantmentBonusAffinity(@Nonnull Enchantment target) {
        return new EnchantmentBonusAffinityBuilder(target);
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
        if (!ForgeRegistries.ENCHANTMENTS.containsKey(this.targetId)) {
            throw new IllegalStateException("Unknown target enchantment " + this.targetId.toString() + " for affinity " + id.toString());
        }
    }
    
    public void build(Consumer<IFinishedAffinity> consumer) {
        this.build(consumer, this.targetId);
    }
    
    public void build(Consumer<IFinishedAffinity> consumer, String name) {
        this.build(consumer, new ResourceLocation(name));
    }
    
    public void build(Consumer<IFinishedAffinity> consumer, ResourceLocation id) {
        this.validate(id);
        consumer.accept(new EnchantmentBonusAffinityBuilder.Result(id, this.targetId, this.multiplierValues.build()));
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
