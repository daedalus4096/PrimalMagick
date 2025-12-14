package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

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
    public boolean matches(@NotNull CraftingInput inv, @NotNull Level worldIn) {
        return !inv.isEmpty() && inv.getItem(0).is(ItemsPM.SPELL_SCROLL_BLANK.get());
    }

    @Override
    @NotNull
    public ItemStack assemble(@NotNull CraftingInput inv, @NotNull HolderLookup.Provider registries) {
        return new ItemStack(ItemsPM.SPELL_SCROLL_FILLED.get());
    }

    @Override
    @NotNull
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return RecipeSerializersPM.SPELLCRAFTING_SPECIAL.get();
    }
}
