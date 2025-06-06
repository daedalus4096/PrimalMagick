package com.verdantartifice.primalmagick.client.compat.jei.runecarving;

import com.verdantartifice.primalmagick.client.compat.jei.JeiHelper;
import com.verdantartifice.primalmagick.client.compat.jei.JeiRecipeTypesPM;
import com.verdantartifice.primalmagick.client.compat.jei.RecipeCategoryPM;
import com.verdantartifice.primalmagick.client.util.RecipeUtils;
import com.verdantartifice.primalmagick.common.crafting.IRunecarvingRecipe;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Optional;

/**
 * Recipe category for a runecarving recipe.
 * 
 * @author Daedalus4096
 */
public class RunecarvingRecipeCategory extends RecipeCategoryPM<RecipeHolder<IRunecarvingRecipe>> {
    public static final ResourceLocation UID = ResourceUtils.loc("runecarving_table");
    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceUtils.loc("textures/gui/jei/runecarving_table.png");
    private static final ResourceLocation RESEARCH_TEXTURE = ResourceUtils.loc("textures/item/grimoire.png");
    private static final int RESEARCH_X_OFFSET = 79;
    private static final int RESEARCH_Y_OFFSET = 19;
    private static final int BG_WIDTH = 125;
    private static final int BG_HEIGHT = 36;

    private final IDrawableStatic background;
    private final IDrawableStatic researchIcon;

    public RunecarvingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, UID, "block.primalmagick.runecarving_table");
        this.researchIcon = guiHelper.drawableBuilder(RESEARCH_TEXTURE, 0, 0, 32, 32).setTextureSize(32, 32).build();
        this.background = guiHelper.createDrawable(BACKGROUND_TEXTURE, 0, 0, BG_WIDTH, BG_HEIGHT);
        this.setIcon(new ItemStack(ItemsPM.RUNECARVING_TABLE.get()));
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<IRunecarvingRecipe> recipeHolder, IFocusGroup focuses) {
        IRunecarvingRecipe recipe = recipeHolder.value();
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 50, 1).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 1).addItemStack(RecipeUtils.getResultItem(recipe));
    }

    @Override
    public void draw(RecipeHolder<IRunecarvingRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics);
        if (recipe.value().getRequirement().isPresent()) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
            this.researchIcon.draw(guiGraphics, RESEARCH_X_OFFSET * 2, RESEARCH_Y_OFFSET * 2);
            guiGraphics.pose().popPose();
        }
    }

    @Override
    public void getTooltip(ITooltipBuilder builder, RecipeHolder<IRunecarvingRecipe> recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        Optional<AbstractRequirement<?>> requirementOpt = recipe.value().getRequirement();
        if ( requirementOpt.isPresent() &&
             mouseX >= RESEARCH_X_OFFSET && mouseX < RESEARCH_X_OFFSET + this.researchIcon.getWidth() &&
             mouseY >= RESEARCH_Y_OFFSET && mouseY < RESEARCH_Y_OFFSET + this.researchIcon.getHeight() ) {
            builder.addAll(JeiHelper.getRequirementTooltipStrings(mc.level.registryAccess(), requirementOpt.get()));
        } else {
            super.getTooltip(builder, recipe, recipeSlotsView, mouseX, mouseY);
        }
    }

    @Override
    public RecipeType<RecipeHolder<IRunecarvingRecipe>> getRecipeType() {
        return JeiRecipeTypesPM.RUNECARVING;
    }
}
