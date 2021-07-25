package com.verdantartifice.primalmagic.client.fx.particles;

import java.util.Locale;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Particle data, specifically target point, for spell bolts
 * 
 * @author Daedalus4096
 */
public class SpellBoltParticleData implements ParticleOptions {
    public static final Codec<SpellBoltParticleData> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.DOUBLE.fieldOf("x").forGetter((data) -> {
            return data.target.x;
        }), Codec.DOUBLE.fieldOf("y").forGetter((data) -> {
            return data.target.y;
        }), Codec.DOUBLE.fieldOf("z").forGetter((data) -> {
            return data.target.z;
        })).apply(instance, SpellBoltParticleData::new);
    });
    
    @SuppressWarnings("deprecation")
    public static final ParticleOptions.Deserializer<SpellBoltParticleData> DESERIALIZER = new ParticleOptions.Deserializer<SpellBoltParticleData>() {
        @Override
        public SpellBoltParticleData fromCommand(ParticleType<SpellBoltParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            double x = reader.readDouble();
            reader.expect(' ');
            double y = reader.readDouble();
            reader.expect(' ');
            double z = reader.readDouble();
            return new SpellBoltParticleData(x, y, z);
        }

        @Override
        public SpellBoltParticleData fromNetwork(ParticleType<SpellBoltParticleData> particleTypeIn, FriendlyByteBuf buffer) {
            return new SpellBoltParticleData(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        }
    };
    
    protected final Vec3 target;
    
    public SpellBoltParticleData(Vec3 target) {
        this(target.x, target.y, target.z);
    }
    
    public SpellBoltParticleData(double targetX, double targetY, double targetZ) {
        this.target = new Vec3(targetX, targetY, targetZ);
    }

    @Override
    public ParticleType<?> getType() {
        return ParticleTypesPM.SPELL_BOLT.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeDouble(this.target.x);
        buffer.writeDouble(this.target.y);
        buffer.writeDouble(this.target.z);
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %d %d %d", ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()), this.target.x, this.target.y, this.target.z);
    }

    public Vec3 getTargetVec() {
        return this.target;
    }
}
