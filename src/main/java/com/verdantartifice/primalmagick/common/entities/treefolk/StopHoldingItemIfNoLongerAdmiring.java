package com.verdantartifice.primalmagick.common.entities.treefolk;

import com.google.common.collect.ImmutableMap;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class StopHoldingItemIfNoLongerAdmiring<E extends TreefolkEntity> extends Behavior<E> {
    public StopHoldingItemIfNoLongerAdmiring() {
        super(ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM, MemoryStatus.VALUE_ABSENT));
    }

    @Override
    protected void start(ServerLevel pLevel, E pEntity, long pGameTime) {
        TreefolkAi.stopHoldingOffHandItem(pEntity, true);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, E pOwner) {
        return !pOwner.getOffhandItem().isEmpty() && !pOwner.getOffhandItem().canPerformAction(net.minecraftforge.common.ToolActions.SHIELD_BLOCK);
    }
}
