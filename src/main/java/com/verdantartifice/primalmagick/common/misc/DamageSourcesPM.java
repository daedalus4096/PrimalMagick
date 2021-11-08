package com.verdantartifice.primalmagick.common.misc;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

/**
 * Definition of mod-specific damage source types.
 * 
 * @author Daedalus4096
 */
public class DamageSourcesPM {
    public static final String HELLISH_CHAIN_TYPE = "primalmagick.hellish_chain";
    public static final String DIRECT_SORCERY_TYPE = "primalmagick.direct_sorcery";
    public static final String INDIRECT_SORCERY_TYPE = "primalmagick.indirect_sorcery";
    
    public static final DamageSource BLEEDING = (new DamageSource("primalmagick.bleeding")).bypassArmor();
    
    public static DamageSource causeHellishChainDamage(LivingEntity mob) {
        return (new EntityDamageSource(HELLISH_CHAIN_TYPE, mob)).setIsFire();
    }
    
    public static DamageSource causeDirectSorceryDamage(LivingEntity caster) {
        return new EntityDamageSource(DIRECT_SORCERY_TYPE, caster).setMagic();
    }
    
    public static DamageSource causeIndirectSorceryDamage(Entity directSource, LivingEntity indirectSource) {
        return new IndirectEntityDamageSource(INDIRECT_SORCERY_TYPE, directSource, indirectSource).setMagic();
    }
}
