package com.verdantartifice.primalmagic.common.misc;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;

/**
 * Definition of mod-specific damage source types.
 * 
 * @author Daedalus4096
 */
public class DamageSourcesPM {
    public static final String HELLISH_CHAIN_TYPE = "primalmagic.hellish_chain";
    
    public static final DamageSource BLEEDING = (new DamageSource("primalmagic.bleeding")).bypassArmor();
    
    public static DamageSource causeHellishChainDamage(LivingEntity mob) {
        return (new EntityDamageSource(HELLISH_CHAIN_TYPE, mob)).setIsFire();
    }
}
