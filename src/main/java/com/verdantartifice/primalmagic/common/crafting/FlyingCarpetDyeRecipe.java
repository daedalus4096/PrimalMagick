package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.common.items.entities.FlyingCarpetItem;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Special definition for a recipe to dye a flying carpet.
 * 
 * @author Daedalus4096
 */
public class FlyingCarpetDyeRecipe extends SpecialRecipe {
    public FlyingCarpetDyeRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        ItemStack carpetStack = ItemStack.EMPTY;
        ItemStack dyeStack = ItemStack.EMPTY;
        
        for (int index = 0; index < inv.getSizeInventory(); index++) {
            ItemStack slotStack = inv.getStackInSlot(index);
            if (!slotStack.isEmpty()) {
                if (slotStack.getItem() instanceof FlyingCarpetItem) {
                    if (!carpetStack.isEmpty()) {
                        return false;
                    }
                    carpetStack = slotStack;
                } else if (slotStack.getItem() instanceof DyeItem) {
                    if (!dyeStack.isEmpty()) {
                        return false;
                    }
                    dyeStack = slotStack;
                }
            }
        }
        
        return !carpetStack.isEmpty() && !dyeStack.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack carpetStack = ItemStack.EMPTY;
        ItemStack dyeStack = ItemStack.EMPTY;
        
        for (int index = 0; index < inv.getSizeInventory(); index++) {
            ItemStack slotStack = inv.getStackInSlot(index);
            if (!slotStack.isEmpty()) {
                if (slotStack.getItem() instanceof FlyingCarpetItem) {
                    if (!carpetStack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    carpetStack = slotStack;
                } else if (slotStack.getItem() instanceof DyeItem) {
                    if (!dyeStack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    dyeStack = slotStack;
                }
            }
        }
        
        return (!carpetStack.isEmpty() && !dyeStack.isEmpty()) ? FlyingCarpetItem.dyeCarpet(carpetStack, (DyeItem)dyeStack.getItem()) : ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.FLYING_CARPET_DYE.get();
    }
}
