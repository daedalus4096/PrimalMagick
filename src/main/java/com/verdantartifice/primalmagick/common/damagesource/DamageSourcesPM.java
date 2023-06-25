package com.verdantartifice.primalmagick.common.damagesource;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

/**
 * Helper class for creating damage sources from mod damage types.
 * 
 * @author Daedalus4096
 */
public class DamageSourcesPM {
    public static DamageSource bleeding(Level level) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypesPM.BLEEDING));
    }
    
    public static DamageSource hellishChain(Level level, LivingEntity directSource) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypesPM.HELLISH_CHAIN), directSource);
    }
    
    public static DamageSource sorcery(Level level, LivingEntity directSource) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypesPM.SORCERY), directSource);
    }
    
    public static DamageSource sorcery(Level level, Entity directSource, LivingEntity indirectSource) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypesPM.SORCERY), directSource, indirectSource);
    }
}
