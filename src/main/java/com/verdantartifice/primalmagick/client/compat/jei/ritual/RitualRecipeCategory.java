package com.verdantartifice.primalmagick.client.compat.jei.ritual;

import java.awt.Color;
import java.util.List;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.compat.jei.JeiHelper;
import com.verdantartifice.primalmagick.client.compat.jei.JeiRecipeTypesPM;
import com.verdantartifice.primalmagick.client.compat.jei.RecipeCategoryPM;
import com.verdantartifice.primalmagick.client.util.RecipeUtils;
import com.verdantartifice.primalmagick.common.crafting.IRitualRecipe;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

/**
 * Recipe category class for a ritual recipe.
 * 
 * @author Daedalus4096
 */
public class RitualRecipeCategory extends RecipeCategoryPM<RecipeHolder<IRitualRecipe>> {
    public static final ResourceLocation UID = PrimalMagick.resource("ritual_altar");
    private static final ResourceLocation BACKGROUND_TEXTURE = PrimalMagick.resource("textures/gui/jei/ritual_altar.png");
    private static final ResourceLocation RESEARCH_TEXTURE = PrimalMagick.resource("textures/item/grimoire.png");
    private static final int MANA_COST_X_OFFSET = 118;
    private static final int MANA_COST_Y_OFFSET = 14;
    private static final int RESEARCH_X_OFFSET = 118;
    private static final int RESEARCH_Y_OFFSET = 49;

    private final IDrawableStatic manaCostIcon;
    private final IDrawableStatic researchIcon;

    public RitualRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, UID, "block.primalmagick.ritual_altar");
        this.manaCostIcon = guiHelper.createDrawable(BACKGROUND_TEXTURE, 170, 0, 16, 16);
        this.researchIcon = guiHelper.drawableBuilder(RESEARCH_TEXTURE, 0, 0, 32, 32).setTextureSize(32, 32).build();
        this.setBackground(guiHelper.createDrawable(BACKGROUND_TEXTURE, 0, 0, 170, 80));
        this.setIcon(new ItemStack(ItemsPM.RITUAL_ALTAR.get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<IRitualRecipe> recipeHolder, IFocusGroup focuses) {
        IRitualRecipe recipe = recipeHolder.value();
        List<Ingredient> offerings = recipe.getIngredients();
        List<Ingredient> props = recipe.getProps().stream().map(b -> b.asIngredient()).toList();
        int ingredientCount = recipe.getIngredients().size();
        int propCount = recipe.getProps().size();
        
        for (int index = 0; index < ingredientCount; index++) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1 + (index % 6) * 18, 14 + ((index / 6) * 18)).addIngredients(offerings.get(index));
        }
        for (int index = 0; index < propCount; index++) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1 + (index % 6) * 18, 63 + ((index / 6) * 18)).addIngredients(props.get(index));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 149, 32).addItemStack(RecipeUtils.getResultItem(recipe));
    }

    @Override
    public void draw(RecipeHolder<IRitualRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        guiGraphics.drawString(mc.font, Component.translatable("jei.primalmagick.ritual.offerings.header"), 0, 2, Color.BLACK.getRGB(), false);
        guiGraphics.drawString(mc.font, Component.translatable("jei.primalmagick.ritual.props.header"), 0, 51, Color.BLACK.getRGB(), false);
        
        IRitualRecipe recipe = recipeHolder.value();
        if (recipe.getManaCosts() != null && !recipe.getManaCosts().isEmpty()) {
            this.manaCostIcon.draw(guiGraphics, MANA_COST_X_OFFSET, MANA_COST_Y_OFFSET);
        }
        if (recipe.getRequiredResearch() != null && !recipe.getRequiredResearch().getKeys().isEmpty()) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
            this.researchIcon.draw(guiGraphics, RESEARCH_X_OFFSET * 2, RESEARCH_Y_OFFSET * 2);
            guiGraphics.pose().popPose();
        }
    }

    @Override
    public List<Component> getTooltipStrings(RecipeHolder<IRitualRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        IRitualRecipe recipe = recipeHolder.value();
        SourceList manaCosts = recipe.getManaCosts();
        CompoundResearchKey compoundResearch = recipe.getRequiredResearch();
        if ( manaCosts != null && !manaCosts.isEmpty() && 
             mouseX >= MANA_COST_X_OFFSET && mouseX < MANA_COST_X_OFFSET + this.manaCostIcon.getWidth() &&
             mouseY >= MANA_COST_Y_OFFSET && mouseY < MANA_COST_Y_OFFSET + this.manaCostIcon.getHeight() ) {
            return JeiHelper.getManaCostTooltipStrings(manaCosts);
        } else if ( compoundResearch != null && !compoundResearch.getKeys().isEmpty() &&
                    mouseX >= RESEARCH_X_OFFSET && mouseX < RESEARCH_X_OFFSET + this.researchIcon.getWidth() &&
                    mouseY >= RESEARCH_Y_OFFSET && mouseY < RESEARCH_Y_OFFSET + this.researchIcon.getHeight() ) {
            return JeiHelper.getRequiredResearchTooltipStrings(compoundResearch);
        } else {
            return super.getTooltipStrings(recipeHolder, recipeSlotsView, mouseX, mouseY);
        }
    }

    @Override
    public RecipeType<RecipeHolder<IRitualRecipe>> getRecipeType() {
        return JeiRecipeTypesPM.RITUAL;
    }
}
