package com.verdantartifice.primalmagick.client.gui.scribe_table;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2ic;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.gui.widgets.VocabularyWidget;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.books.ScribeTableMode;
import com.verdantartifice.primalmagick.common.books.grids.GridDefinition;
import com.verdantartifice.primalmagick.common.books.grids.PlayerGrid;
import com.verdantartifice.primalmagick.common.menus.ScribeGainComprehensionMenu;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for the gain comprehension mode of the scribe table block.
 * 
 * @author Daedalus4096
 */
public class ScribeGainComprehensionScreen extends AbstractScribeTableScreen<ScribeGainComprehensionMenu> {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/scribe_gain_comprehension.png");
    protected static final ResourceLocation PARCHMENT_SPRITE = PrimalMagick.resource("scribe_table/parchment");
    
    protected VocabularyWidget vocabularyWidget;
    protected PlayerGrid grid;
    
    public ScribeGainComprehensionScreen(ScribeGainComprehensionMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 230;
        this.imageHeight = 222;
    }

    @Override
    protected ScribeTableMode getMode() {
        return ScribeTableMode.GAIN_COMPREHENSION;
    }

    @Override
    protected ResourceLocation getBgTexture() {
        return TEXTURE;
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        // Don't render labels in this mode
    }

    @Override
    protected void init() {
        super.init();
        BookLanguage lang = this.menu.getBookLanguage();
        this.vocabularyWidget = this.addRenderableWidget(new VocabularyWidget(lang, this.menu.getVocabularyCount(), this.leftPos + 207, this.topPos + 7));
        this.grid = LinguisticsManager.getPlayerGrid(this.minecraft.player, lang.languageId());
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);

        // Update the vocabulary widget based on the current language in the menu
        BookLanguage lang = this.menu.getBookLanguage();
        this.vocabularyWidget.visible = lang.isComplex();
        this.vocabularyWidget.setLanguage(lang);
        this.vocabularyWidget.setAmount(this.menu.getVocabularyCount());
        
        boolean gridChanged = false;
        if (lang.isComplex()) {
            // Update the local player grid if the current language has changed
            if (this.grid == null || !lang.languageId().equals(this.grid.getDefinition().getKey())) {
                this.grid = LinguisticsManager.getPlayerGrid(this.minecraft.player, lang.languageId());
                gridChanged = true;
            }
            
            // Draw the parchment background for the comprehension grid
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(this.leftPos + 34, this.topPos + 7, 0);
            pGuiGraphics.pose().scale(0.5F, 0.5F, 1F);
            pGuiGraphics.blitSprite(PARCHMENT_SPRITE, 0, 0, 324, 256);
            pGuiGraphics.pose().popPose();
            
            // Draw a node button for each node in the grid definition
            if (gridChanged) {
                this.clearWidgets();
            }
            if (this.grid != null) {
                for (Vector2ic nodePos : this.grid.getDefinition().getNodes().keySet()) {
                    int x = this.leftPos + 148 + (12 * nodePos.x());
                    int y = this.topPos + 87 + (12 * nodePos.y());
                    NodeButton button = new NodeButton(this, nodePos.x(), nodePos.y(), x, y, this.grid.isUnlockable(nodePos));
                    button.active = !this.grid.getUnlocked().contains(nodePos);
                    this.addRenderableWidget(button);
                }
            }
        } else {
            // Render missing writing materials text
            Component text = Component.translatable("label.primalmagick.scribe_table.missing_book");
            int width = this.minecraft.font.width(text.getString());
            pGuiGraphics.drawString(this.minecraft.font, text, this.leftPos + 34 + ((162 - width) / 2), this.topPos + 7 + ((128 - this.minecraft.font.lineHeight) / 2), Color.BLACK.getRGB(), false);
        }
    }
    
    protected static class NodeButton extends ImageButton {
        protected static final WidgetSprites BUTTON_SPRITES = new WidgetSprites(PrimalMagick.resource("scribe_table/grid_node_button"), PrimalMagick.resource("scribe_table/grid_node_button_disabled"), PrimalMagick.resource("scribe_table/grid_node_highlighted"), PrimalMagick.resource("scribe_table/grid_node_button_disabled_highlighted"));
        protected static final ResourceLocation PLACEHOLDER = PrimalMagick.resource("scribe_table/grid_node_placeholder");
        
        protected final int xIndex;
        protected final int yIndex;
        protected final GridDefinition gridDef;
        protected final boolean reachable;
        
        public NodeButton(ScribeGainComprehensionScreen screen, int xIndex, int yIndex, int leftPos, int topPos, boolean reachable) {
            super(leftPos, topPos, 12, 12, BUTTON_SPRITES, button -> {
                // Unlock node via screen's player grid
                screen.grid.unlock(xIndex, yIndex);
            });
            this.gridDef = screen.grid.getDefinition();
            this.xIndex = xIndex;
            this.yIndex = yIndex;
            this.reachable = reachable;
        }

        @Override
        public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            // Configure tooltip
            this.gridDef.getNode(this.xIndex, this.yIndex).ifPresentOrElse(node -> {
                Component rewardText = node.getReward() == null ? Component.literal("NULL REWARD") : node.getReward().getDescription();
                if (this.isActive()) {
                    List<Component> lines = new ArrayList<>();
                    lines.add(Component.translatable("tooltip.primalmagick.scribe_table.grid.reward", rewardText));
                    lines.add(CommonComponents.EMPTY);
                    lines.add(Component.translatable("tooltip.primalmagick.scribe_table.cost", node.getVocabularyCost(), this.gridDef.getLanguage().getName()).withStyle(ChatFormatting.RED));
                    this.setTooltip(Tooltip.create(CommonComponents.joinLines(lines)));
                } else {
                    this.setTooltip(Tooltip.create(Component.translatable("tooltip.primalmagick.scribe_table.grid.reward.unlocked", rewardText)));
                }
            }, () -> {
                this.setTooltip(null);
            });

            // Scale down to 50% size for rendering
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().scale(0.5F, 0.5F, 1F);
            ResourceLocation resourcelocation = this.reachable ? this.sprites.get(this.isActive(), this.isHoveredOrFocused()) : PLACEHOLDER;
            pGuiGraphics.blitSprite(resourcelocation, this.getX(), this.getY(), this.width * 2, this.height * 2);
            pGuiGraphics.pose().popPose();
        }
    }
}
