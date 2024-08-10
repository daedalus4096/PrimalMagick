package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagick.common.items.wands.StaffCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCapItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandGemItem;
import com.verdantartifice.primalmagick.common.wands.IStaff;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Special definition for a wand glamour recipe.
 * 
 * @author Daedalus4096
 */
public class WandGlamourRecipe extends CustomRecipe {
    public WandGlamourRecipe(CraftingBookCategory category) {
        super(category);
    }

    private static ItemStack getItem(CraftingInput inv, int index) {
        return (index >= 0 && index < inv.size()) ? inv.getItem(index) : ItemStack.EMPTY;
    }

    @Override
    public boolean matches(CraftingInput inv, Level worldIn) {
        ItemStack wandStack = getItem(inv, 0);
        ItemStack coreStack = getItem(inv, 1);
        ItemStack capStack = getItem(inv, 2);
        ItemStack gemStack = getItem(inv, 3);
        boolean isStaff = (wandStack.getItem() instanceof IStaff);
        
        // Make sure the crafting inventory has a modular wand/staff, as well as an optional core, cap, and/or gem
        return !wandStack.isEmpty() && (wandStack.getItem() instanceof ModularWandItem) &&
               (coreStack.isEmpty() || (isStaff && (coreStack.getItem() instanceof StaffCoreItem)) || (!isStaff && (coreStack.getItem() instanceof WandCoreItem))) &&
               (capStack.isEmpty() || (capStack.getItem() instanceof WandCapItem)) &&
               (gemStack.isEmpty() || (gemStack.getItem() instanceof WandGemItem));
    }
    
    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
        ItemStack wandStack = getItem(inv, 0);
        ItemStack coreStack = getItem(inv, 1);
        ItemStack capStack = getItem(inv, 2);
        ItemStack gemStack = getItem(inv, 3);
        
        ItemStack retVal = wandStack.copy();
        ModularWandItem wandItem = (ModularWandItem)retVal.getItem();
        
        if (coreStack.getItem() instanceof WandCoreItem wandCoreItem) {
            wandItem.setWandCoreAppearance(retVal, wandCoreItem.getWandCore());
        } else if (coreStack.getItem() instanceof StaffCoreItem staffCoreItem) {
            wandItem.setWandCoreAppearance(retVal, staffCoreItem.getWandCore());
        } else {
            wandItem.setWandCoreAppearance(retVal, null);
        }
        
        if (capStack.getItem() instanceof WandCapItem capItem) {
            wandItem.setWandCapAppearance(retVal, capItem.getWandCap());
        } else {
            wandItem.setWandCapAppearance(retVal, null);
        }
        
        if (gemStack.getItem() instanceof WandGemItem gemItem) {
            wandItem.setWandGemAppearance(retVal, gemItem.getWandGem());
        } else {
            wandItem.setWandGemAppearance(retVal, null);
        }

        return retVal;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return (width * height) >= 4;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.WAND_GLAMOUR_SPECIAL.get();
    }
}
