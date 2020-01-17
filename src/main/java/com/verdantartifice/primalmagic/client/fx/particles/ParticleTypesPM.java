package com.verdantartifice.primalmagic.client.fx.particles;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Object holder for mod particle types.
 * 
 * @author Michael Bunting
 */
@OnlyIn(Dist.CLIENT)
@ObjectHolder(PrimalMagic.MODID)
public class ParticleTypesPM {
    public static final BasicParticleType WAND_POOF = null;
    public static final BasicParticleType MANA_SPARKLE = null;
    public static final BasicParticleType SPELL_SPARKLE = null;
    public static final ParticleType<SpellBoltParticleData> SPELL_BOLT = null;
}
