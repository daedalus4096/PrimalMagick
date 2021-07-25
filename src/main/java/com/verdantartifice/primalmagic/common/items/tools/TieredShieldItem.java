package com.verdantartifice.primalmagic.common.items.tools;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;

/**
 * Definition of a shield item made of a magical metal.
 * 
 * @author Daedalus4096
 */
public class TieredShieldItem extends ShieldItem {
    protected final Tier tier;
    
    public TieredShieldItem(Tier tier, Item.Properties properties) {
        super(properties.defaultDurability(tier.getUses()));
        this.tier = tier;
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

    @Override
    public boolean isShield(ItemStack stack, LivingEntity entity) {
        return true;
    }
}
