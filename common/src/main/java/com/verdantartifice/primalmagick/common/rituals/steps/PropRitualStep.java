package com.verdantartifice.primalmagick.common.rituals.steps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * Class identifying a single step in a ritual's process, one activating a prop per the recipe.
 * 
 * @author Daedalus4096
 */
public class PropRitualStep extends AbstractRitualStep<PropRitualStep> {
    public static final MapCodec<PropRitualStep> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("index").forGetter(PropRitualStep::getIndex)
        ).apply(instance, PropRitualStep::new));
    
    public static final StreamCodec<ByteBuf, PropRitualStep> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, PropRitualStep::getIndex,
            PropRitualStep::new);
    
    protected final int index;
    
    public PropRitualStep(int index) {
        this.index = index;
    }
    
    public int getIndex() {
        return this.index;
    }

    @Override
    public boolean isValid() {
        return this.index >= 0;
    }

    @Override
    public RitualStepType<PropRitualStep> getType() {
        return RitualStepTypesPM.PROP.get();
    }
}
