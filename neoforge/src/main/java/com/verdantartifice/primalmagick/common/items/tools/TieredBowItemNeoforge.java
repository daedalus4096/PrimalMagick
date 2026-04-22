package com.verdantartifice.primalmagick.common.items.tools;

import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import org.jetbrains.annotations.NotNull;

public class TieredBowItemNeoforge extends AbstractTieredBowItem {
    public TieredBowItemNeoforge(ToolMaterial material, Properties properties) {
        super(material, properties);
    }

    @Override
    @NotNull
    public AbstractArrow customArrow(@NotNull AbstractArrow arrow, @NotNull ItemStack projectileStack, @NotNull ItemStack weaponStack) {
        return this.boostArrowDamage(super.customArrow(arrow, projectileStack, weaponStack));
    }
}
