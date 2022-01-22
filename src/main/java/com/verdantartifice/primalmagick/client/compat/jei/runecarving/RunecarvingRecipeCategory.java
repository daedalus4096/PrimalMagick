package com.verdantartifice.primalmagick.client.compat.jei.runecarving;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.compat.jei.RecipeCategoryPM;
import com.verdantartifice.primalmagick.common.crafting.IRunecarvingRecipe;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Recipe category for a runecarving recipe.
 * 
 * @author Daedalus4096
 */
public class RunecarvingRecipeCategory extends RecipeCategoryPM<IRunecarvingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(PrimalMagick.MODID, "runecarving_table");
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/jei/runecarving_table.png");

    public RunecarvingRecipeCategory(IGuiHelper guiHelper) {
        super(IRunecarvingRecipe.class, guiHelper, UID, "block.primalmagick.runecarving_table");
        this.setBackground(guiHelper.createDrawable(BACKGROUND_TEXTURE, 0, 0, 125, 18));
        this.setIcon(new ItemStack(ItemsPM.RUNECARVING_TABLE.get()));
    }

    @Override
    public void setIngredients(IRunecarvingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRunecarvingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 0, 0);
        guiItemStacks.init(1, true, 49, 0);
        guiItemStacks.init(2, false, 107, 0);

        guiItemStacks.set(ingredients);
    }
}
