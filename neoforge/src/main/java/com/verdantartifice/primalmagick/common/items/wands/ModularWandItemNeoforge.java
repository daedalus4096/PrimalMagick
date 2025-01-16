package com.verdantartifice.primalmagick.common.items.wands;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public class ModularWandItemNeoforge extends ModularWandItem {
    public ModularWandItemNeoforge(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return this.onWandUseFirst(stack, context);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        // Don't break wand interaction just because the stack NBT changes
        return true;
    }
}
