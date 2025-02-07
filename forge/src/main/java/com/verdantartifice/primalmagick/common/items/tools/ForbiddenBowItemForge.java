package com.verdantartifice.primalmagick.common.items.tools;

import net.minecraft.world.entity.projectile.AbstractArrow;

public class ForbiddenBowItemForge extends ForbiddenBowItem {
    public ForbiddenBowItemForge(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow customArrow(AbstractArrow arrow) {
        return this.boostArrowDamage(super.customArrow(arrow));
    }
}
