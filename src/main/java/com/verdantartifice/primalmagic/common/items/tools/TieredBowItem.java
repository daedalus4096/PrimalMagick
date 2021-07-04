package com.verdantartifice.primalmagic.common.items.tools;

import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Definition of a repairable bow item made of a magical metal.
 * 
 * @author Daedalus4096
 */
public class TieredBowItem extends BowItem {
    protected final IItemTier tier;
    
    public TieredBowItem(IItemTier tier, Item.Properties properties) {
        super(properties.defaultMaxDamage(tier.getMaxUses()));
        this.tier = tier;
    }

    @Override
    public AbstractArrowEntity customArrow(AbstractArrowEntity arrow) {
        AbstractArrowEntity newArrow = super.customArrow(arrow);
        double damageBonus = 0.0D;
        if (this.tier == ItemTierPM.PRIMALITE) {
            damageBonus = 0.5D;
        } else if (this.tier == ItemTierPM.HEXIUM) {
            damageBonus = 1.0D;
        } else if (this.tier == ItemTierPM.HALLOWSTEEL) {
            damageBonus = 1.5D;
        }
        newArrow.setDamage(newArrow.getDamage() + damageBonus);
        return newArrow;
    }
    
    public IItemTier getTier() {
        return this.tier;
    }

    @Override
    public int getItemEnchantability() {
        return this.tier.getEnchantability();
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return this.tier.getRepairMaterial().test(repair) || super.getIsRepairable(toRepair, repair);
    }
}
