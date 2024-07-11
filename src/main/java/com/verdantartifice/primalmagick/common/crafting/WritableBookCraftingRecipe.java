package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.theorycrafting.IWritingImplement;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Special recipe for creating a writable book using a blank book and a mod writing implement.
 * 
 * @author Daedalus4096
 */
public class WritableBookCraftingRecipe extends CustomRecipe {
    protected static final RandomSource RANDOM = RandomSource.create();
    
    public WritableBookCraftingRecipe(CraftingBookCategory pCategory) {
        super(pCategory);
    }

    @Override
    public boolean matches(CraftingInput pContainer, Level pLevel) {
        ItemStack penStack = ItemStack.EMPTY;
        ItemStack bookStack = ItemStack.EMPTY;
        
        for (int index = 0; index < pContainer.size(); index++) {
            ItemStack stack = pContainer.getItem(index);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof IWritingImplement) {
                    if (!penStack.isEmpty()) {
                        return false;
                    }
                    penStack = stack;
                } else if (stack.is(Items.BOOK)) {
                    if (!bookStack.isEmpty()) {
                        return false;
                    }
                    bookStack = stack;
                } else {
                    return false;
                }
            }
        }
        
        return !penStack.isEmpty() && !bookStack.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput pContainer, HolderLookup.Provider pRegistries) {
        ItemStack penStack = ItemStack.EMPTY;
        ItemStack bookStack = ItemStack.EMPTY;
        
        for (int index = 0; index < pContainer.size(); index++) {
            ItemStack stack = pContainer.getItem(index);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof IWritingImplement) {
                    penStack = stack;
                } else if (stack.is(Items.BOOK)) {
                    bookStack = stack;
                }
            }
        }
        
        if (!bookStack.isEmpty() && !penStack.isEmpty()) {
            return new ItemStack(Items.WRITABLE_BOOK);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 2;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput pContainer) {
        NonNullList<ItemStack> retVal = NonNullList.withSize(pContainer.size(), ItemStack.EMPTY);
        
        for (int index = 0; index < retVal.size(); index++) {
            ItemStack inputStack = pContainer.getItem(index);
            if (inputStack.hasCraftingRemainingItem()) {
                retVal.set(index, inputStack.getCraftingRemainingItem());
            } else if (inputStack.getItem() instanceof IWritingImplement pen) {
                ItemStack leftoverStack = inputStack.copyWithCount(1);
                if (pen.isDamagedOnUse()) {
                    int newDamage = leftoverStack.getDamageValue() + 1;
                    if (newDamage >= leftoverStack.getMaxDamage()) {
                        leftoverStack = ItemStack.EMPTY;
                    } else {
                        leftoverStack.setDamageValue(newDamage);
                    }
                }
                retVal.set(index, leftoverStack);
            }
        }
        
        return retVal;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.WRITABLE_BOOK_CRAFTING.get();
    }
}
