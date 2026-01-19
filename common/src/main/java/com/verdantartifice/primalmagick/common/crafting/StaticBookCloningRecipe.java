package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Special recipe for cloning a static book using writable books.
 * 
 * @author Daedalus4096
 */
public class StaticBookCloningRecipe extends CustomRecipe {
    public static final ResourceKey<Recipe<?>> RECIPE_KEY = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("static_book_cloning"));

    public StaticBookCloningRecipe(CraftingBookCategory pCategory) {
        super(pCategory);
    }

    @Override
    public boolean matches(@NotNull CraftingInput pContainer, @NotNull Level pLevel) {
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
    @NotNull
    public ItemStack assemble(@NotNull CraftingInput pContainer, @NotNull HolderLookup.Provider pRegistries) {
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
    @NotNull
    public NonNullList<ItemStack> getRemainingItems(@NotNull CraftingInput pContainer) {
        NonNullList<ItemStack> retVal = NonNullList.withSize(pContainer.size(), ItemStack.EMPTY);
        
        for (int index = 0; index < retVal.size(); index++) {
            ItemStack inputStack = pContainer.getItem(index);
            ItemStack remainderStack = inputStack.getItem().getCraftingRemainder();
            if (!remainderStack.isEmpty()) {
                retVal.set(index, remainderStack);
            } else if (inputStack.getItem() instanceof StaticBookItem) {
                retVal.set(index, inputStack.copyWithCount(1));
                break;
            }
        }
        
        return retVal;
    }

    @Override
    @NotNull
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return RecipeSerializersPM.STATIC_BOOK_CLONING.get();
    }

}
