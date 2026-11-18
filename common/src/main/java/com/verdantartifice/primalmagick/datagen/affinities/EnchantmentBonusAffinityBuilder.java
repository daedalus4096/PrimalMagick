package com.verdantartifice.primalmagick.datagen.affinities;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.verdantartifice.primalmagick.common.affinities.AbstractAffinity;
import com.verdantartifice.primalmagick.common.affinities.AffinityType;
import com.verdantartifice.primalmagick.common.affinities.AffinityTypesPM;
import com.verdantartifice.primalmagick.common.affinities.EnchantmentBonusAffinity;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.enchantment.Enchantment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class EnchantmentBonusAffinityBuilder {
    protected static final Logger LOGGER = LogManager.getLogger();

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
    
    private void validate(Identifier id) {
        if (this.targetId == null) {
            throw new IllegalStateException("No target enchantment for affinity " + id);
        }
        if (this.registries.lookupOrThrow(Registries.ENCHANTMENT).get(this.targetId).isEmpty()) {
            throw new IllegalStateException("Unknown target enchantment " + this.targetId + " for affinity " + id);
        }
    }
    
    public void build(Consumer<IFinishedAffinity> consumer) {
        this.build(consumer, this.targetId.identifier());
    }
    
    public void build(Consumer<IFinishedAffinity> consumer, String name) {
        this.build(consumer, Identifier.parse(name));
    }
    
    public void build(Consumer<IFinishedAffinity> consumer, Identifier id) {
        this.validate(id);
        consumer.accept(new EnchantmentBonusAffinityBuilder.Result(id, this.targetId, this.multiplierValues.build()));
    }
    
    public static class Result implements IFinishedAffinity {
        protected final Identifier id;
        protected final ResourceKey<Enchantment> targetId;
        protected final SourceList multiplierValues;
        
        public Result(@Nonnull Identifier id, @Nonnull ResourceKey<Enchantment> targetId, @Nullable SourceList multiplierValues) {
            this.id = id;
            this.targetId = targetId;
            this.multiplierValues = multiplierValues == null ? SourceList.EMPTY : multiplierValues;
        }

        @Override
        public AffinityType<?> getType() {
            return AffinityTypesPM.ENCHANTMENT_BONUS.get();
        }

        @Override
        public Identifier getId() {
            return this.id;
        }

        @Override
        public JsonElement serialize() {
            return AbstractAffinity.dispatchCodec().encodeStart(JsonOps.INSTANCE, new EnchantmentBonusAffinity(this.targetId, this.multiplierValues)).resultOrPartial(LOGGER::error).orElseThrow();
        }
    }
}
