package com.verdantartifice.primalmagick.client.fx.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * Particle data for note emitter meta-particles.
 * 
 * @author Daedalus4096
 */
public class NoteEmitterParticleData implements ParticleOptions {
    public static final MapCodec<NoteEmitterParticleData> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(
                Codec.DOUBLE.fieldOf("hue").forGetter(data -> data.hue),
                Codec.INT.fieldOf("duration").forGetter(data -> data.duration)
            ).apply(instance, NoteEmitterParticleData::new);
    });
    
    public static final StreamCodec<ByteBuf, NoteEmitterParticleData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, data -> data.hue,
            ByteBufCodecs.VAR_INT, data -> data.duration,
            NoteEmitterParticleData::new);
    
    protected final double hue;
    protected final int duration;
    
    public NoteEmitterParticleData(double hue, int duration) {
        this.hue = hue;
        this.duration = duration;
    }
    
    public static MapCodec<NoteEmitterParticleData> codec(ParticleType<NoteEmitterParticleData> pParticleType) {
        return CODEC;
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, NoteEmitterParticleData> streamCodec(ParticleType<NoteEmitterParticleData> pParticleType) {
        return STREAM_CODEC;
    }

    @Override
    public ParticleType<?> getType() {
        return ParticleTypesPM.NOTE_EMITTER.get();
    }

    public double getHue() {
        return this.hue;
    }
    
    public int getDuration() {
        return this.duration;
    }
}
