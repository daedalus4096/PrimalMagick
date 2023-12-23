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
import net.minecraft.world.item.crafting.RecipeHolder;

/**
 * Recipe category for a dissolution recipe.
 * 
 * @author Daedalus4096
 */
public class DissolutionRecipeCategory extends RecipeCategoryPM<RecipeHolder<IDissolutionRecipe>> {
    public static final ResourceLocation UID = PrimalMagick.resource("dissolution_chamber");
    private static final ResourceLocation BACKGROUND_TEXTURE = PrimalMagick.resource("textures/gui/jei/dissolution_chamber.png");
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<IDissolutionRecipe> recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 19).addIngredients(recipe.value().getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 19).addItemStack(RecipeUtils.getResultItem(recipe.value()));
    }

    @Override
    public void draw(RecipeHolder<IDissolutionRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if (recipe.value().getManaCosts() != null && !recipe.value().getManaCosts().isEmpty()) {
            this.manaCostIcon.draw(guiGraphics, MANA_COST_X_OFFSET, MANA_COST_Y_OFFSET);
        }
    }

    @Override
    public List<Component> getTooltipStrings(RecipeHolder<IDissolutionRecipe> recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        SourceList manaCosts = recipe.value().getManaCosts();
        if ( manaCosts != null && !manaCosts.isEmpty() && 
             mouseX >= MANA_COST_X_OFFSET && mouseX < MANA_COST_X_OFFSET + this.manaCostIcon.getWidth() &&
             mouseY >= MANA_COST_Y_OFFSET && mouseY < MANA_COST_Y_OFFSET + this.manaCostIcon.getHeight() ) {
            return JeiHelper.getManaCostTooltipStrings(manaCosts);
        } else {
            return super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
        }
    }

    @Override
    public RecipeType<RecipeHolder<IDissolutionRecipe>> getRecipeType() {
        return JeiRecipeTypesPM.DISSOLUTION;
    }
}
