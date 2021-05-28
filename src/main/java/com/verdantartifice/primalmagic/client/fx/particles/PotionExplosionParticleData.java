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
 * Particle data for potion explosions.
 * 
 * @author Daedalus4096
 */
public class PotionExplosionParticleData implements IParticleData {
    public static final Codec<PotionExplosionParticleData> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.BOOL.fieldOf("instant").forGetter((data) -> {
            return data.isInstant;
        })).apply(instance, PotionExplosionParticleData::new);
    });
    
    @SuppressWarnings("deprecation")
    public static final IParticleData.IDeserializer<PotionExplosionParticleData> DESERIALIZER = new IParticleData.IDeserializer<PotionExplosionParticleData>() {
        @Override
        public PotionExplosionParticleData deserialize(ParticleType<PotionExplosionParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            boolean instant = reader.readBoolean();
            return new PotionExplosionParticleData(instant);
        }

        @Override
        public PotionExplosionParticleData read(ParticleType<PotionExplosionParticleData> particleTypeIn, PacketBuffer buffer) {
            return new PotionExplosionParticleData(buffer.readBoolean());
        }
    };
    
    protected final boolean isInstant;
    
    public PotionExplosionParticleData(boolean isInstant) {
        this.isInstant = isInstant;
    }

    @Override
    public ParticleType<?> getType() {
        return ParticleTypesPM.POTION_EXPLOSION.get();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeBoolean(this.isInstant);
    }

    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%s %b", ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()), this.isInstant);
    }

    public boolean isInstant() {
        return this.isInstant;
    }
}
