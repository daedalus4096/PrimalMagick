package com.verdantartifice.primalmagick.client.compat.jei.runecarving;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.compat.jei.RecipeCategoryPM;
import com.verdantartifice.primalmagick.client.compat.jei.JeiRecipeTypesPM;
import com.verdantartifice.primalmagick.common.crafting.IRunecarvingRecipe;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
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
        super(guiHelper, UID, "block.primalmagick.runecarving_table");
        this.setBackground(guiHelper.createDrawable(BACKGROUND_TEXTURE, 0, 0, 125, 18));
        this.setIcon(new ItemStack(ItemsPM.RUNECARVING_TABLE.get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IRunecarvingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 50, 1).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 1).addItemStack(recipe.getResultItem());
    }

    @Override
    public RecipeType<IRunecarvingRecipe> getRecipeType() {
        return JeiRecipeTypesPM.RUNECARVING;
    }
}
