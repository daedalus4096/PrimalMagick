package com.verdantartifice.primalmagick.client.compat.jei.concocting;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.compat.jei.RecipeCategoryPM;
import com.verdantartifice.primalmagick.client.compat.jei.JeiRecipeTypesPM;
import com.verdantartifice.primalmagick.common.crafting.IConcoctingRecipe;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
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
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ConcoctingRecipeCategory extends RecipeCategoryPM<IConcoctingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(PrimalMagick.MODID, "concocter");
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/jei/arcane_workbench.png");
    private static final ResourceLocation RESEARCH_TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/item/grimoire.png");
    private static final int MANA_COST_X_OFFSET = 64;
    private static final int MANA_COST_Y_OFFSET = 1;
    private static final int RESEARCH_X_OFFSET = 64;
    private static final int RESEARCH_Y_OFFSET = 36;

    private final ICraftingGridHelper craftingGridHelper;
    private final IDrawableStatic manaCostIcon;
    private final IDrawableStatic researchIcon;

    public ConcoctingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, UID, "block.primalmagick.concocter");
        this.craftingGridHelper = guiHelper.createCraftingGridHelper();
        this.manaCostIcon = guiHelper.createDrawable(BACKGROUND_TEXTURE, 116, 0, 16, 16);
        this.researchIcon = guiHelper.drawableBuilder(RESEARCH_TEXTURE, 0, 0, 32, 32).setTextureSize(32, 32).build();
        this.setBackground(guiHelper.createDrawable(BACKGROUND_TEXTURE, 0, 0, 116, 54));
        this.setIcon(new ItemStack(ItemsPM.CONCOCTER.get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IConcoctingRecipe recipe, IFocusGroup focuses) {
        // Initialize recipe output
        this.craftingGridHelper.createAndSetOutputs(builder, VanillaTypes.ITEM_STACK, List.of(recipe.getResultItem()));
        
        // Initialize recipe inputs
        List<List<ItemStack>> inputs = recipe.getIngredients().stream().map(ingredient -> List.of(ingredient.getItems())).toList();
        this.craftingGridHelper.createAndSetInputs(builder, VanillaTypes.ITEM_STACK, inputs, 0, 0);
    }

    @Override
    public void draw(IConcoctingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        if (recipe.getManaCosts() != null && !recipe.getManaCosts().isEmpty()) {
            this.manaCostIcon.draw(stack, MANA_COST_X_OFFSET, MANA_COST_Y_OFFSET);
        }
        if (recipe.getRequiredResearch() != null && !recipe.getRequiredResearch().getKeys().isEmpty()) {
            stack.pushPose();
            stack.scale(0.5F, 0.5F, 0.5F);
            this.researchIcon.draw(stack, RESEARCH_X_OFFSET * 2, RESEARCH_Y_OFFSET * 2);
            stack.popPose();
        }
    }

    @Override
    public List<Component> getTooltipStrings(IConcoctingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        SourceList manaCosts = recipe.getManaCosts();
        CompoundResearchKey compoundResearch = recipe.getRequiredResearch();
        if ( manaCosts != null && !manaCosts.isEmpty() && 
             mouseX >= MANA_COST_X_OFFSET && mouseX < MANA_COST_X_OFFSET + this.manaCostIcon.getWidth() &&
             mouseY >= MANA_COST_Y_OFFSET && mouseY < MANA_COST_Y_OFFSET + this.manaCostIcon.getHeight() ) {
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.translatable("primalmagick.crafting.mana_cost_header"));
            for (Source source : manaCosts.getSourcesSorted()) {
                tooltip.add(Component.translatable("primalmagick.crafting.mana_tooltip", manaCosts.getAmount(source), source.getNameText()));
            }
            return tooltip;
        } else if ( compoundResearch != null && !compoundResearch.getKeys().isEmpty() &&
                    mouseX >= RESEARCH_X_OFFSET && mouseX < RESEARCH_X_OFFSET + this.researchIcon.getWidth() &&
                    mouseY >= RESEARCH_Y_OFFSET && mouseY < RESEARCH_Y_OFFSET + this.researchIcon.getHeight() ) {
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.translatable("primalmagick.crafting.research_header"));
            for (SimpleResearchKey key : compoundResearch.getKeys()) {
                ResearchEntry entry = ResearchEntries.getEntry(key);
                if (entry == null) {
                    tooltip.add(Component.translatable("primalmagick.research." + key.getRootKey() + ".text"));
                } else {
                    MutableComponent comp = Component.translatable(entry.getNameTranslationKey());
                    ResearchDiscipline disc = ResearchDisciplines.getDiscipline(entry.getDisciplineKey());
                    if (disc != null) {
                        comp.append(Component.literal(" ("));
                        comp.append(Component.translatable(disc.getNameTranslationKey()));
                        comp.append(Component.literal(")"));
                    }
                    tooltip.add(comp);
                }
            }
            return tooltip;
        } else {
            return super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
        }
    }

    @Override
    public RecipeType<IConcoctingRecipe> getRecipeType() {
        return JeiRecipeTypesPM.CONCOCTING;
    }
}
