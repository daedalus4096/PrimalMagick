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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Particle data, specifically target point, for spell bolts
 * 
 * @author Daedalus4096
 */
public record SpellBoltParticleData(Vec3 target, int color) implements ParticleOptions {
    public static final MapCodec<SpellBoltParticleData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Vec3.CODEC.fieldOf("target").forGetter(SpellBoltParticleData::target),
            Codec.INT.fieldOf("color").forGetter(SpellBoltParticleData::color)
        ).apply(instance, SpellBoltParticleData::new));
    
    public static final StreamCodec<ByteBuf, SpellBoltParticleData> STREAM_CODEC = StreamCodec.composite(
            Vec3.STREAM_CODEC, SpellBoltParticleData::target,
            ByteBufCodecs.VAR_INT, SpellBoltParticleData::color,
            SpellBoltParticleData::new);
    
    public static MapCodec<SpellBoltParticleData> codec(ParticleType<SpellBoltParticleData> pParticleType) {
        return CODEC;
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, SpellBoltParticleData> streamCodec(ParticleType<SpellBoltParticleData> pParticleType) {
        return STREAM_CODEC;
    }

    @Override
    @NotNull
    public ParticleType<?> getType() {
        return ParticleTypesPM.SPELL_BOLT.get();
    }
}
