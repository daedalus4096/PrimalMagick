package com.verdantartifice.primalmagick.common.items.tools;

import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ForbiddenBowItemNeoforge extends ForbiddenBowItem {
    public ForbiddenBowItemNeoforge(Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    public AbstractArrow customArrow(@NotNull AbstractArrow arrow, @NotNull ItemStack projectileStack, @NotNull ItemStack weaponStack) {
        return this.boostArrowDamage(super.customArrow(arrow, projectileStack, weaponStack));
    }
}
