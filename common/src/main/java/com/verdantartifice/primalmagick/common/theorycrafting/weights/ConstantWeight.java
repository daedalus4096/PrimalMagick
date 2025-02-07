package com.verdantartifice.primalmagick.common.theorycrafting.weights;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;

/**
 * Weight function that returns a constant value.
 * 
 * @author Daedalus4096
 */
public class ConstantWeight extends AbstractWeightFunction<ConstantWeight> {
    public static final MapCodec<ConstantWeight> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.DOUBLE.fieldOf("weight").forGetter(w -> w.weight)).apply(instance, ConstantWeight::new));
    public static final StreamCodec<ByteBuf, ConstantWeight> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            w -> w.weight,
            ConstantWeight::new);
    
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
}
