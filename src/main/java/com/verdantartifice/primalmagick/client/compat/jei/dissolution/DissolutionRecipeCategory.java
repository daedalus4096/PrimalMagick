package com.verdantartifice.primalmagick.client.compat.jei.dissolution;

import java.util.List;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.compat.jei.JeiHelper;
import com.verdantartifice.primalmagick.client.compat.jei.JeiRecipeTypesPM;
import com.verdantartifice.primalmagick.client.compat.jei.RecipeCategoryPM;
import com.verdantartifice.primalmagick.client.util.RecipeUtils;
import com.verdantartifice.primalmagick.common.crafting.IDissolutionRecipe;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Recipe category for a dissolution recipe.
 * 
 * @author Daedalus4096
 */
public class DissolutionRecipeCategory extends RecipeCategoryPM<IDissolutionRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(PrimalMagick.MODID, "dissolution_chamber");
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/jei/dissolution_chamber.png");
    private static final int MANA_COST_X_OFFSET = 28;
    private static final int MANA_COST_Y_OFFSET = 1;

    private final IDrawableStatic manaCostIcon;
    
    public DissolutionRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, UID, "block.primalmagick.dissolution_chamber");
        this.manaCostIcon = guiHelper.createDrawable(BACKGROUND_TEXTURE, 82, 0, 16, 16);
        this.setBackground(guiHelper.createDrawable(BACKGROUND_TEXTURE, 0, 0, 82, 44));
        this.setIcon(new ItemStack(ItemsPM.DISSOLUTION_CHAMBER.get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IDissolutionRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 19).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 19).addItemStack(RecipeUtils.getResultItem(recipe));
    }

    @Override
    public void draw(IDissolutionRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if (recipe.getManaCosts() != null && !recipe.getManaCosts().isEmpty()) {
            this.manaCostIcon.draw(guiGraphics, MANA_COST_X_OFFSET, MANA_COST_Y_OFFSET);
        }
    }

    @Override
    public List<Component> getTooltipStrings(IDissolutionRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        SourceList manaCosts = recipe.getManaCosts();
        if ( manaCosts != null && !manaCosts.isEmpty() && 
             mouseX >= MANA_COST_X_OFFSET && mouseX < MANA_COST_X_OFFSET + this.manaCostIcon.getWidth() &&
             mouseY >= MANA_COST_Y_OFFSET && mouseY < MANA_COST_Y_OFFSET + this.manaCostIcon.getHeight() ) {
            return JeiHelper.getManaCostTooltipStrings(manaCosts);
        } else {
            return super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
        }
    }

    @Override
    public RecipeType<IDissolutionRecipe> getRecipeType() {
        return JeiRecipeTypesPM.DISSOLUTION;
    }
}
