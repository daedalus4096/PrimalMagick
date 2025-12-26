package com.verdantartifice.primalmagick.common.damagesource;

import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

/**
 * Helper class for creating damage sources from mod damage types.
 * 
 * @author Daedalus4096
 */
public class DamageSourcesPM {
    public static DamageSource bleeding(RegistryAccess registryAccess) {
        return new DamageSource(registryAccess.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypesPM.BLEEDING));
    }
    
    public static DamageSource bloodRose(RegistryAccess registryAccess) {
        return new DamageSource(registryAccess.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypesPM.BLOOD_ROSE));
    }
    
    public static DamageSource hellishChain(RegistryAccess registryAccess, LivingEntity directSource) {
        return new DamageSource(registryAccess.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypesPM.HELLISH_CHAIN), directSource);
    }
    
    public static DamageSource sorcery(RegistryAccess registryAccess, Source source, LivingEntity directSource) {
        return new DamageSource(registryAccess.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypesPM.getSorceryType(source)), directSource);
    }
    
    public static DamageSource sorcery(RegistryAccess registryAccess, Source source, Entity directSource, LivingEntity indirectSource) {
        return new DamageSource(registryAccess.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypesPM.getSorceryType(source)), directSource, indirectSource);
    }
}
