package com.verdantartifice.primalmagick.common.items.tools;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Tier;

public class TieredBowItemForge extends TieredBowItem {
    public TieredBowItemForge(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public AbstractArrow customArrow(AbstractArrow arrow) {
        return this.boostArrowDamage(super.customArrow(arrow));
    }
}
