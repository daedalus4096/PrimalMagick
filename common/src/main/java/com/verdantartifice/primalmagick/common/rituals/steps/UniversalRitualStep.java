package com.verdantartifice.primalmagick.common.rituals.steps;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

/**
 * Class identifying a single step in a ritual's process, one targeting a nearby universal prop.
 * 
 * @author Daedalus4096
 */
public class UniversalRitualStep extends AbstractRitualStep<UniversalRitualStep> {
    public static final MapCodec<UniversalRitualStep> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(UniversalRitualStep::getPos),
            ResourceLocation.CODEC.fieldOf("expectedId").forGetter(UniversalRitualStep::getExpectedId)
        ).apply(instance, UniversalRitualStep::new));
    
    public static final StreamCodec<ByteBuf, UniversalRitualStep> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, UniversalRitualStep::getPos,
            ResourceLocation.STREAM_CODEC, UniversalRitualStep::getExpectedId,
            UniversalRitualStep::new);
    
    protected final BlockPos pos;
    protected final ResourceLocation expectedId;
    
    public UniversalRitualStep(BlockPos pos, ResourceLocation expectedId) {
        this.pos = pos;
        this.expectedId = expectedId;
    }

    @Override
    public boolean isValid() {
        return this.pos != null && this.expectedId != null;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public ResourceLocation getExpectedId() {
        return this.expectedId;
    }

    @Override
    public RitualStepType<UniversalRitualStep> getType() {
        return RitualStepTypesPM.UNIVERSAL.get();
    }
}
