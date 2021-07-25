package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.common.items.wands.SpellScrollItem;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

/**
 * Special definition for a wand inscription recipe.
 * 
 * @author Daedalus4096
 */
public class WandInscriptionRecipe extends CustomRecipe {
    public WandInscriptionRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        ItemStack wandStack = inv.getItem(0);
        ItemStack scrollStack = inv.getItem(1);
        
        if (!wandStack.isEmpty() && !scrollStack.isEmpty() && wandStack.getItem() instanceof IWand && scrollStack.getItem() instanceof SpellScrollItem) {
            // Make sure a wand and a filled spell scroll are present, and that the scroll's spell will fit into the wand
            IWand wand = (IWand)wandStack.getItem();
            SpellScrollItem scroll = (SpellScrollItem)scrollStack.getItem();
            return wand.canAddSpell(wandStack, scroll.getSpell(scrollStack));
        } else {
            return false;
        }
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        ItemStack wandStack = inv.getItem(0);
        ItemStack scrollStack = inv.getItem(1);
        
        if (!wandStack.isEmpty() && !scrollStack.isEmpty() && wandStack.getItem() instanceof IWand && scrollStack.getItem() instanceof SpellScrollItem) {
            // Create a copy of the given wand and add the scroll's spell to it
            IWand wand = (IWand)wandStack.getItem();
            SpellScrollItem scroll = (SpellScrollItem)scrollStack.getItem();
            ItemStack retVal = wandStack.copy();
            if (wand.addSpell(retVal, scroll.getSpell(scrollStack))) {
                return retVal;
            } else {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }
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
