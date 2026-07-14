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
import org.jetbrains.annotations.NotNull;

/**
 * Particle data for potion explosions.
 * 
 * @author Daedalus4096
 */
public class PotionExplosionParticleData implements ParticleOptions {
    public static final MapCodec<PotionExplosionParticleData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("color").forGetter(data -> data.color),
            Codec.BOOL.fieldOf("instant").forGetter(data -> data.isInstant)
        ).apply(instance, PotionExplosionParticleData::new));

    public static final StreamCodec<ByteBuf, PotionExplosionParticleData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, data -> data.color,
            ByteBufCodecs.BOOL, data -> data.isInstant,
            PotionExplosionParticleData::new);

    protected final int color;
    protected final boolean isInstant;
    
    public PotionExplosionParticleData(int color, boolean isInstant) {
        this.color = color;
        this.isInstant = isInstant;
    }

    public static MapCodec<PotionExplosionParticleData> codec(ParticleType<PotionExplosionParticleData> pParticleType) {
        return CODEC;
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, PotionExplosionParticleData> streamCodec(ParticleType<PotionExplosionParticleData> pParticleType) {
        return STREAM_CODEC;
    }

    @Override
    @NotNull
    public ParticleType<?> getType() {
        return ParticleTypesPM.POTION_EXPLOSION.get();
    }

    public int getColor() {
        return this.color;
    }

    public boolean isInstant() {
        return this.isInstant;
    }
}
