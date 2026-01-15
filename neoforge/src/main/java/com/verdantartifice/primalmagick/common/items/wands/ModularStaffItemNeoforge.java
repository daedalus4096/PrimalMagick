package com.verdantartifice.primalmagick.common.items.wands;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

public class ModularStaffItemNeoforge extends ModularStaffItem {
    public ModularStaffItemNeoforge(Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    public InteractionResult onItemUseFirst(@NotNull ItemStack stack, @NotNull UseOnContext context) {
        return this.onWandUseFirst(stack, context);
    }

    @Override
    public boolean shouldCauseReequipAnimation(@NotNull ItemStack oldStack, @NotNull ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean canContinueUsing(@NotNull ItemStack oldStack, @NotNull ItemStack newStack) {
        // Don't break wand interaction just because the stack NBT changes
        return true;
    }
}
