package com.verdantartifice.primalmagick.common.items.tools;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ToolMaterial;

public class TieredBowItemForge extends AbstractTieredBowItem {
    public TieredBowItemForge(ToolMaterial material, Properties properties) {
        super(material, properties);
    }

    @Override
    public AbstractArrow customArrow(AbstractArrow arrow) {
        return this.boostArrowDamage(super.customArrow(arrow));
    }
}
