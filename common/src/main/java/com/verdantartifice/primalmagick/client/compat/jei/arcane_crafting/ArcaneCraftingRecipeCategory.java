package com.verdantartifice.primalmagick.client.compat.jei.arcane_crafting;

import com.verdantartifice.primalmagick.client.compat.jei.JeiHelper;
import com.verdantartifice.primalmagick.client.compat.jei.JeiRecipeTypesPM;
import com.verdantartifice.primalmagick.client.compat.jei.RecipeCategoryPM;
import com.verdantartifice.primalmagick.common.crafting.IArcaneRecipe;
import com.verdantartifice.primalmagick.common.crafting.display.ShapedArcaneCraftingRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.display.ShapelessArcaneCraftingRecipeDisplay;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Recipe category class for an arcane workbench recipe.
 * 
 * @author Daedalus4096
 */
public class ArcaneCraftingRecipeCategory extends RecipeCategoryPM<RecipeHolder<IArcaneRecipe>> {
    public static final Identifier UID = ResourceUtils.loc("arcane_workbench");
    private static final Identifier BACKGROUND_TEXTURE = ResourceUtils.loc("textures/gui/jei/arcane_workbench.png");
    private static final Identifier RESEARCH_TEXTURE = ResourceUtils.loc("textures/item/grimoire.png");
    private static final int MANA_COST_X_OFFSET = 64;
    private static final int MANA_COST_Y_OFFSET = 1;
    private static final int RESEARCH_X_OFFSET = 64;
    private static final int RESEARCH_Y_OFFSET = 36;
    private static final int BG_WIDTH = 116;
    private static final int BG_HEIGHT = 54;
    
    private final ICraftingGridHelper craftingGridHelper;
    private final IDrawableStatic background;
    private final IDrawableStatic manaCostIcon;
    private final IDrawableStatic researchIcon;

    public ArcaneCraftingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, UID, "block.primalmagick.arcane_workbench");
        this.craftingGridHelper = guiHelper.createCraftingGridHelper();
        this.manaCostIcon = guiHelper.createDrawable(BACKGROUND_TEXTURE, 116, 0, 16, 16);
        this.researchIcon = guiHelper.drawableBuilder(RESEARCH_TEXTURE, 0, 0, 32, 32).setTextureSize(32, 32).build();
        this.background = guiHelper.createDrawable(BACKGROUND_TEXTURE, 0, 0, BG_WIDTH, BG_HEIGHT);
        this.setIcon(new ItemStack(ItemsPM.ARCANE_WORKBENCH.get()));
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
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RecipeHolder<IArcaneRecipe> recipe, @NotNull IFocusGroup focuses) {
        RecipeDisplay display = recipe.value().display().getFirst();

        // Initialize recipe output
        this.craftingGridHelper.createAndSetOutputs(builder, display.result());

        // Initialize recipe inputs
        if (display instanceof ShapedArcaneCraftingRecipeDisplay shapedDisplay) {
            this.craftingGridHelper.createAndSetIngredientsFromDisplays(builder, shapedDisplay.ingredients(), shapedDisplay.width(), shapedDisplay.height());
        } else if (display instanceof ShapelessArcaneCraftingRecipeDisplay shapelessDisplay) {
            this.craftingGridHelper.createAndSetIngredientsFromDisplays(builder, shapelessDisplay.ingredients(), 0, 0);
            builder.setShapeless();
        }
    }

    @Override
    public void draw(@NotNull RecipeHolder<IArcaneRecipe> recipeHolder, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        IArcaneRecipe recipe = recipeHolder.value();
        this.background.draw(guiGraphics);
        if (!recipe.getManaCosts().isEmpty()) {
            this.manaCostIcon.draw(guiGraphics, MANA_COST_X_OFFSET, MANA_COST_Y_OFFSET);
        }
        if (recipe.getRequirement().isPresent()) {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().scale(0.5F, 0.5F);
            this.researchIcon.draw(guiGraphics, RESEARCH_X_OFFSET * 2, RESEARCH_Y_OFFSET * 2);
            guiGraphics.pose().popMatrix();
        }
    }

    @Override
    public void getTooltip(@NotNull ITooltipBuilder builder, @NotNull RecipeHolder<IArcaneRecipe> recipeHolder, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        IArcaneRecipe recipe = recipeHolder.value();
        SourceList manaCosts = recipe.getManaCosts();
        Optional<AbstractRequirement<?>> requirementOpt = recipe.getRequirement();
        if ( !manaCosts.isEmpty() &&
             mouseX >= MANA_COST_X_OFFSET && mouseX < MANA_COST_X_OFFSET + this.manaCostIcon.getWidth() &&
             mouseY >= MANA_COST_Y_OFFSET && mouseY < MANA_COST_Y_OFFSET + this.manaCostIcon.getHeight() ) {
            builder.addAll(JeiHelper.getManaCostTooltipStrings(manaCosts));
        } else if ( requirementOpt.isPresent() &&
                    mouseX >= RESEARCH_X_OFFSET && mouseX < RESEARCH_X_OFFSET + this.researchIcon.getWidth() &&
                    mouseY >= RESEARCH_Y_OFFSET && mouseY < RESEARCH_Y_OFFSET + this.researchIcon.getHeight() ) {
            builder.addAll(JeiHelper.getRequirementTooltipStrings(mc.level.registryAccess(), requirementOpt.get()));
        } else {
            super.getTooltip(builder, recipeHolder, recipeSlotsView, mouseX, mouseY);
        }
    }

    @Override
    @NotNull
    public IRecipeType<RecipeHolder<IArcaneRecipe>> getRecipeType() {
        return JeiRecipeTypesPM.ARCANE_CRAFTING;
    }
}
