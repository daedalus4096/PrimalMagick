package com.verdantartifice.primalmagick.common.theorycrafting.weights;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

/**
 * Weight function that returns a constant value.
 * 
 * @author Daedalus4096
 */
public class ConstantWeight extends AbstractWeightFunction<ConstantWeight> {
    public static final Codec<ConstantWeight> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.DOUBLE.fieldOf("weight").forGetter(w -> w.weight)).apply(instance, ConstantWeight::new));
    
    private final double weight;
    
    public ConstantWeight(double weight) {
        this.weight = weight;
    }
    
    @Override
    protected WeightFunctionType<ConstantWeight> getType() {
        return WeightFunctionTypesPM.CONSTANT.get();
    }

    @Override
    public double getWeight(Player t) {
        return this.weight;
    }
    
    @Nonnull
    public static ConstantWeight fromNetworkInner(FriendlyByteBuf buf) {
        return new ConstantWeight(buf.readDouble());
    }

    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeDouble(this.weight);
    }
}
