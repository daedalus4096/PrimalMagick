package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IItemAbilityService;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.loot.CanItemPerformAbility;

public class ItemAbilityServiceNeoforge implements IItemAbilityService {
    @Override
    public boolean canAxeStrip(ItemStack stack) {
        return stack.canPerformAction(ItemAbilities.AXE_STRIP);
    }

    @Override
    public LootItemCondition.Builder makeShearsDigLootCondition() {
        return CanItemPerformAbility.canItemPerformAbility(ItemAbilities.SHEARS_DIG);
    }
}
