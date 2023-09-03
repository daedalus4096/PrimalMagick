package com.verdantartifice.primalmagick.common.items.armor;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.misc.ITieredDevice;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Item definition for a warding module, a magitech device that can be attached to hard armor
 * to provide the wearer with temporary hit points at the cost of mana.
 * 
 * @author Daedalus4096
 */
public class WardingModuleItem extends Item implements ITieredDevice {
    public static final String TAG_NAME = String.join(":", PrimalMagick.MODID, "WardingModule");
    
    /**
     * The total amount of centimana that can be infused into a warded armor stack at once.
     */
    public static final int MANA_CAPACITY = 10000;
    
    /**
     * The amount of centimana that can be infused into the warded armor per tick.
     */
    public static final int CHARGE_RATE = 100;
    
    /**
     * The amount of centimana needed to regenerate one point of ward.
     */
    public static final int REGEN_COST = 500;
    
    protected final DeviceTier tier;
    
    public WardingModuleItem(DeviceTier tier, Item.Properties properties) {
        super(properties);
        this.tier = tier;
    }
    
    @Override
    public DeviceTier getDeviceTier() {
        return this.tier;
    }
    
    public boolean hasWard() {
        return this.getWardLevel() > 0;
    }
    
    public int getWardLevel() {
        return switch (this.getDeviceTier()) {
            case ENCHANTED -> 1;
            case FORBIDDEN -> 2;
            case HEAVENLY -> 3;
            default -> 0;
        };
    }
    
    public static boolean hasWardAttached(ItemStack stack) {
        return stack.is(ItemTagsPM.WARDABLE_ARMOR) && stack.hasTag() && stack.getTag().contains(TAG_NAME) && getAttachedWardLevel(stack) > 0;
    }
    
    public static int getAttachedWardLevel(ItemStack stack) {
        return stack.getTag().getInt(TAG_NAME);
    }
}
