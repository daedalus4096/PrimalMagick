package com.verdantartifice.primalmagick.common.items.tools;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;

public class ForbiddenBowItemNeoforge extends ForbiddenBowItem {
    public ForbiddenBowItemNeoforge(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
        return this.boostArrowDamage(super.customArrow(arrow, projectileStack, weaponStack));
    }
}
