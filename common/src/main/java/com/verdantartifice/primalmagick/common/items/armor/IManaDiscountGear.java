package com.verdantartifice.primalmagick.common.items.armor;

import java.util.Optional;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Interface describing a piece of gear that gives a discount to mana expenditures from wands.
 * 
 * @author Daedalus4096
 */
public interface IManaDiscountGear {
    /**
     * Gets the mana discount for the given stack, player, and source, in whole percentage points.
     * 
     * @param stack the item stack to be queried
     * @param player the player wearing the item stack
     * @param source the source being queried
     * @return the mana discount for the given stack and player, in whole percentage points
     */
    public int getManaDiscount(ItemStack stack, @Nullable Player player, Source source);
    
    /**
     * Gets the best mana discount for the given stack and player, taking into account source attunement, in whole percentage points.
     * 
     * @param stack the item stack to be queried
     * @param player the player wearing the item stack
     * @return the best mana discount for the given stack and player, taking into account source attunement, in whole percentage points
     */
    public int getBestManaDiscount(ItemStack stack, @Nullable Player player);
    
    /**
     * Gets the source to which the given stack is attuned, if any.
     * 
     * @param stack the item stack to be queried
     * @param player the player wearing the item stack
     * @return the source to which the given stack is attuned, if any
     */
    public Optional<Source> getAttunedSource(ItemStack stack, @Nullable Player player);
}
