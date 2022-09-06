package com.verdantartifice.primalmagick.common.entities.ai.sensing;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;

/**
 * AI sensor specific to custom treefolk needs.
 * 
 * @author Daedalus4096
 */
public class TreefolkSpecificSensor extends Sensor<LivingEntity> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        // TODO Auto-generated method stub
        return ImmutableSet.of();
    }

    @Override
    protected void doTick(ServerLevel pLevel, LivingEntity pEntity) {
        // TODO Auto-generated method stub
        
    }
}
