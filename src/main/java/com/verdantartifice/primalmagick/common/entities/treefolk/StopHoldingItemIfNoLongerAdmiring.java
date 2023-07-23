package com.verdantartifice.primalmagick.common.entities.treefolk;

import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.common.ToolActions;

public class StopHoldingItemIfNoLongerAdmiring {
    public static BehaviorControl<TreefolkEntity> create() {
        return BehaviorBuilder.create(treefolkInstance -> {
            return treefolkInstance.group(treefolkInstance.absent(MemoryModuleType.ADMIRING_ITEM)).apply(treefolkInstance, admiringItemAccessor -> {
                return (level, entity, gameTime) -> {
                    if (!entity.getOffhandItem().isEmpty() && !entity.getOffhandItem().canPerformAction(ToolActions.SHIELD_BLOCK)) {
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
