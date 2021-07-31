package com.verdantartifice.primalmagic.common.items.armor;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Interface describing a piece of gear that gives a discount to mana expenditures from wands.
 * 
 * @author Daedalus4096
 */
public interface IManaDiscountGear {
    /**
     * Gets the mana discount for the given stack and player, in whole percentage points.
     * 
     * @param stack the item stack to be queried
     * @param player the player wearing the item stack
     * @return the mana discount for the given stack and player, in whole percentage points
     */
    public int getManaDiscount(ItemStack stack, Player player);
}
