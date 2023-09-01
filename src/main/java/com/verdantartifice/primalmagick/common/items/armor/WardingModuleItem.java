package com.verdantartifice.primalmagick.common.items.armor;

import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.misc.ITieredDevice;

import net.minecraft.world.item.Item;

/**
 * Item definition for a warding module, a magitech device that can be attached to hard armor
 * to provide the wearer with temporary hit points at the cost of mana.
 * 
 * @author Daedalus4096
 */
public class WardingModuleItem extends Item implements ITieredDevice {
    protected final DeviceTier tier;
    
    public WardingModuleItem(DeviceTier tier, Item.Properties properties) {
        super(properties);
        this.tier = tier;
    }
    
    @Override
    public DeviceTier getDeviceTier() {
        return this.tier;
    }
    
    public int getWardLevel() {
        return switch (this.getDeviceTier()) {
            case ENCHANTED -> 2;
            case FORBIDDEN -> 3;
            case HEAVENLY -> 4;
            default -> 0;
        };
    }
}
