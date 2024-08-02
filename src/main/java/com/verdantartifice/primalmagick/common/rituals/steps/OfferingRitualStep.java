package com.verdantartifice.primalmagick.common.rituals.steps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * Class identifying a single step in a ritual's process, one consuming an offering per the recipe.
 * 
 * @author Daedalus4096
 */
public class OfferingRitualStep extends AbstractRitualStep<OfferingRitualStep> {
    public static final MapCodec<OfferingRitualStep> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("index").forGetter(OfferingRitualStep::getIndex)
        ).apply(instance, OfferingRitualStep::new));
    
    public static final StreamCodec<ByteBuf, OfferingRitualStep> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, OfferingRitualStep::getIndex,
            OfferingRitualStep::new);
    
    protected final int index;
    
    public OfferingRitualStep(int index) {
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
    public RitualStepType<OfferingRitualStep> getType() {
        return RitualStepTypesPM.OFFERING.get();
    }
}
