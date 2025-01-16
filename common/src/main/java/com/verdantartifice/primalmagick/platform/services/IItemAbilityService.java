package com.verdantartifice.primalmagick.platform.services;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public interface IItemAbilityService {
    boolean canAxeStrip(ItemStack stack);
    boolean canShieldBlock(ItemStack stack);

    LootItemCondition.Builder makeShearsDigLootCondition();
}
