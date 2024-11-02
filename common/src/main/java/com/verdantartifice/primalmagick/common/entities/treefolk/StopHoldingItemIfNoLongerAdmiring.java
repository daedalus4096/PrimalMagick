package com.verdantartifice.primalmagick.common.entities.treefolk;

import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class StopHoldingItemIfNoLongerAdmiring {
    public static BehaviorControl<TreefolkEntity> create() {
        return BehaviorBuilder.create(treefolkInstance -> {
            return treefolkInstance.group(treefolkInstance.absent(MemoryModuleType.ADMIRING_ITEM)).apply(treefolkInstance, admiringItemAccessor -> {
                return (level, entity, gameTime) -> {
                    if (!entity.getOffhandItem().isEmpty() && !Services.ITEM_ABILITIES.canShieldBlock(entity.getOffhandItem())) {
                        TreefolkAi.stopHoldingOffHandItem(entity, true);
                        return true;
                    } else {
                        return false;
                    }
                };
            });
        });
    }
}
