package com.verdantartifice.primalmagick.client.compat.jei.arcane_crafting;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.compat.jei.RecipeCategoryPM;
import com.verdantartifice.primalmagick.client.compat.jei.JeiRecipeTypesPM;
import com.verdantartifice.primalmagick.common.crafting.IArcaneRecipe;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.IShapedRecipe;

/**
 * Recipe category class for an arcane workbench recipe.
 * 
 * @author Daedalus4096
 */
public class ArcaneCraftingRecipeCategory extends RecipeCategoryPM<IArcaneRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(PrimalMagick.MODID, "arcane_workbench");
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/jei/arcane_workbench.png");
    private static final int MANA_COST_X_OFFSET = 64;
    private static final int MANA_COST_Y_OFFSET = 1;
    
    private final ICraftingGridHelper craftingGridHelper;
    private final IDrawableStatic manaCostIcon;

    public ArcaneCraftingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, UID, "block.primalmagick.arcane_workbench");
        this.craftingGridHelper = guiHelper.createCraftingGridHelper();
        this.manaCostIcon = guiHelper.createDrawable(BACKGROUND_TEXTURE, 116, 0, 16, 16);
        this.setBackground(guiHelper.createDrawable(BACKGROUND_TEXTURE, 0, 0, 116, 54));
        this.setIcon(new ItemStack(ItemsPM.ARCANE_WORKBENCH.get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IArcaneRecipe recipe, IFocusGroup focuses) {
        // Initialize recipe output
        this.craftingGridHelper.setOutputs(builder, VanillaTypes.ITEM_STACK, List.of(recipe.getResultItem()));
        
        // Initialize recipe inputs
        int width = (recipe instanceof IShapedRecipe<?> shapedRecipe) ? shapedRecipe.getRecipeWidth() : 0;
        int height = (recipe instanceof IShapedRecipe<?> shapedRecipe) ? shapedRecipe.getRecipeHeight() : 0;
        List<List<ItemStack>> inputs = recipe.getIngredients().stream().map(ingredient -> List.of(ingredient.getItems())).toList();
        this.craftingGridHelper.setInputs(builder, VanillaTypes.ITEM_STACK, inputs, width, height);
    }

    @Override
    public void draw(IArcaneRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        if (recipe.getManaCosts() != null && !recipe.getManaCosts().isEmpty()) {
            this.manaCostIcon.draw(stack, MANA_COST_X_OFFSET, MANA_COST_Y_OFFSET);
        }
    }

    @Override
    public List<Component> getTooltipStrings(IArcaneRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
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
    public RecipeType<IArcaneRecipe> getRecipeType() {
        return JeiRecipeTypesPM.ARCANE_CRAFTING;
    }
}
