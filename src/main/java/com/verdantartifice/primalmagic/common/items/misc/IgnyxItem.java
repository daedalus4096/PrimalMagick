package com.verdantartifice.primalmagic.common.items.misc;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

/**
 * Item definition for ignyx, an alchemical super-coal.  Explodes on impact when thrown.
 * 
 * @author Daedalus4096
 */
public class IgnyxItem extends Item {
    public IgnyxItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
        return 12800;
    }
}
