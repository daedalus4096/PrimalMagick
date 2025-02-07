package com.verdantartifice.primalmagick.common.items.tools;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class TieredBowItemNeoforge extends TieredBowItem{
    public TieredBowItemNeoforge(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
        return this.boostArrowDamage(super.customArrow(arrow, projectileStack, weaponStack));
    }
}
