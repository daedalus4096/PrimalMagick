package com.verdantartifice.primalmagick.client.compat.jei.dissolution;

import com.verdantartifice.primalmagick.client.compat.jei.JeiHelper;
import com.verdantartifice.primalmagick.client.compat.jei.JeiRecipeTypesPM;
import com.verdantartifice.primalmagick.client.compat.jei.RecipeCategoryPM;
import com.verdantartifice.primalmagick.client.util.RecipeUtils;
import com.verdantartifice.primalmagick.common.crafting.IDissolutionRecipe;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

/**
 * Recipe category for a dissolution recipe.
 * 
 * @author Daedalus4096
 */
public class DissolutionRecipeCategory extends RecipeCategoryPM<RecipeHolder<IDissolutionRecipe>> {
    public static final ResourceLocation UID = ResourceUtils.loc("dissolution_chamber");
    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceUtils.loc("textures/gui/jei/dissolution_chamber.png");
    private static final int MANA_COST_X_OFFSET = 28;
    private static final int MANA_COST_Y_OFFSET = 1;
    private static final int BG_WIDTH = 82;
    private static final int BG_HEIGHT = 44;

    private final IDrawableStatic background;
    private final IDrawableStatic manaCostIcon;
    
    public DissolutionRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, UID, "block.primalmagick.dissolution_chamber");
        this.manaCostIcon = guiHelper.createDrawable(BACKGROUND_TEXTURE, 82, 0, 16, 16);
        this.background = guiHelper.createDrawable(BACKGROUND_TEXTURE, 0, 0, BG_WIDTH, BG_HEIGHT);
        this.setIcon(new ItemStack(ItemsPM.DISSOLUTION_CHAMBER.get()));
    }

    @Override
    public int getWidth() {
        return BG_WIDTH;
    }

    @Override
    public int getHeight() {
        return BG_HEIGHT;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<IDissolutionRecipe> recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 19).addIngredients(recipe.value().getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 19).addItemStack(RecipeUtils.getResultItem(recipe.value()));
    }

    @Override
    public void draw(RecipeHolder<IDissolutionRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics);
        if (!recipe.value().getManaCosts().isEmpty()) {
            this.manaCostIcon.draw(guiGraphics, MANA_COST_X_OFFSET, MANA_COST_Y_OFFSET);
        }
    }

    @Override
    public void getTooltip(ITooltipBuilder builder, RecipeHolder<IDissolutionRecipe> recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        SourceList manaCosts = recipe.value().getManaCosts();
        if ( !manaCosts.isEmpty() &&
             mouseX >= MANA_COST_X_OFFSET && mouseX < MANA_COST_X_OFFSET + this.manaCostIcon.getWidth() &&
             mouseY >= MANA_COST_Y_OFFSET && mouseY < MANA_COST_Y_OFFSET + this.manaCostIcon.getHeight() ) {
            builder.addAll(JeiHelper.getManaCostTooltipStrings(manaCosts));
        } else {
            super.getTooltip(builder, recipe, recipeSlotsView, mouseX, mouseY);
        }
    }

    @Override
    public RecipeType<RecipeHolder<IDissolutionRecipe>> getRecipeType() {
        return JeiRecipeTypesPM.DISSOLUTION;
    }
}
