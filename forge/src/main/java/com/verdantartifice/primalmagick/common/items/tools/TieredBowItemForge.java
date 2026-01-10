package com.verdantartifice.primalmagick.common.items.tools;

import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ToolMaterial;
import org.jetbrains.annotations.NotNull;

public class TieredBowItemForge extends AbstractTieredBowItem {
    public TieredBowItemForge(ToolMaterial material, Properties properties) {
        super(material, properties);
    }

    @Override
    @NotNull
    public AbstractArrow customArrow(@NotNull AbstractArrow arrow) {
        return this.boostArrowDamage(super.customArrow(arrow));
    }
}
