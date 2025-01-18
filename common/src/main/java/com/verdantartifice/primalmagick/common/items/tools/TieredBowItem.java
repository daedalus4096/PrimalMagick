package com.verdantartifice.primalmagick.common.items.tools;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

import javax.annotation.Nonnull;

/**
 * Definition of a repairable bow item made of a magickal metal.
 * 
 * @author Daedalus4096
 */
public abstract class TieredBowItem extends BowItem {
    protected final Tier tier;
    
    public TieredBowItem(Tier tier, Item.Properties properties) {
        super(properties.durability(tier.getUses()));
        this.tier = tier;
    }

    protected AbstractArrow boostArrowDamage(@Nonnull AbstractArrow arrow) {
        double damageBonus = switch (this.tier) {
            case ItemTierPM.PRIMALITE -> 0.5D;
            case ItemTierPM.HEXIUM -> 1.0D;
            case ItemTierPM.HALLOWSTEEL -> 1.5D;
            default -> 0.0D;
        };
        arrow.setBaseDamage(arrow.getBaseDamage() + damageBonus);
        return arrow;
    }
    
    public Tier getTier() {
        return this.tier;
    }

    @Override
    public int getEnchantmentValue() {
        return this.tier.getEnchantmentValue();
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return this.tier.getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
    }
}
