package com.verdantartifice.primalmagick.datagen.affinities;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.verdantartifice.primalmagick.common.affinities.AbstractAffinity;
import com.verdantartifice.primalmagick.common.affinities.AffinityType;
import com.verdantartifice.primalmagick.common.affinities.AffinityTypesPM;
import com.verdantartifice.primalmagick.common.affinities.EnchantmentBonusAffinity;
import com.verdantartifice.primalmagick.common.affinities.PotionBonusAffinity;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.alchemy.Potion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class PotionBonusAffinityBuilder {
    protected static final Logger LOGGER = LogManager.getLogger();

    protected final ResourceKey<Potion> targetId;
    protected SourceList.Builder bonusValues = SourceList.builder();
    
    protected PotionBonusAffinityBuilder(@Nonnull Holder<Potion> target) {
        this.targetId = BuiltInRegistries.POTION.getResourceKey(target.value()).orElseThrow();
    }
    
    public static PotionBonusAffinityBuilder potionBonusAffinity(@Nonnull Holder<Potion> target) {
        return new PotionBonusAffinityBuilder(target);
    }
    
    public PotionBonusAffinityBuilder bonus(SourceList bonusValues) {
        this.bonusValues.with(bonusValues);
        return this;
    }
    
    public PotionBonusAffinityBuilder bonus(Source source, int amount) {
        this.bonusValues.with(source, amount);
        return this;
    }
    
    private void validate(Identifier id) {
        if (this.targetId == null) {
            throw new IllegalStateException("No target potion for affinity " + id);
        }
        if (!BuiltInRegistries.POTION.containsKey(this.targetId)) {
            throw new IllegalStateException("Unknown target potion " + this.targetId + " for affinity " + id);
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
        consumer.accept(new PotionBonusAffinityBuilder.Result(id, this.targetId, this.bonusValues.build()));
    }
    
    public static class Result implements IFinishedAffinity {
        protected final Identifier id;
        protected final ResourceKey<Potion> targetId;
        protected final SourceList bonusValues;
        
        public Result(@Nonnull Identifier id, @Nonnull ResourceKey<Potion> targetId, @Nullable SourceList bonusValues) {
            this.id = id;
            this.targetId = targetId;
            this.bonusValues = bonusValues == null ? SourceList.EMPTY : bonusValues;
        }

        @Override
        public AffinityType<?> getType() {
            return AffinityTypesPM.POTION_BONUS.get();
        }

        @Override
        public Identifier getId() {
            return this.id;
        }

        @Override
        public JsonElement serialize() {
            return AbstractAffinity.dispatchCodec().encodeStart(JsonOps.INSTANCE, new PotionBonusAffinity(this.targetId, this.bonusValues)).resultOrPartial(LOGGER::error).orElseThrow();
        }
    }
}
