package com.verdantartifice.primalmagic.common.items.tools;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;

/**
 * Definition of a shield item made of a magical metal.
 * 
 * @author Daedalus4096
 */
public class TieredShieldItem extends ShieldItem {
    protected final IItemTier tier;
    
    public TieredShieldItem(IItemTier tier, Item.Properties properties) {
        super(properties.defaultMaxDamage(tier.getMaxUses()));
        this.tier = tier;
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

    @Override
    public boolean isShield(ItemStack stack, LivingEntity entity) {
        return true;
    }
}
