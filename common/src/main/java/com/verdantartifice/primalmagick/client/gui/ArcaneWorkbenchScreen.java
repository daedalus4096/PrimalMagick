package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.client.gui.recipe_book.ArcaneCraftingRecipeBookComponent;
import com.verdantartifice.primalmagick.client.gui.widgets.ManaCostWidget;
import com.verdantartifice.primalmagick.common.crafting.display.IManaCostRecipeDisplay;
import com.verdantartifice.primalmagick.common.menus.ArcaneWorkbenchMenu;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * GUI screen for arcane workbench block.
 *
 * @author Daedalus4096
 */
public class ArcaneWorkbenchScreen extends AbstractRecipeBookScreen<ArcaneWorkbenchMenu> {
    private static final Identifier TEXTURE = ResourceUtils.loc("textures/gui/arcane_workbench.png");

    protected List<ManaCostWidget> costWidgets = new ArrayList<>();

    public ArcaneWorkbenchScreen(ArcaneWorkbenchMenu menu, Inventory inventory, Component title) {
        super(menu, new ArcaneCraftingRecipeBookComponent(menu), inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.initCostWidgets();
        this.titleLabelX = 29;
    }

    @Override
    @NotNull
    protected ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(this.leftPos + 105, this.topPos + 69);
    }

    @Override
    public void extractBackground(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        int xo = this.leftPos;
        int yo = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    public void extractRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        this.adjustCostWidgets();
        super.extractRenderState(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void extractLabels(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractLabels(graphics, mouseX, mouseY);

        // Generate text in the case that the current recipe, or lack there of, does not have a mana cost
        RecipeDisplay display = this.menu.getActiveRecipeDisplay();
        if (display == null || (display instanceof IManaCostRecipeDisplay manaDisplay && manaDisplay.manaCosts().isEmpty())) {
            Component text = Component.translatable("label.primalmagick.crafting.no_mana");
            int width = this.font.width(text.getString());
            int x = 1 + (this.imageWidth - width) / 2;
            int y = 10 + (16 - this.font.lineHeight) / 2;
            graphics.text(this.font, text, x, y, 0, false);
        }
    }

    protected void initCostWidgets() {
        this.costWidgets.clear();
        int widgetSetWidth = Sources.getAllSorted().size() * 18;
        int x = this.leftPos + 1 + (this.imageWidth - widgetSetWidth) / 2;
        int y = this.topPos + 10;
        for (Source source : Sources.getAllSorted()) {
            this.costWidgets.add(this.addRenderableWidget(new ManaCostWidget(source, 0, x, y, this.menu::getWand, this.menu.owner())));
            x += 18;
        }
    }

    protected void adjustCostWidgets() {
        RecipeDisplay activeArcaneRecipe = this.menu.getActiveRecipeDisplay();
        if (activeArcaneRecipe instanceof IManaCostRecipeDisplay display) {
            SourceList manaCosts = display.manaCosts();
            int widgetSetWidth = manaCosts.getSourcesSorted().size() * 18;
            int dx = 0;
            for (ManaCostWidget widget : this.costWidgets) {
                int amount = manaCosts.getAmount(widget.getSource());
                widget.visible = (amount > 0);
                if (widget.visible) {
                    widget.setAmount(amount);
                    widget.setX(this.leftPos + 1 + dx + (this.imageWidth - widgetSetWidth) / 2);
                    dx += 18;
                }
            }
        } else {
            this.costWidgets.forEach(widget -> widget.visible = false);
        }
    }
}
