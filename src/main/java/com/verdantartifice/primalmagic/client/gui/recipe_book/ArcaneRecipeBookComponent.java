package com.verdantartifice.primalmagic.client.gui.recipe_book;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.client.recipe_book.ArcaneRecipeBookCategories;
import com.verdantartifice.primalmagic.client.recipe_book.ClientArcaneRecipeBook;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.containers.AbstractArcaneRecipeBookMenu;
import com.verdantartifice.primalmagic.common.crafting.recipe_book.ArcaneRecipeBookType;

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
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.gui.screens.recipebook.RecipeShownListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
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
    protected final ArcaneRecipeBookPage recipeBookPage = new ArcaneRecipeBookPage();
    protected final StackedContents stackedContents = new StackedContents();
    protected int timesInventoryChanged;
    protected boolean ignoreTextInput;
    protected boolean visible;
    protected boolean widthTooNarrow;

    public void init(int width, int height, Minecraft mc, boolean tooNarrow, AbstractArcaneRecipeBookMenu<?> menu) {
        this.mc = mc;
        this.width = width;
        this.height = height;
        this.menu = menu;
        this.widthTooNarrow = tooNarrow;
        mc.player.containerMenu = menu;
        this.vanillaBook = mc.player.getRecipeBook();
        this.arcaneBook = new ClientArcaneRecipeBook(PrimalMagicCapabilities.getArcaneRecipeBook(mc.player).orElseThrow(() -> new IllegalArgumentException("No arcane recipe book for player")).get());
        this.visible = this.isVisibleAccordingToBookData();
        if (this.visible) {
            this.initVisuals();
        }
        mc.keyboardHandler.setSendRepeatsToGui(true);
    }
    
    public void initVisuals() {
        this.xOffset = this.widthTooNarrow ? 0 : OFFSET_X_POSITION;
        int xPos = (this.width - 147) / 2 - this.xOffset;
        int yPos = (this.height - 166) / 2;
        this.stackedContents.clear();
        this.mc.player.getInventory().fillStackedContents(this.stackedContents);
        this.menu.fillCraftSlotsStackedContents(this.stackedContents);
        String s = this.searchBox != null ? this.searchBox.getValue() : "";
        this.searchBox = new EditBox(this.mc.font, xPos + 25, yPos + 14, 80, 9 + 5, new TranslatableComponent("itemGroup.search"));
        this.searchBox.setMaxLength(50);
        this.searchBox.setBordered(false);
        this.searchBox.setVisible(true);
        this.searchBox.setTextColor(0xFFFFFF);
        this.searchBox.setValue(s);
        this.recipeBookPage.init(this.mc, xPos, yPos, this.arcaneBook.getData());
        this.recipeBookPage.addListener(this);
        this.filterButton = new StateSwitchingButton(xPos + 110, yPos + 12, 26, 16, this.arcaneBook.getData().isFiltering(this.menu.getRecipeBookType()));
        this.initFilterButtonTextures();
        this.tabButtons.clear();

        for (ArcaneRecipeBookCategories category : this.menu.getRecipeBookCategories()) {
            this.tabButtons.add(new ArcaneRecipeBookTabButton(category));
        }
        
        if (this.selectedTab != null) {
            this.selectedTab = this.tabButtons.stream().filter(tab -> {
                return tab.getCategory().equals(this.selectedTab.getCategory());
            }).findFirst().orElse(null);
        }
        if (this.selectedTab == null) {
            this.selectedTab = this.tabButtons.get(0);
        }
        
        this.selectedTab.setStateTriggered(true);
        this.updateCollections(false);
        this.updateTabs();
    }

    @Override
    public boolean changeFocus(boolean focus) {
        return false;
    }
    
    protected void initFilterButtonTextures() {
        this.filterButton.initTextureValues(152, 41, 28, 18, RECIPE_BOOK_LOCATION);
    }
    
    public void removed() {
        this.mc.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    public int updateScreenPosition(int width, int imageWidth) {
        int retVal;
        if (this.isVisible() && !this.widthTooNarrow) {
            retVal = 177 + (width - imageWidth - 200) / 2;
        } else {
            retVal = (width - imageWidth) / 2;
        }
        return retVal;
    }
    
    public void toggleVisibility() {
        this.setVisible(!this.isVisible());
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    protected boolean isVisibleAccordingToBookData() {
        return this.arcaneBook.getData().isOpen(this.menu.getRecipeBookType());
    }
    
    protected void setVisible(boolean visible) {
        if (visible) {
            this.initVisuals();
        }
        
        this.visible = visible;
        this.arcaneBook.getData().setOpen(this.menu.getRecipeBookType(), visible);
        if (!visible) {
            this.recipeBookPage.setInvisible();
        }
        
        this.sendUpdateSettings();
    }
    
    public void slotClicked(@Nullable Slot slot) {
        if (slot != null && slot.index < this.menu.getSize()) {
            this.ghostRecipe.clear();
            if (this.isVisible()) {
                this.updateStackedContents();
            }
        }
    }
    
    protected void updateCollections(boolean resetPage) {
        // TODO
    }
    
    protected void updateTabs() {
        int xPos = (this.width - IMAGE_WIDTH) / 2 - this.xOffset - 30;
        int yPos = (this.height - IMAGE_HEIGHT) / 2 + 3;
        int tabCount = 0;
        
        for (ArcaneRecipeBookTabButton tab : this.tabButtons) {
            ArcaneRecipeBookCategories category = tab.getCategory();
            if (category != ArcaneRecipeBookCategories.CRAFTING_SEARCH) {
                if (tab.updateVisibility(this.vanillaBook, this.arcaneBook)) {
                    tab.setPosition(xPos, yPos + 27 * tabCount++);
                    tab.startAnimation(this.mc);
                }
            } else {
                tab.visible = true;
                tab.setPosition(xPos, yPos + 27 * tabCount++);
            }
        }
    }
    
    public void tick() {
        boolean flag = this.isVisibleAccordingToBookData();
        if (this.isVisible() != flag) {
            this.setVisible(flag);
        }
        
        if (this.isVisible()) {
            if (this.timesInventoryChanged != this.mc.player.getInventory().getTimesChanged()) {
                this.updateStackedContents();
                this.timesInventoryChanged = this.mc.player.getInventory().getTimesChanged();
            }
            this.searchBox.tick();
        }
    }
    
    protected void updateStackedContents() {
        this.stackedContents.clear();
        this.mc.player.getInventory().fillStackedContents(this.stackedContents);
        this.menu.fillCraftSlotsStackedContents(this.stackedContents);
        this.updateCollections(false);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        if (this.isVisible()) {
            poseStack.pushPose();
            poseStack.translate(0.0D, 0.0D, 100.0D);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, RECIPE_BOOK_LOCATION);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int xPos = (this.width - 147) / 2 - this.xOffset;
            int yPos = (this.height - 166) / 2;
            this.blit(poseStack, xPos, yPos, 1, 1, IMAGE_WIDTH, IMAGE_HEIGHT);
            
            if (!this.searchBox.isFocused() && this.searchBox.getValue().isEmpty()) {
                drawString(poseStack, this.mc.font, SEARCH_HINT, xPos + 25, yPos + 14, -1);
            } else {
                this.searchBox.render(poseStack, mouseX, mouseY, partialTicks);
            }
            
            for (ArcaneRecipeBookTabButton tab : this.tabButtons) {
                tab.render(poseStack, mouseX, mouseY, partialTicks);
            }
            
            this.filterButton.render(poseStack, mouseX, mouseY, partialTicks);
            this.recipeBookPage.render(poseStack, xPos, yPos, mouseX, mouseY, partialTicks);
            poseStack.popPose();
        }
    }
    
    public void renderTooltip(PoseStack poseStack, int parentLeft, int parentTop, int mouseX, int mouseY) {
        if (this.isVisible()) {
            this.recipeBookPage.renderTooltip(poseStack, mouseX, mouseY);
            if (this.filterButton.isHovered() && this.mc.screen != null) {
                this.mc.screen.renderTooltip(poseStack, this.getFilterButtonTooltip(), mouseX, mouseY);
            }
            this.renderGhostRecipeTooltip(poseStack, parentLeft, parentTop, mouseX, mouseY);
        }
    }
    
    protected Component getFilterButtonTooltip() {
        return this.filterButton.isStateTriggered() ? this.getRecipeFilterName() : ALL_RECIPES_TOOLTIP;
    }
    
    protected Component getRecipeFilterName() {
        return ONLY_CRAFTABLES_TOOLTIP;
    }
    
    protected void renderGhostRecipeTooltip(PoseStack poseStack, int parentLeft, int parentTop, int mouseX, int mouseY) {
        ItemStack stack = null;
        for (int index = 0; index < this.ghostRecipe.size(); index++) {
            GhostRecipe.GhostIngredient ghostIngredient = this.ghostRecipe.get(index);
            int xPos = ghostIngredient.getX() + parentLeft;
            int yPos = ghostIngredient.getY() + parentTop;
            if (mouseX >= xPos && mouseY >= yPos && mouseX < xPos + 16 && mouseY < yPos + 16) {
                stack = ghostIngredient.getItem();
            }
        }
        if (stack != null && this.mc.screen != null) {
            this.mc.screen.renderComponentTooltip(poseStack, this.mc.screen.getTooltipFromItem(stack), mouseX, mouseY);
        }
    }
    
    public void renderGhostRecipe(PoseStack poseStack, int parentLeft, int parentTop, boolean largeSlot, float partialTicks) {
        this.ghostRecipe.render(poseStack, this.mc, parentLeft, parentTop, largeSlot, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonIndex) {
        if (this.isVisible() && !this.mc.player.isSpectator()) {
            if (this.recipeBookPage.mouseClicked(mouseX, mouseY, buttonIndex, (this.width - IMAGE_WIDTH) / 2 - this.xOffset, (this.height - IMAGE_HEIGHT) / 2, IMAGE_WIDTH, IMAGE_HEIGHT)) {
                Recipe<?> recipe = this.recipeBookPage.getLastClickedRecipe();
                RecipeCollection collection = this.recipeBookPage.getLastClickedRecipeCollection();
                if (recipe != null && collection != null) {
                    if (!collection.isCraftable(recipe) && this.ghostRecipe.getRecipe() == recipe) {
                        return false;
                    }
                    this.ghostRecipe.clear();
                    // TODO Handle place recipe
                    if (!this.isOffsetNextToMainGUI()) {
                        this.setVisible(false);
                    }
                }
                return true;
            } else if (this.searchBox.mouseClicked(mouseX, mouseY, buttonIndex)) {
                return true;
            } else if (this.filterButton.mouseClicked(mouseX, mouseY, buttonIndex)) {
                this.filterButton.setStateTriggered(this.toggleFiltering());
                this.sendUpdateSettings();
                this.updateCollections(false);
                return true;
            } else {
                for (ArcaneRecipeBookTabButton tab : this.tabButtons) {
                    if (tab.mouseClicked(mouseX, mouseY, buttonIndex)) {
                        if (this.selectedTab != tab) {
                            if (this.selectedTab != null) {
                                this.selectedTab.setStateTriggered(false);
                            }
                            this.selectedTab = tab;
                            this.selectedTab.setStateTriggered(true);
                            this.updateCollections(true);
                        }
                        return true;
                    }
                }
                return false;
            }
        } else {
            return false;
        }
    }
    
    protected boolean toggleFiltering() {
        ArcaneRecipeBookType type = this.menu.getRecipeBookType();
        boolean newValue = !this.arcaneBook.getData().isFiltering(type);
        this.arcaneBook.getData().setFiltering(type, newValue);
        return newValue;
    }

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
    public void addItemToSlot(Iterator<Ingredient> p_135415_, int p_135416_, int p_135417_, int p_135418_, int p_135419_) {
        // TODO Auto-generated method stub
        
    }

}
