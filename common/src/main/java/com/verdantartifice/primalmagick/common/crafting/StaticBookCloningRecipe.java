package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Special recipe for cloning a static book using writable books.
 * 
 * @author Daedalus4096
 */
public class StaticBookCloningRecipe extends CustomRecipe {
    public StaticBookCloningRecipe(CraftingBookCategory pCategory) {
        super(pCategory);
    }

    @Override
    public boolean matches(CraftingInput pContainer, Level pLevel) {
        int count = 0;
        ItemStack bookStack = ItemStack.EMPTY;
        
        for (int index = 0; index < pContainer.size(); index++) {
            ItemStack slotStack = pContainer.getItem(index);
            if (!slotStack.isEmpty()) {
                if (slotStack.is(ItemTagsPM.STATIC_BOOKS)) {
                    if (!bookStack.isEmpty()) {
                        return false;
                    }
                    bookStack = slotStack;
                } else if (slotStack.is(Items.WRITABLE_BOOK)) {
                    count++;
                } else {
                    return false;
                }
            }
        }
        
        return !bookStack.isEmpty() && StaticBookItem.getBookId(bookStack).isPresent() && count > 0;
    }

    @Override
    public ItemStack assemble(CraftingInput pContainer, HolderLookup.Provider pRegistries) {
        int count = 0;
        ItemStack originalStack = ItemStack.EMPTY;
        
        for (int index = 0; index < pContainer.size(); index++) {
            ItemStack slotStack = pContainer.getItem(index);
            if (!slotStack.isEmpty()) {
                if (slotStack.is(ItemTagsPM.STATIC_BOOKS)) {
                    if (!originalStack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    originalStack = slotStack;
                } else if (slotStack.is(Items.WRITABLE_BOOK)) {
                    count++;
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }
        
        if (!originalStack.isEmpty() && StaticBookItem.getBookId(originalStack).isPresent() && count > 0 && StaticBookItem.getGeneration(originalStack) < StaticBookItem.MAX_GENERATION) {
            ItemStack copyStack = originalStack.copyWithCount(count);
            StaticBookItem.setGeneration(copyStack, StaticBookItem.getGeneration(originalStack) + 1);
            return copyStack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput pContainer) {
        NonNullList<ItemStack> retVal = NonNullList.withSize(pContainer.size(), ItemStack.EMPTY);
        
        for (int index = 0; index < retVal.size(); index++) {
            ItemStack inputStack = pContainer.getItem(index);
            if (Services.ITEMS.hasCraftingRemainingItem(inputStack)) {
                retVal.set(index, Services.ITEMS.getCraftingRemainingItem(inputStack));
            } else if (inputStack.getItem() instanceof StaticBookItem) {
                retVal.set(index, inputStack.copyWithCount(1));
                break;
            }
        }
        
        return retVal;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth >= 3 && pHeight >= 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.STATIC_BOOK_CLONING.get();
    }

}
