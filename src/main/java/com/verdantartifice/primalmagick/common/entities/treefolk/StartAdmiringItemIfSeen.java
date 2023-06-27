package com.verdantartifice.primalmagick.common.entities.treefolk;

import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;

public class StartAdmiringItemIfSeen {
    public static BehaviorControl<TreefolkEntity> create(int admireDuration) {
        return BehaviorBuilder.create(treefolkInstance -> {
            return treefolkInstance.group(treefolkInstance.present(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM), treefolkInstance.absent(MemoryModuleType.ADMIRING_ITEM), treefolkInstance.absent(MemoryModuleType.ADMIRING_DISABLED), treefolkInstance.absent(MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM))
                    .apply(treefolkInstance, (nvwiAccessor, admiringItemAccessor, admiringDisabledAccessor, dwtaiAccessor) -> {
                        return (level, entity, gameTime) -> {
                            ItemEntity itemEntity = treefolkInstance.get(nvwiAccessor);
                            if (TreefolkAi.isLovedItem(itemEntity.getItem())) {
                                admiringItemAccessor.setWithExpiry(true, admireDuration);
                                return true;
                            } else {
                                return false;
                            }
                        };
                    });
        });
    }
}
