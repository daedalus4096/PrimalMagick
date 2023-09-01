package com.verdantartifice.primalmagick.common.items.armor;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.misc.ITieredDevice;

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
        return stack.hasTag() && stack.getTag().contains(TAG_NAME) && getAttachedWardLevel(stack) > 0;
    }
    
    public static int getAttachedWardLevel(ItemStack stack) {
        return stack.getTag().getInt(TAG_NAME);
    }
}
