package com.verdantartifice.primalmagic.common.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.NBTIngredient;

/**
 * An extension of Forge's NBTIngredient that allows for instantiation.
 * 
 * @author Daedalus4096
 */
public class NBTIngredientPM extends NBTIngredient {
    protected NBTIngredientPM(ItemStack stack) {
        super(stack);
    }
    
    public static NBTIngredientPM fromStack(ItemStack stack) {
        return new NBTIngredientPM(stack);
    }
}
