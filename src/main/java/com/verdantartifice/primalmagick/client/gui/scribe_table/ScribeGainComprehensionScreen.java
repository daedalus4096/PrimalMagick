package com.verdantartifice.primalmagick.client.gui.scribe_table;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2i;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.gui.widgets.VocabularyWidget;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.books.ScribeTableMode;
import com.verdantartifice.primalmagick.common.books.grids.GridDefinition;
import com.verdantartifice.primalmagick.common.books.grids.GridNodeDefinition;
import com.verdantartifice.primalmagick.common.books.grids.PlayerGrid;
import com.verdantartifice.primalmagick.common.menus.ScribeGainComprehensionMenu;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.tags.BookLanguageTagsPM;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

/**
 * GUI screen for the gain comprehension mode of the scribe table block.
 * 
 * @author Daedalus4096
 */
public class ScribeGainComprehensionScreen extends AbstractScribeTableScreen<ScribeGainComprehensionMenu> {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/scribe_gain_comprehension.png");
    protected static final ResourceLocation PARCHMENT_SPRITE = PrimalMagick.resource("scribe_table/parchment");
    protected static final Logger LOGGER = LogManager.getLogger();
    
    protected final Map<Vector2i, NodeButton> nodeButtons = new HashMap<>();
    protected VocabularyWidget vocabularyWidget;
    protected PlayerGrid grid;
    protected long nextCheckTime = 0L;

    public ScribeGainComprehensionScreen(ScribeGainComprehensionMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 222;
        this.inventoryLabelY = this.imageHeight - 94;
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
    protected void init() {
        super.init();
        Holder.Reference<BookLanguage> lang = this.menu.getBookLanguage();
        this.vocabularyWidget = this.addRenderableWidget(new VocabularyWidget(lang, this.menu.getVocabularyCount(), this.leftPos + 151, this.topPos + 17));
        this.grid = LinguisticsManager.getPlayerGrid(this.minecraft.player, lang.key().location());
        
        // Initialize the node buttons for the grid
        for (int y = GridDefinition.MIN_POS; y <= GridDefinition.MAX_POS; y++) {
            for (int x = GridDefinition.MIN_POS; x <= GridDefinition.MAX_POS; x++) {
                Vector2i nodePos = new Vector2i(x, y);
                NodeButton button = new NodeButton(this, x, y, this.leftPos + 40 + (12 * x), this.topPos + 23 + (12 * y));
                button.visible = false;
                this.nodeButtons.put(nodePos, button);
                this.addRenderableWidget(button);
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);

        // Update the vocabulary widget based on the current language in the menu
        Holder.Reference<BookLanguage> lang = this.menu.getBookLanguage();
        this.vocabularyWidget.visible = lang.is(BookLanguageTagsPM.ANCIENT);
        this.vocabularyWidget.setLanguage(lang);
        this.vocabularyWidget.setAmount(this.menu.getVocabularyCount());
        
        // Update the local player grid if the current language has changed
        boolean timeUp = (System.currentTimeMillis() > this.nextCheckTime);
        if (this.grid == null || !lang.key().equals(this.grid.getDefinition().getLanguage()) || timeUp) {
            this.refreshGrid();
            this.nextCheckTime = System.currentTimeMillis() + 250L;
        }

        if (lang.get().isComplex()) {
            // Draw the parchment background for the comprehension grid
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(this.leftPos + 31, this.topPos + 17, 0);
            pGuiGraphics.pose().scale(0.5F, 0.5F, 1F);
            pGuiGraphics.blitSprite(PARCHMENT_SPRITE, 0, 0, 228, 216);
            pGuiGraphics.pose().popPose();
            
            // Update the node buttons for each node in the grid definition
            if (this.grid != null) {
                this.nodeButtons.entrySet().forEach(entry -> {
                    Vector2i nodePos = entry.getKey();
                    NodeButton button = entry.getValue();
                    if (this.grid.getDefinition().getNodes().keySet().contains(nodePos)) {
                        // If the button's position is part of the grid definition, update the button to reflect the node
                        boolean unlockable = this.grid.isUnlockable(nodePos);
                        boolean isUnlocked = this.grid.getUnlocked().contains(nodePos);
                        button.setGridDefinition(this.grid.getDefinition());
                        button.setReachable(unlockable || isUnlocked);
                        button.setUnlockable(unlockable);
                        button.active = !isUnlocked;
                        button.visible = true;
                    } else {
                        // Otherwise, make the button invisible
                        button.visible = false;
                    }
                });
            } else {
                // If no grid is currently active, hide all node buttons
                this.nodeButtons.entrySet().stream().map(e -> e.getValue()).forEach(b -> b.visible = false);
            }
        } else {
            // Render missing writing materials text
            Component text = Component.translatable("label.primalmagick.scribe_table.missing_book");
            int width = this.minecraft.font.width(text.getString());
            pGuiGraphics.drawString(this.minecraft.font, text, this.leftPos + 7 + ((162 - width) / 2), this.topPos + 7 + ((128 - this.minecraft.font.lineHeight) / 2), Color.BLACK.getRGB(), false);

            // If no grid is currently active, hide all node buttons
            this.nodeButtons.entrySet().stream().map(e -> e.getValue()).forEach(b -> b.visible = false);
        }
    }
    
    protected void refreshGrid() {
        Holder.Reference<BookLanguage> lang = this.menu.getBookLanguage();
        PlayerGrid newGrid = LinguisticsManager.getPlayerGrid(this.minecraft.player, lang.key().location());
        if (this.grid == null || newGrid == null || newGrid.getLastModified() > this.grid.getLastModified() || !this.grid.getDefinition().getLanguage().equals(newGrid.getDefinition().getLanguage())) {
            this.grid = newGrid;
        }
    }
    
    protected static class NodeButton extends ImageButton {
        protected static final WidgetSprites BUTTON_SPRITES = new WidgetSprites(PrimalMagick.resource("scribe_table/grid_node_button"), PrimalMagick.resource("scribe_table/grid_node_button_disabled"), PrimalMagick.resource("scribe_table/grid_node_button_highlighted"), PrimalMagick.resource("scribe_table/grid_node_button_disabled_highlighted"));
        protected static final ResourceLocation PLACEHOLDER = PrimalMagick.resource("scribe_table/grid_node_placeholder");
        
        protected final Player player;
        protected final RegistryAccess registryAccess;
        protected final int xIndex;
        protected final int yIndex;
        protected Optional<GridDefinition> gridDef;
        protected boolean reachable;
        protected boolean unlockable;
        protected Component lastTooltip = Component.empty();
        protected Component tooltip = Component.empty();
        
        public NodeButton(ScribeGainComprehensionScreen screen, int xIndex, int yIndex, int leftPos, int topPos) {
            super(leftPos, topPos, 12, 12, BUTTON_SPRITES, button -> {
                // Unlock node via screen's player grid
                ClientLevel level = screen.minecraft.level;
                screen.grid.unlock(xIndex, yIndex, level.registryAccess());
                level.playSound(screen.minecraft.player, screen.menu.getTilePos(), SoundsPM.WRITING.get(), SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.1F + 0.9F);
            });
            this.player = screen.minecraft.player;
            this.registryAccess = screen.minecraft.level.registryAccess();
            this.gridDef = screen.grid == null ? Optional.empty() : Optional.ofNullable(screen.grid.getDefinition());
            this.xIndex = xIndex;
            this.yIndex = yIndex;
            this.reachable = false;
            this.unlockable = false;
        }
        
        public void setReachable(boolean reachable) {
            this.reachable = reachable;
        }
        
        public void setUnlockable(boolean unlockable) {
            this.unlockable = unlockable;
        }
        
        public void setGridDefinition(GridDefinition gridDef) {
            this.gridDef = Optional.ofNullable(gridDef);
        }

        @Override
        public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            // Configure tooltip
            this.lastTooltip = this.tooltip;
            MutableObject<Component> mutableTooltip = new MutableObject<>(Component.empty());
            this.gridDef.ifPresent(def -> {
                def.getNode(this.xIndex, this.yIndex).ifPresent(node -> {
                    Component rewardText = node.getReward().getDescription(this.player, this.registryAccess);
                    if (this.isActive()) {
                        List<Component> lines = new ArrayList<>();
                        lines.add(Component.translatable("tooltip.primalmagick.scribe_table.grid.reward", rewardText));
                        if (!this.player.getAbilities().instabuild) {
                            lines.add(CommonComponents.EMPTY);
                            Holder.Reference<BookLanguage> lang = BookLanguagesPM.getLanguageOrDefault(def.getLanguage(), this.registryAccess, BookLanguagesPM.DEFAULT);
                            MutableComponent costText = Component.translatable("tooltip.primalmagick.scribe_table.grid.cost", node.getVocabularyCost(), lang.get().getName());
                            if (LinguisticsManager.getVocabulary(this.player, lang) < node.getVocabularyCost()) {
                                costText = costText.withStyle(ChatFormatting.RED);
                            }
                            lines.add(costText);
                        }
                        if (!this.reachable) {
                            lines.add(CommonComponents.EMPTY);
                            lines.add(Component.translatable("tooltip.primalmagick.scribe_table.grid.no_path").withStyle(ChatFormatting.RED));
                        }
                        mutableTooltip.setValue(CommonComponents.joinLines(lines));
                    } else {
                        mutableTooltip.setValue(Component.translatable("tooltip.primalmagick.scribe_table.grid.reward.unlocked", rewardText));
                    }
                });
            });
            this.tooltip = mutableTooltip.getValue();
            if (!this.lastTooltip.equals(this.tooltip)) {
                this.setTooltip(Tooltip.create(this.tooltip));
            }

            // Render node button background
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(this.getX(), this.getY(), 0);
            pGuiGraphics.pose().scale(0.5F, 0.5F, 1F);  // Scale down to 50% size for rendering
            ResourceLocation resourcelocation = this.reachable ? this.sprites.get(this.isActive(), this.isHoveredOrFocused()) : PLACEHOLDER;
            pGuiGraphics.blitSprite(resourcelocation, 0, 0, this.width * 2, this.height * 2);
            pGuiGraphics.pose().popPose();

            // Render node contents, if they exist
            this.gridDef.flatMap(d -> d.getNode(this.xIndex, this.yIndex)).ifPresent(node -> {
                // Render the reward icon
                pGuiGraphics.pose().pushPose();
                int dx = this.width / 2;
                int dy = this.height / 2;
                pGuiGraphics.pose().translate(this.getX() + 2 + (dx * 0.75F), this.getY() + 2 + (dy * 0.75F), 5);
                pGuiGraphics.pose().scale(0.5F, 0.5F, 1F);  // Scale down to 50% size for rendering
                if (this.unlockable) {
                    // If the node can currently be unlocked, pulse its scale up and down for extra visibility
                    float scale = 1F + (0.1F * Mth.sin((this.player.tickCount + pPartialTick) / 3F));
                    pGuiGraphics.pose().scale(scale, scale, 1F);
                }
                pGuiGraphics.blit(node.getReward().getIconLocation(this.player), (int)(-dx * 1.5D), (int)(-dy * 1.5D), 0, 0, 0, 16, 16, 16, 16);
                pGuiGraphics.pose().popPose();
                
                // Render the node amount string, if applicable
                node.getReward().getAmountText().ifPresent(text -> {
                    pGuiGraphics.pose().pushPose();
                    Minecraft mc = Minecraft.getInstance();
                    int width = mc.font.width(text.getString());
                    pGuiGraphics.pose().translate(this.getX() + 11 - width / 2, this.getY() + 7, 10);
                    pGuiGraphics.pose().scale(0.5F, 0.5F, 1F);  // Scale down to 50% size for rendering
                    pGuiGraphics.drawString(mc.font, text, 0, 0, Color.WHITE.getRGB());
                    pGuiGraphics.pose().popPose();
                });
            });
        }

        @Override
        protected boolean clicked(double pMouseX, double pMouseY) {
            boolean retVal = this.reachable && super.clicked(pMouseX, pMouseY);
            if (retVal && this.gridDef.isPresent()) {
                Optional<GridNodeDefinition> nodeOpt = this.gridDef.get().getNode(this.xIndex, this.yIndex);
                Holder.Reference<BookLanguage> lang = BookLanguagesPM.getLanguageOrDefault(this.gridDef.get().getLanguage(), this.registryAccess, BookLanguagesPM.DEFAULT);
                return nodeOpt.isPresent() && (this.player.getAbilities().instabuild || LinguisticsManager.getVocabulary(this.player, lang) >= nodeOpt.get().getVocabularyCost());
            } else {
                return false;
            }
        }
    }
}
