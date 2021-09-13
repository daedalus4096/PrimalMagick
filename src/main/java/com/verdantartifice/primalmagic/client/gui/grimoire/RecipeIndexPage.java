package com.verdantartifice.primalmagic.client.gui.grimoire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

/**
 * Grimoire page listing all of the mod recipes that the player has unlocked.
 * 
 * @author Daedalus4096
 */
public class RecipeIndexPage extends AbstractPage {
    public static final String TOPIC = "recipe_index";
    
    protected List<ResourceLocation> contents = new ArrayList<>();
    protected boolean firstPage;

    public RecipeIndexPage() {
        this(false);
    }
    
    public RecipeIndexPage(boolean first) {
        this.firstPage = first;
    }
    
    @Nonnull
    public List<ResourceLocation> getContents() {
        return Collections.unmodifiableList(this.contents);
    }
    
    public boolean addContent(ResourceLocation entry) {
        return this.contents.add(entry);
    }
    
    public boolean isFirstPage() {
        return this.firstPage;
    }
    
    @Override
    protected String getTitleTranslationKey() {
        return "primalmagic.grimoire.recipe_index_header";
    }

    @Override
    public void render(PoseStack matrixStack, int side, int x, int y, int mouseX, int mouseY) {
        // Just render the title; buttons have already been added
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(matrixStack, side, x, y, mouseX, mouseY, ItemsPM.ARCANE_WORKBENCH.get().getRegistryName());
        }
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        Minecraft mc = screen.getMinecraft();
        RecipeManager recipeManager = mc.level.getRecipeManager();
        for (ResourceLocation loc : this.getContents()) {
            // TODO Render a recipe entry button for each recipe
            recipeManager.byKey(loc).ifPresent(recipe -> {
                
            });
        }
    }

}
