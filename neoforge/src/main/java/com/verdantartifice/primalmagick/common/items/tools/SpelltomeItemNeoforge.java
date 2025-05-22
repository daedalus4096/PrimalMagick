package com.verdantartifice.primalmagick.common.items.tools;

import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import net.minecraft.world.item.ItemStack;

public class SpelltomeItemNeoforge extends SpelltomeItem {
    public SpelltomeItemNeoforge(DeviceTier tier, Properties pProperties) {
        super(tier, pProperties);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
}
