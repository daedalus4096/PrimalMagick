package com.verdantartifice.primalmagick.common.entities.treefolk;

import com.verdantartifice.primalmagick.common.entities.ai.memory.MemoryModuleTypesPM;

import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class JoinDanceParty {
    public static BehaviorControl<TreefolkEntity> create(int danceDuration, int danceCooldown) {
        return BehaviorBuilder.create(treefolkInstance -> {
            return treefolkInstance.group(treefolkInstance.present(MemoryModuleType.CELEBRATE_LOCATION), treefolkInstance.absent(MemoryModuleType.DANCING), treefolkInstance.registered(MemoryModuleTypesPM.DANCED_RECENTLY.get()))
                    .apply(treefolkInstance, (celebrateLocationAccessor, dancingAccessor, dancedRecentlyAccessor) -> {
                        return (level, entity, gameTime) -> {
                            dancingAccessor.setWithExpiry(true, danceDuration);
                            dancedRecentlyAccessor.setWithExpiry(true, danceCooldown);
                            return true;
                        };
                    });
        });
    }
}
