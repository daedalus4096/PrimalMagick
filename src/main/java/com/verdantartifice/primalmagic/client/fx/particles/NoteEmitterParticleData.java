package com.verdantartifice.primalmagic.client.fx.particles;

import java.util.Locale;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Particle data for note emitter meta-particles.
 * 
 * @author Daedalus4096
 */
public class NoteEmitterParticleData implements IParticleData {
    public static final Codec<NoteEmitterParticleData> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.DOUBLE.fieldOf("hue").forGetter((data) -> {
            return data.hue;
        }), Codec.INT.fieldOf("duration").forGetter((data) -> {
            return data.duration;
        })).apply(instance, NoteEmitterParticleData::new);
    });
    
    @SuppressWarnings("deprecation")
    public static final IParticleData.IDeserializer<NoteEmitterParticleData> DESERIALIZER = new IParticleData.IDeserializer<NoteEmitterParticleData>() {
        @Override
        public NoteEmitterParticleData deserialize(ParticleType<NoteEmitterParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            double hue = reader.readDouble();
            reader.expect(' ');
            int duration = reader.readInt();
            return new NoteEmitterParticleData(hue, duration);
        }

        @Override
        public NoteEmitterParticleData read(ParticleType<NoteEmitterParticleData> particleTypeIn, PacketBuffer buffer) {
            return new NoteEmitterParticleData(buffer.readDouble(), buffer.readInt());
        }
    };
    
    protected final double hue;
    protected final int duration;
    
    public NoteEmitterParticleData(double hue, int duration) {
        this.hue = hue;
        this.duration = duration;
    }
    
    @Override
    public ParticleType<?> getType() {
        return ParticleTypesPM.NOTE_EMITTER.get();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeDouble(this.hue);
        buffer.writeInt(this.duration);
    }

    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%s %d %d", ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()), this.hue, this.duration);
    }

    public double getHue() {
        return this.hue;
    }
    
    public int getDuration() {
        return this.duration;
    }
}
