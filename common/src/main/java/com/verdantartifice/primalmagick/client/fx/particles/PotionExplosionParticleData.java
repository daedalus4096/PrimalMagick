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
 * Particle data for potion explosions.
 * 
 * @author Daedalus4096
 */
public class PotionExplosionParticleData implements ParticleOptions {
    public static final MapCodec<PotionExplosionParticleData> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(Codec.BOOL.fieldOf("instant").forGetter(data -> data.isInstant)).apply(instance, PotionExplosionParticleData::new);
    });
    
    public static final StreamCodec<ByteBuf, PotionExplosionParticleData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, data -> data.isInstant,
            PotionExplosionParticleData::new);
    
    protected final boolean isInstant;
    
    public PotionExplosionParticleData(boolean isInstant) {
        this.isInstant = isInstant;
    }

    public static MapCodec<PotionExplosionParticleData> codec(ParticleType<PotionExplosionParticleData> pParticleType) {
        return CODEC;
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, PotionExplosionParticleData> streamCodec(ParticleType<PotionExplosionParticleData> pParticleType) {
        return STREAM_CODEC;
    }

    @Override
    public ParticleType<?> getType() {
        return ParticleTypesPM.POTION_EXPLOSION.get();
    }

    public boolean isInstant() {
        return this.isInstant;
    }
}
