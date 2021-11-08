package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RecipeEntryButton;

import net.minecraft.network.chat.TextComponent;

/**
 * Grimoire page listing all of the mod recipes that the player has unlocked.
 * 
 * @author Daedalus4096
 */
public class RecipeIndexPage extends AbstractPage {
    public static final String TOPIC = "recipe_index";
    
    protected List<String> contents = new ArrayList<>();
    protected boolean firstPage;

    public RecipeIndexPage() {
        this(false);
    }
    
    public RecipeIndexPage(boolean first) {
        this.firstPage = first;
    }
    
    @Nonnull
    public List<String> getContents() {
        return Collections.unmodifiableList(this.contents);
    }
    
    public boolean addContent(String entry) {
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
            this.renderTitle(matrixStack, side, x, y, mouseX, mouseY, null);
        }
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        for (String name : this.getContents()) {
            // Render a recipe entry button for each recipe
            screen.addWidgetToScreen(new RecipeEntryButton(x + 12 + (side * 140), y, new TextComponent(name), screen, name));
            y += 12;
        }
    }

}
