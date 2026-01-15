package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IItemAbilityService;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.loot.CanToolPerformAction;

public class ItemAbilityServiceForge implements IItemAbilityService {
    @Override
    public boolean canAxeStrip(ItemStack stack) {
        return stack.canPerformAction(ToolActions.AXE_STRIP);
    }

    @Override
    public LootItemCondition.Builder makeShearsDigLootCondition() {
        return CanToolPerformAction.canToolPerformAction(ToolActions.SHEARS_DIG);
    }
}
