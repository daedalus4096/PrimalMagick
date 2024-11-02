package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IItemAbilityService;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolActions;

public class ItemAbilityServiceForge implements IItemAbilityService {
    @Override
    public boolean canAxeStrip(ItemStack stack) {
        return stack.canPerformAction(ToolActions.AXE_STRIP);
    }

    @Override
    public boolean canShieldBlock(ItemStack stack) {
        return stack.canPerformAction(ToolActions.SHIELD_BLOCK);
    }
}
