package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.theorycrafting.IWritingImplement;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Special recipe for creating a writable book using a blank book and a mod writing implement.
 * 
 * @author Daedalus4096
 */
public class WritableBookCraftingRecipe extends CustomRecipe {
    public static final ResourceKey<Recipe<?>> RECIPE_KEY = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("writable_book_crafting"));

    @Override
    public boolean matches(@NotNull CraftingInput pContainer, @NotNull Level pLevel) {
        if (pContainer.ingredientCount() != 2) {
            return false;
        }

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
    @NotNull
    public ItemStack assemble(@NotNull CraftingInput pContainer) {
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
    @NotNull
    public NonNullList<ItemStack> getRemainingItems(@NotNull CraftingInput pContainer) {
        NonNullList<ItemStack> retVal = NonNullList.withSize(pContainer.size(), ItemStack.EMPTY);
        
        for (int index = 0; index < retVal.size(); index++) {
            ItemStack inputStack = pContainer.getItem(index);
            ItemStack remainderStack = inputStack.getItem().getCraftingRemainder();
            if (!remainderStack.isEmpty()) {
                retVal.set(index, remainderStack);
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
    @NotNull
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return RecipeSerializersPM.WRITABLE_BOOK_CRAFTING.get();
    }
}
