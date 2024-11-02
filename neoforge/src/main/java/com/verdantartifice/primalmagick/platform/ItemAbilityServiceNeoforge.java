package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IItemAbilityService;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbilities;

public class ItemAbilityServiceNeoforge implements IItemAbilityService {
    @Override
    public boolean canAxeStrip(ItemStack stack) {
        return stack.canPerformAction(ItemAbilities.AXE_STRIP);
    }

    @Override
    public boolean canShieldBlock(ItemStack stack) {
        return stack.canPerformAction(ItemAbilities.SHIELD_BLOCK);
    }
}
