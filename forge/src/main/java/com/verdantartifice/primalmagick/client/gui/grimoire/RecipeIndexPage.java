package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RecipeEntryButton;
import com.verdantartifice.primalmagick.common.research.topics.OtherResearchTopic;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * Grimoire page listing all of the mod recipes that the player has unlocked.
 * 
 * @author Daedalus4096
 */
public class RecipeIndexPage extends AbstractPage {
    public static final OtherResearchTopic TOPIC = new OtherResearchTopic("recipe_index", 0);
    protected static final Component SEARCH_HINT = (Component.translatable("gui.recipebook.search_hint")).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY);
    protected static final Logger LOGGER = LogManager.getLogger();
    
    protected List<IndexItem> contents = new ArrayList<>();
    protected boolean firstPage;
    protected Optional<String> startingSearchText;
    
    @Nullable
    protected EditBox searchBox;
    @Nullable
    protected GrimoireScreen screen;

    public RecipeIndexPage() {
        this(false, Optional.empty());
    }
    
    public RecipeIndexPage(boolean first, Optional<String> searchText) {
        this.firstPage = first;
        this.startingSearchText = searchText;
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
    protected Component getTitleText() {
        return Component.translatable("grimoire.primalmagick.recipe_index_header");
    }

    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        // Just render the title; buttons have already been added
        if (this.isFirstPage() && side == 0) {
            this.renderTitle(guiGraphics, side, x, y, mouseX, mouseY, null);
            if (this.searchBox != null) {
                this.searchBox.render(guiGraphics, mouseX, mouseY, 0F);
            }
        }
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        this.screen = screen;
        
        // Initialize a search box for the first side of the first page
        if (this.isFirstPage() && side == 0) {
            Minecraft mc = Minecraft.getInstance();
            this.searchBox = new EditBox(mc.font, x + 12, y + 3, 121, 14, Component.translatable("itemGroup.search"));
            this.searchBox.setMaxLength(50);
            this.searchBox.setBordered(true);
            this.searchBox.setVisible(true);
            this.searchBox.setTextColor(0xFFFFFF);
            this.searchBox.setHint(SEARCH_HINT);
            this.searchBox.setEditable(true);
            this.startingSearchText.ifPresentOrElse(text -> {
                this.searchBox.setValue(text);
                this.searchBox.setFocused(true);
            }, () -> {
                this.searchBox.setValue("");
                this.searchBox.setFocused(false);
            });
            y += 24;
        }
        
        for (IndexItem item : this.getContents()) {
            // Render a recipe entry button for each recipe
            screen.addWidgetToScreen(new RecipeEntryButton(x + 12 + (side * 140), y, Component.literal(item.name), screen, item.name, item.iconStack));
            y += 12;
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.searchBox != null && this.searchBox.mouseClicked(pMouseX, pMouseY, pButton)) {
            this.searchBox.setFocused(true);
            return true;
        } else if (this.searchBox != null) {
            this.searchBox.setFocused(false);
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (this.searchBox != null && this.searchBox.isFocused()) {
            if (this.searchBox.keyPressed(pKeyCode, pScanCode, pModifiers)) {
                if (this.screen != null) {
                    this.screen.checkRecipeSearchStringUpdate(this.searchBox.getValue());
                }
                return true;
            }
        }
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean charTyped(char pCodePoint, int pModifiers) {
        if (this.searchBox != null && this.searchBox.isFocused() && this.searchBox.charTyped(pCodePoint, pModifiers)) {
            if (this.screen != null) {
                this.screen.checkRecipeSearchStringUpdate(this.searchBox.getValue());
            }
            return true;
        }
        return super.charTyped(pCodePoint, pModifiers);
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
