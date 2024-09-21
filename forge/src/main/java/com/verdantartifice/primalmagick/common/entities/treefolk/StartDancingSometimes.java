package com.verdantartifice.primalmagick.common.entities.treefolk;

import com.verdantartifice.primalmagick.common.entities.ai.memory.MemoryModuleTypesPM;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

@SuppressWarnings("deprecation")
public class StartDancingSometimes {
    public static BehaviorControl<TreefolkEntity> create(int danceDuration, int recentlyCooldown, UniformInt interval) {
        SetEntityLookTargetSometimes.Ticker ticker = new SetEntityLookTargetSometimes.Ticker(interval);
        return BehaviorBuilder.create(treefolkInstance -> {
            return treefolkInstance.group(treefolkInstance.absent(MemoryModuleType.CELEBRATE_LOCATION), treefolkInstance.absent(MemoryModuleType.DANCING), treefolkInstance.absent(MemoryModuleTypesPM.DANCED_RECENTLY.get()), treefolkInstance.registered(MemoryModuleTypesPM.NEARBY_TREEFOLK.get()))
                    .apply(treefolkInstance, (celebrateLocationAccessor, dancingAccessor, dancedRecentlyAccessor, nearbyTreefolkAccessor) -> {
                        return (level, entity, gameTime) -> {
                            if (!ticker.tickDownAndCheck(level.random)) {
                                return false;
                            } else {
                                TreefolkAi.startDanceParty(entity, danceDuration, recentlyCooldown);
                                return true;
                            }
                        };
                    });
        });
    }
}
