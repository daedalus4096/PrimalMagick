package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.armor.WardingModuleItem;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Special recipe for applying a warding module to a piece of armor.
 * 
 * @author Daedalus4096
 */
public class WardingModuleApplicationRecipe extends CustomRecipe {
    public WardingModuleApplicationRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput pContainer, Level pLevel) {
        ItemStack moduleStack = ItemStack.EMPTY;
        ItemStack armorStack = ItemStack.EMPTY;
        
        for (int index = 0; index < pContainer.size(); index++) {
            ItemStack stack = pContainer.getItem(index);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof WardingModuleItem) {
                    if (!moduleStack.isEmpty()) {
                        return false;
                    }
                    moduleStack = stack;
                } else if (stack.is(ItemTagsPM.WARDABLE_ARMOR)) {
                    if (!armorStack.isEmpty()) {
                        return false;
                    }
                    armorStack = stack;
                } else {
                    return false;
                }
            }
        }
        
        return !moduleStack.isEmpty() && !armorStack.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput pContainer, HolderLookup.Provider pRegistries) {
        ItemStack moduleStack = ItemStack.EMPTY;
        ItemStack armorStack = ItemStack.EMPTY;
        
        for (int index = 0; index < pContainer.size(); index++) {
            ItemStack stack = pContainer.getItem(index);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof WardingModuleItem) {
                    moduleStack = stack;
                } else if (stack.is(ItemTagsPM.WARDABLE_ARMOR)) {
                    armorStack = stack.copy();
                }
            }
        }
        
        if (armorStack.isEmpty()) {
            return armorStack;
        } else if (moduleStack.getItem() instanceof WardingModuleItem module) {
            return module.applyWard(armorStack);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.WARDING_MODULE_APPLICATION.get();
    }
}
