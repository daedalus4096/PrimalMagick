package com.verdantartifice.primalmagick.platform.services;

import net.minecraft.world.item.ItemStack;

public interface IItemAbilityService {
    boolean canAxeStrip(ItemStack stack);
    boolean canShieldBlock(ItemStack stack);
}
