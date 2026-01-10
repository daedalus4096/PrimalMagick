package com.verdantartifice.primalmagick.common.items.tools;

import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.jetbrains.annotations.NotNull;

public class ForbiddenBowItemForge extends ForbiddenBowItem {
    public ForbiddenBowItemForge(Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    public AbstractArrow customArrow(@NotNull AbstractArrow arrow) {
        return this.boostArrowDamage(super.customArrow(arrow));
    }
}
