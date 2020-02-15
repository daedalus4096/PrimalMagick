package com.verdantartifice.primalmagic.client.fx.particles;

import java.util.Locale;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

/**
 * Particle data, specifically target point, for spell bolts
 * 
 * @author Daedalus4096
 */
public class SpellBoltParticleData implements IParticleData {
    public static final IParticleData.IDeserializer<SpellBoltParticleData> DESERIALIZER = new IParticleData.IDeserializer<SpellBoltParticleData>() {
        @Override
        public SpellBoltParticleData deserialize(ParticleType<SpellBoltParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            double x = reader.readDouble();
            reader.expect(' ');
            double y = reader.readDouble();
            reader.expect(' ');
            double z = reader.readDouble();
            return new SpellBoltParticleData(x, y, z);
        }

        @Override
        public SpellBoltParticleData read(ParticleType<SpellBoltParticleData> particleTypeIn, PacketBuffer buffer) {
            return new SpellBoltParticleData(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        }
    };
    
    protected final Vec3d target;
    
    public SpellBoltParticleData(Vec3d target) {
        this(target.x, target.y, target.z);
    }
    
    public SpellBoltParticleData(double targetX, double targetY, double targetZ) {
        this.target = new Vec3d(targetX, targetY, targetZ);
    }

    @Override
    public ParticleType<?> getType() {
        return ParticleTypesPM.SPELL_BOLT.get();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeDouble(this.target.x);
        buffer.writeDouble(this.target.y);
        buffer.writeDouble(this.target.z);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%s %d %d %d", Registry.PARTICLE_TYPE.getKey(this.getType()), this.target.x, this.target.y, this.target.z);
    }

    public Vec3d getTargetVec() {
        return this.target;
    }
}
