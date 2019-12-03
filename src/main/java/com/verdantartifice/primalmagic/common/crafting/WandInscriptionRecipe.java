package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.common.items.wands.SpellScrollItem;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class WandInscriptionRecipe extends SpecialRecipe {
    public WandInscriptionRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        ItemStack wandStack = inv.getStackInSlot(0);
        ItemStack scrollStack = inv.getStackInSlot(1);
        
        if (!wandStack.isEmpty() && !scrollStack.isEmpty() && wandStack.getItem() instanceof IWand && scrollStack.getItem() instanceof SpellScrollItem) {
            IWand wand = (IWand)wandStack.getItem();
            SpellScrollItem scroll = (SpellScrollItem)scrollStack.getItem();
            return wand.canAddSpell(wandStack, scroll.getSpell(scrollStack));
        } else {
            return false;
        }
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack wandStack = inv.getStackInSlot(0);
        ItemStack scrollStack = inv.getStackInSlot(1);
        
        if (!wandStack.isEmpty() && !scrollStack.isEmpty() && wandStack.getItem() instanceof IWand && scrollStack.getItem() instanceof SpellScrollItem) {
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
    public boolean canFit(int width, int height) {
        return (width * height) >= 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.WAND_INSCRIPTION_SPECIAL;
    }
}
