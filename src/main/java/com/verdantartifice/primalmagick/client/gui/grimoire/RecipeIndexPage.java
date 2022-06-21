package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RecipeEntryButton;
import com.verdantartifice.primalmagick.common.research.topics.OtherResearchTopic;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * Grimoire page listing all of the mod recipes that the player has unlocked.
 * 
 * @author Daedalus4096
 */
public class RecipeIndexPage extends AbstractPage {
    public static final OtherResearchTopic TOPIC = new OtherResearchTopic("recipe_index", 0);
    
    protected List<IndexItem> contents = new ArrayList<>();
    protected boolean firstPage;

    public RecipeIndexPage() {
        this(false);
    }
    
    public RecipeIndexPage(boolean first) {
        this.firstPage = first;
    }
    
    @Nonnull
    public List<IndexItem> getContents() {
        return Collections.unmodifiableList(this.contents);
    }
    
    public boolean addContent(String entry, ItemStack stack) {
        return this.contents.add(new IndexItem(entry, stack));
    }
    
    public boolean isFirstPage() {
        return this.firstPage;
    }
    
    @Override
    protected String getTitleTranslationKey() {
        return "primalmagick.grimoire.recipe_index_header";
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
        for (IndexItem item : this.getContents()) {
            // Render a recipe entry button for each recipe
            screen.addWidgetToScreen(new RecipeEntryButton(x + 12 + (side * 140), y, Component.literal(item.name), screen, item.name, item.iconStack));
            y += 12;
        }
    }

    private static class IndexItem {
        public final String name;
        public final ItemStack iconStack;
        
        public IndexItem(String name, ItemStack stack) {
            this.name = name;
            this.iconStack = stack;
        }
    }
}
