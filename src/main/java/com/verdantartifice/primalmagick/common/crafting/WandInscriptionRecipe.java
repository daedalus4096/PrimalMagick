package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.items.wands.SpellScrollItem;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Special definition for a wand inscription recipe.
 * 
 * @author Daedalus4096
 */
public class WandInscriptionRecipe extends CustomRecipe {
    public WandInscriptionRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        ItemStack wandStack = inv.getItem(0);
        ItemStack scrollStack = inv.getItem(1);
        
        // Make sure a wand is present
        if (!wandStack.isEmpty() && wandStack.getItem() instanceof IWand wand) {
            if (!scrollStack.isEmpty() && scrollStack.getItem() instanceof SpellScrollItem scroll) {
                // If a filled spell scroll is also present, check that the scroll's spell will fit into the wand
                return wand.canAddSpell(wandStack, scroll.getSpell(scrollStack));
            } else {
                // If no item is present in the scroll slot, clear the wand; if it's something other than a filled spell scroll, don't allow combination
                return scrollStack.isEmpty();
            }
        } else {
            return false;
        }
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        ItemStack wandStack = inv.getItem(0);
        ItemStack scrollStack = inv.getItem(1);
        
        if (!wandStack.isEmpty() && wandStack.getItem() instanceof IWand wand) {
            if (!scrollStack.isEmpty() && scrollStack.getItem() instanceof SpellScrollItem scroll) {
                // If a filled spell scroll is also present, create a copy of the given wand and add the scroll's spell to it
                ItemStack retVal = wandStack.copy();
                if (wand.addSpell(retVal, scroll.getSpell(scrollStack)) && wand.setActiveSpellIndex(retVal, wand.getSpellCount(retVal) - 1)) {
                    return retVal;
                } else {
                    return ItemStack.EMPTY;
                }
            } else if (scrollStack.isEmpty()) {
                // If no item is present in the scroll slot, clear the wand of spells
                ItemStack retVal = wandStack.copy();
                wand.clearSpells(retVal);
                return retVal;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return (width * height) >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.WAND_INSCRIPTION_SPECIAL.get();
    }
}
