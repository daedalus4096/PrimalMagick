package com.verdantartifice.primalmagick.common.items.tools;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;

public class TieredBowItemNeoforge extends AbstractTieredBowItem {
    public TieredBowItemNeoforge(ToolMaterial material, Properties properties) {
        super(material, properties);
    }

    @Override
    public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
        return this.boostArrowDamage(super.customArrow(arrow, projectileStack, weaponStack));
    }
}
