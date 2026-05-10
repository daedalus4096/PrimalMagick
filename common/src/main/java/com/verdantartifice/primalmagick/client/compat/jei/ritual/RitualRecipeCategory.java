package com.verdantartifice.primalmagick.client.compat.jei.ritual;

import com.verdantartifice.primalmagick.client.compat.jei.JeiHelper;
import com.verdantartifice.primalmagick.client.compat.jei.JeiRecipeTypesPM;
import com.verdantartifice.primalmagick.client.compat.jei.RecipeCategoryPM;
import com.verdantartifice.primalmagick.common.crafting.IRitualRecipe;
import com.verdantartifice.primalmagick.common.crafting.display.RitualRecipeDisplay;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.List;
import java.util.Optional;

/**
 * Recipe category class for a ritual recipe.
 * 
 * @author Daedalus4096
 */
public class RitualRecipeCategory extends RecipeCategoryPM<RecipeHolder<IRitualRecipe>> {
    public static final Identifier UID = ResourceUtils.loc("ritual_altar");
    private static final Identifier BACKGROUND_TEXTURE = ResourceUtils.loc("textures/gui/jei/ritual_altar.png");
    private static final Identifier RESEARCH_TEXTURE = ResourceUtils.loc("textures/item/grimoire.png");
    private static final int MANA_COST_X_OFFSET = 118;
    private static final int MANA_COST_Y_OFFSET = 14;
    private static final int RESEARCH_X_OFFSET = 118;
    private static final int RESEARCH_Y_OFFSET = 49;
    private static final int BG_WIDTH = 170;
    private static final int BG_HEIGHT = 80;

    private final IDrawableStatic background;
    private final IDrawableStatic manaCostIcon;
    private final IDrawableStatic researchIcon;

    public RitualRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, UID, "block.primalmagick.ritual_altar");
        this.manaCostIcon = guiHelper.createDrawable(BACKGROUND_TEXTURE, 170, 0, 16, 16);
        this.researchIcon = guiHelper.drawableBuilder(RESEARCH_TEXTURE, 0, 0, 32, 32).setTextureSize(32, 32).build();
        this.background = guiHelper.createDrawable(BACKGROUND_TEXTURE, 0, 0, BG_WIDTH, BG_HEIGHT);
        this.setIcon(new ItemStack(ItemsPM.RITUAL_ALTAR.get()));
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
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RecipeHolder<IRitualRecipe> recipeHolder, @NotNull IFocusGroup focuses) {
        RecipeDisplay display = recipeHolder.value().display().getFirst();
        if (display instanceof RitualRecipeDisplay ritualDisplay) {
            List<SlotDisplay> ingredients = ritualDisplay.ingredients();
            List<SlotDisplay> props = ritualDisplay.props();
            for (int index = 0; index < ingredients.size(); index++) {
                builder.addInputSlot(1 + (index % 6) * 18, 14 + ((index / 6) * 18)).add(ingredients.get(index));
            }
            for (int index = 0; index < props.size(); index++) {
                builder.addInputSlot(1 + (index % 6) * 18, 63 + ((index / 6) * 18)).add(props.get(index));
            }
            builder.addOutputSlot(149, 32).add(ritualDisplay.result());
        }
    }

    @Override
    public void draw(@NotNull RecipeHolder<IRitualRecipe> recipeHolder, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics);

        Minecraft mc = Minecraft.getInstance();
        guiGraphics.text(mc.font, Component.translatable("jei.primalmagick.ritual.offerings.header"), 0, 2, Color.BLACK.getRGB(), false);
        guiGraphics.text(mc.font, Component.translatable("jei.primalmagick.ritual.props.header"), 0, 51, Color.BLACK.getRGB(), false);
        
        IRitualRecipe recipe = recipeHolder.value();
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
    public void getTooltip(@NotNull ITooltipBuilder builder, @NotNull RecipeHolder<IRitualRecipe> recipeHolder, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        IRitualRecipe recipe = recipeHolder.value();
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
    public IRecipeType<RecipeHolder<IRitualRecipe>> getRecipeType() {
        return JeiRecipeTypesPM.RITUAL;
    }
}
