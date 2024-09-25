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

/**
 * Particle data, specifically target point, for spell bolts
 * 
 * @author Daedalus4096
 */
public class SpellBoltParticleData implements ParticleOptions {
    public static final MapCodec<SpellBoltParticleData> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(
                Codec.DOUBLE.fieldOf("x").forGetter(data -> data.target.x),
                Codec.DOUBLE.fieldOf("y").forGetter(data -> data.target.y),
                Codec.DOUBLE.fieldOf("z").forGetter(data -> data.target.z)
            ).apply(instance, SpellBoltParticleData::new);
    });
    
    public static final StreamCodec<ByteBuf, SpellBoltParticleData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, data -> data.target.x,
            ByteBufCodecs.DOUBLE, data -> data.target.y,
            ByteBufCodecs.DOUBLE, data -> data.target.z,
            SpellBoltParticleData::new);
    
    protected final Vec3 target;
    
    public SpellBoltParticleData(Vec3 target) {
        this(target.x, target.y, target.z);
    }
    
    public SpellBoltParticleData(double targetX, double targetY, double targetZ) {
        this.target = new Vec3(targetX, targetY, targetZ);
    }

    public static MapCodec<SpellBoltParticleData> codec(ParticleType<SpellBoltParticleData> pParticleType) {
        return CODEC;
    }

    public static StreamCodec<? super RegistryFriendlyByteBuf, SpellBoltParticleData> streamCodec(ParticleType<SpellBoltParticleData> pParticleType) {
        return STREAM_CODEC;
    }

    @Override
    public ParticleType<?> getType() {
        return ParticleTypesPM.SPELL_BOLT.get();
    }

    public Vec3 getTargetVec() {
        return this.target;
    }
}
