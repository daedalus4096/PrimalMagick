package com.verdantartifice.primalmagick.common.items.tools;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.Tier;

/**
 * Base definition of a shield item made of a magickal metal.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractTieredShieldItem extends ShieldItem {
    protected final Tier tier;
    
    public AbstractTieredShieldItem(Tier tier, Item.Properties properties) {
        super(properties.durability(tier.getUses()));
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

    /**
     * Return true if a banner can be applied to this item to decorate it.
     * 
     * @return whether a banner can be applied to this item to decorate it
     */
    public boolean canDecorate() {
        return true;
    }
}
