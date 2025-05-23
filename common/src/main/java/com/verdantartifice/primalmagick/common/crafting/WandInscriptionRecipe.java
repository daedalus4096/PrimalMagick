package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.items.wands.SpellScrollItem;
import com.verdantartifice.primalmagick.common.wands.ISpellContainer;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
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

    private static ItemStack getItem(CraftingInput inv, int index) {
        return (index >= 0 && index < inv.size()) ? inv.getItem(index) : ItemStack.EMPTY;
    }

    @Override
    public boolean matches(CraftingInput inv, Level worldIn) {
        ItemStack wandStack = getItem(inv, 0);
        ItemStack scrollStack = getItem(inv, 1);
        
        // Make sure a spell container is present
        if (!wandStack.isEmpty() && wandStack.getItem() instanceof ISpellContainer spellContainer) {
            if (!scrollStack.isEmpty() && scrollStack.getItem() instanceof SpellScrollItem scroll) {
                // If a filled spell scroll is also present, check that the scroll's spell will fit into the wand
                return spellContainer.canAddSpell(wandStack, scroll.getSpell(scrollStack));
            } else {
                // If no item is present in the scroll slot, clear the wand; if it's something other than a filled spell scroll, don't allow combination
                return scrollStack.isEmpty();
            }
        } else {
            return false;
        }
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
        ItemStack wandStack = getItem(inv, 0);
        ItemStack scrollStack = getItem(inv, 1);
        
        if (!wandStack.isEmpty() && wandStack.getItem() instanceof ISpellContainer spellContainer) {
            if (!scrollStack.isEmpty() && scrollStack.getItem() instanceof SpellScrollItem scroll) {
                // If a filled spell scroll is also present, create a copy of the given wand and add the scroll's spell to it
                ItemStack retVal = wandStack.copy();
                if (spellContainer.addSpell(retVal, scroll.getSpell(scrollStack)) && spellContainer.setActiveSpellIndex(retVal, spellContainer.getSpellCount(retVal) - 1)) {
                    return retVal;
                } else {
                    return ItemStack.EMPTY;
                }
            } else if (scrollStack.isEmpty()) {
                // If no item is present in the scroll slot, clear the wand of spells
                ItemStack retVal = wandStack.copy();
                spellContainer.clearSpells(retVal);
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
