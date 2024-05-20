package com.verdantartifice.primalmagick.common.theorycrafting.weights;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.mutable.MutableDouble;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;

/**
 * Weight function that returns a progressing value based on a player's completed research.
 * 
 * @author Daedalus4096
 */
public class ProgressiveWeight extends AbstractWeightFunction<ProgressiveWeight> {
    public static final Codec<ProgressiveWeight> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("startingWeight").forGetter(w -> w.startingWeight),
            Modifier.CODEC.listOf().fieldOf("modifiers").forGetter(w -> w.modifiers)
        ).apply(instance, ProgressiveWeight::new));

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
    
    @Nonnull
    public static ProgressiveWeight fromNetworkInner(FriendlyByteBuf buf) {
        double start = buf.readDouble();
        List<Modifier> modifiers = buf.readList(Modifier::fromNetwork);
        return new ProgressiveWeight(start, modifiers);
    }

    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeDouble(this.startingWeight);
        buf.writeCollection(this.modifiers, (b, m) -> m.toNetwork(b));
    }

    protected static record Modifier(ResearchEntryKey researchKey, double weightModifier) {
        public static final Codec<Modifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceKey.codec(RegistryKeysPM.RESEARCH_ENTRIES).fieldOf("researchKey").xmap(ResearchEntryKey::new, ResearchEntryKey::getRootKey).forGetter(Modifier::researchKey), 
                Codec.DOUBLE.fieldOf("weightModifier").forGetter(Modifier::weightModifier)
            ).apply(instance, Modifier::new));
        
        public Modifier(ResourceKey<ResearchEntry> rawKey, double weightModifier) {
            this(new ResearchEntryKey(rawKey), weightModifier);
        }
        
        public static Modifier fromNetwork(FriendlyByteBuf buf) {
            return new Modifier(buf.readResourceKey(RegistryKeysPM.RESEARCH_ENTRIES), buf.readDouble());
        }
        
        public void toNetwork(FriendlyByteBuf buf) {
            buf.writeResourceKey(this.researchKey.getRootKey());
            buf.writeDouble(this.weightModifier);
        }
    }
    
    public static class Builder {
        protected final double startingWeight;
        protected final List<Modifier> modifiers = new ArrayList<>();
        
        public Builder(double startingWeight) {
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
