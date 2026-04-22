package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.items.entities.FlyingCarpetItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Special definition for a recipe to dye a flying carpet.
 * 
 * @author Daedalus4096
 */
public class FlyingCarpetDyeRecipe extends CustomRecipe {
    public static final ResourceKey<Recipe<?>> RECIPE_KEY = ResourceKey.create(Registries.RECIPE, ResourceUtils.loc("flying_carpet_dye"));

    public FlyingCarpetDyeRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput inv, Level worldIn) {
        ItemStack carpetStack = ItemStack.EMPTY;
        ItemStack dyeStack = ItemStack.EMPTY;

        if (inv.ingredientCount() != 2) {
            return false;
        }
        for (int index = 0; index < inv.size(); index++) {
            ItemStack slotStack = inv.getItem(index);
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
                } else {
                    return false;
                }
            }
        }
        
        return !carpetStack.isEmpty() && !dyeStack.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registryAccess) {
        ItemStack carpetStack = ItemStack.EMPTY;
        ItemStack dyeStack = ItemStack.EMPTY;
        
        for (int index = 0; index < inv.size(); index++) {
            ItemStack slotStack = inv.getItem(index);
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
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }
        
        return (!carpetStack.isEmpty() && !dyeStack.isEmpty()) ? FlyingCarpetItem.dyeCarpet(carpetStack, (DyeItem)dyeStack.getItem()) : ItemStack.EMPTY;
    }

    @Override
    @NotNull
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return RecipeSerializersPM.FLYING_CARPET_DYE.get();
    }
}
