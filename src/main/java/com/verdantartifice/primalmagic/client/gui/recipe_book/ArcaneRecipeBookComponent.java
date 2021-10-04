package com.verdantartifice.primalmagic.client.gui.recipe_book;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.client.recipe_book.ClientArcaneRecipeBook;
import com.verdantartifice.primalmagic.common.containers.AbstractArcaneRecipeBookMenu;

import net.minecraft.ChatFormatting;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.recipebook.GhostRecipe;
import net.minecraft.client.gui.screens.recipebook.RecipeShownListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

/**
 * Screen component for the arcane recipe book.
 * 
 * @author Daedalus4096
 */
public class ArcaneRecipeBookComponent extends GuiComponent implements Widget, GuiEventListener, NarratableEntry, RecipeShownListener, PlaceRecipe<Ingredient> {
    protected static final ResourceLocation RECIPE_BOOK_LOCATION = new ResourceLocation("textures/gui/recipe_book.png");
    protected static final Component SEARCH_HINT = (new TranslatableComponent("gui.recipebook.search_hint")).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY);
    public static final int IMAGE_WIDTH = 147;
    public static final int IMAGE_HEIGHT = 166;
    private static final int OFFSET_X_POSITION = 86;
    private static final Component ONLY_CRAFTABLES_TOOLTIP = new TranslatableComponent("gui.recipebook.toggleRecipes.craftable");
    private static final Component ALL_RECIPES_TOOLTIP = new TranslatableComponent("gui.recipebook.toggleRecipes.all");
    
    protected int xOffset;
    protected int width;
    protected int height;
    protected final GhostRecipe ghostRecipe = new GhostRecipe();
    protected final List<ArcaneRecipeBookTabButton> tabButtons = new ArrayList<>();
    @Nullable
    protected ArcaneRecipeBookTabButton selectedTab;
    protected StateSwitchingButton filterButton;
    protected AbstractArcaneRecipeBookMenu<?> menu;
    protected Minecraft mc;
    @Nullable
    protected EditBox searchBox;
    protected String lastSearch = "";
    protected ClientRecipeBook vanillaBook;
    protected ClientArcaneRecipeBook arcaneBook;

    @Override
    public void updateNarration(NarrationElementOutput p_169152_) {
        // TODO Auto-generated method stub

    }

    @Override
    public void recipesShown(List<Recipe<?>> p_100518_) {
        // TODO Auto-generated method stub

    }

    @Override
    public NarrationPriority narrationPriority() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void render(PoseStack p_94669_, int p_94670_, int p_94671_, float p_94672_) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addItemToSlot(Iterator<Ingredient> p_135415_, int p_135416_, int p_135417_, int p_135418_, int p_135419_) {
        // TODO Auto-generated method stub
        
    }

}
