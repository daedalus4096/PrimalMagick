package com.verdantartifice.primalmagick.client.compat.jei.ritual;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.compat.jei.RecipeCategoryPM;
import com.verdantartifice.primalmagick.client.compat.jei.RecipeTypesPM;
import com.verdantartifice.primalmagick.common.crafting.IRitualRecipe;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Recipe category class for a ritual recipe.
 * 
 * @author Daedalus4096
 */
public class RitualRecipeCategory extends RecipeCategoryPM<IRitualRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(PrimalMagick.MODID, "ritual_altar");
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/jei/ritual_altar.png");
    private static final int MANA_COST_X_OFFSET = 118;
    private static final int MANA_COST_Y_OFFSET = 14;

    private final IDrawableStatic manaCostIcon;
    
    public RitualRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, UID, "block.primalmagick.ritual_altar");
        this.manaCostIcon = guiHelper.createDrawable(BACKGROUND_TEXTURE, 170, 0, 16, 16);
        this.setBackground(guiHelper.createDrawable(BACKGROUND_TEXTURE, 0, 0, 170, 80));
        this.setIcon(new ItemStack(ItemsPM.RITUAL_ALTAR.get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IRitualRecipe recipe, IFocusGroup focuses) {
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
        builder.addSlot(RecipeIngredientRole.OUTPUT, 149, 32).addItemStack(recipe.getResultItem());
    }

    @Override
    public void draw(IRitualRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        Minecraft mc = Minecraft.getInstance();
        mc.font.draw(stack, Component.translatable("gui.primalmagick.jei.ritual.offerings.header"), 0, 2, 0xFF808080);
        mc.font.draw(stack, Component.translatable("gui.primalmagick.jei.ritual.props.header"), 0, 51, 0xFF808080);
        
        if (recipe.getManaCosts() != null && !recipe.getManaCosts().isEmpty()) {
            this.manaCostIcon.draw(stack, MANA_COST_X_OFFSET, MANA_COST_Y_OFFSET);
        }
    }

    @Override
    public List<Component> getTooltipStrings(IRitualRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        SourceList manaCosts = recipe.getManaCosts();
        if ( manaCosts != null && !manaCosts.isEmpty() && 
             mouseX >= MANA_COST_X_OFFSET && mouseX < MANA_COST_X_OFFSET + this.manaCostIcon.getWidth() &&
             mouseY >= MANA_COST_Y_OFFSET && mouseY < MANA_COST_Y_OFFSET + this.manaCostIcon.getHeight() ) {
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.translatable("primalmagick.crafting.mana_cost_header"));
            for (Source source : manaCosts.getSourcesSorted()) {
                tooltip.add(Component.translatable("primalmagick.crafting.mana_tooltip", manaCosts.getAmount(source), source.getNameText()));
            }
            return tooltip;
        } else {
            return super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
        }
    }

    @Override
    public RecipeType<IRitualRecipe> getRecipeType() {
        return RecipeTypesPM.RITUAL;
    }
}
