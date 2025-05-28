package com.verdantartifice.primalmagick.common.items.tools;

import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import net.minecraft.world.item.ItemStack;

public class ManaOrbItemNeoforge extends ManaOrbItem {
    public ManaOrbItemNeoforge(DeviceTier tier, Properties pProperties) {
        super(tier, pProperties);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
}
