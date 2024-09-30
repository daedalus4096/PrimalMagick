package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Special definition for a spellcrafting recipe.
 * 
 * @author Daedalus4096
 */
public class SpellcraftingRecipe extends CustomRecipe {
    public SpellcraftingRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput inv, Level worldIn) {
        return inv.isEmpty() ? false : inv.getItem(0).getItem().equals(ItemsPM.SPELL_SCROLL_BLANK.get());
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider registries) {
        return new ItemStack(ItemsPM.SPELL_SCROLL_FILLED.get());
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return (width * height) >= 1;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializersPM.SPELLCRAFTING_SPECIAL.get();
    }
}
