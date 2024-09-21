package com.verdantartifice.primalmagick.common.theorycrafting.weights;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableDouble;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;

/**
 * Weight function that returns a progressing value based on a player's completed research.
 * 
 * @author Daedalus4096
 */
public class ProgressiveWeight extends AbstractWeightFunction<ProgressiveWeight> {
    public static final MapCodec<ProgressiveWeight> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.DOUBLE.fieldOf("startingWeight").forGetter(w -> w.startingWeight),
            Modifier.CODEC.codec().listOf().fieldOf("modifiers").forGetter(w -> w.modifiers)
        ).apply(instance, ProgressiveWeight::new));
    
    public static final StreamCodec<ByteBuf, ProgressiveWeight> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            w -> w.startingWeight,
            Modifier.STREAM_CODEC.apply(ByteBufCodecs.list()),
            w -> w.modifiers,
            ProgressiveWeight::new);

    private final double startingWeight;
    private final List<Modifier> modifiers;
    
    protected ProgressiveWeight(double startingWeight, List<Modifier> modifiers) {
        this.startingWeight = startingWeight;
        this.modifiers = ImmutableList.copyOf(modifiers);
    }

    @Override
    protected WeightFunctionType<ProgressiveWeight> getType() {
        return WeightFunctionTypesPM.PROGRESSIVE.get();
    }

    @Override
    public double getWeight(Player player) {
        MutableDouble retVal = new MutableDouble(this.startingWeight);
        this.modifiers.stream().filter(m -> m.researchKey.isKnownBy(player)).forEach(m -> retVal.add(m.weightModifier()));
        return Math.max(0D, retVal.doubleValue());
    }
    
    protected static record Modifier(ResearchEntryKey researchKey, double weightModifier) {
        public static final MapCodec<Modifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                ResearchEntryKey.CODEC.fieldOf("researchKey").forGetter(Modifier::researchKey), 
                Codec.DOUBLE.fieldOf("weightModifier").forGetter(Modifier::weightModifier)
            ).apply(instance, Modifier::new));
        
        public static final StreamCodec<ByteBuf, Modifier> STREAM_CODEC = StreamCodec.composite(
                ResearchEntryKey.STREAM_CODEC,
                Modifier::researchKey,
                ByteBufCodecs.DOUBLE,
                Modifier::weightModifier,
                Modifier::new);
        
        public Modifier(ResourceKey<ResearchEntry> rawKey, double weightModifier) {
            this(new ResearchEntryKey(rawKey), weightModifier);
        }
    }
    
    public static Builder builder(double startingWeight) {
        return new Builder(startingWeight);
    }
    
    public static class Builder {
        protected final double startingWeight;
        protected final List<Modifier> modifiers = new ArrayList<>();
        
        protected Builder(double startingWeight) {
            this.startingWeight = startingWeight;
        }
        
        public Builder modifier(ResourceKey<ResearchEntry> rawKey, double weightModifier) {
            this.modifiers.add(new Modifier(rawKey, weightModifier));
            return this;
        }
        
        private void validate() {
            if (this.startingWeight < 0) {
                throw new IllegalStateException("Progressive weight start value must be non-negative");
            } else if (this.modifiers.isEmpty()) {
                throw new IllegalStateException("Progressive weight must have at least one modifier; did you mean to use a constant weight instead?");
            }
        }
        
        public ProgressiveWeight build() {
            this.validate();
            return new ProgressiveWeight(this.startingWeight, this.modifiers);
        }
    }
}
