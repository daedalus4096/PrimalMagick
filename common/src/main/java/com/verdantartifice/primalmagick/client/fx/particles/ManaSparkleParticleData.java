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
import net.minecraft.util.ExtraCodecs;

/**
 * Particle data for the spiral trail that appears when siphoning a font with a wand.
 *
 * @param color particle color
 * @param lifetime particle lifetime
 * @param phase particle rotation phase
 * @author Daedalus4096
 */
public record ManaSparkleParticleData(int color, int lifetime, double phase) implements ParticleOptions {
    public static final MapCodec<ManaSparkleParticleData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.ARGB_COLOR_CODEC.fieldOf("color").forGetter(ManaSparkleParticleData::color),
            Codec.INT.fieldOf("lifetime").forGetter(ManaSparkleParticleData::lifetime),
            Codec.DOUBLE.fieldOf("phase").forGetter(ManaSparkleParticleData::phase)
        ).apply(instance, ManaSparkleParticleData::new));

    public static final StreamCodec<ByteBuf, ManaSparkleParticleData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ManaSparkleParticleData::color,
            ByteBufCodecs.VAR_INT, ManaSparkleParticleData::lifetime,
            ByteBufCodecs.DOUBLE, ManaSparkleParticleData::phase,
            ManaSparkleParticleData::new);

    public static MapCodec<ManaSparkleParticleData> codec(ParticleType<ManaSparkleParticleData> pParticleType) {
        return CODEC;
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, ManaSparkleParticleData> streamCodec(ParticleType<ManaSparkleParticleData> pParticleType) {
        return STREAM_CODEC;
    }

    @Override
    public ParticleType<ManaSparkleParticleData> getType() {
        return ParticleTypesPM.MANA_SPARKLE.get();
    }
}
