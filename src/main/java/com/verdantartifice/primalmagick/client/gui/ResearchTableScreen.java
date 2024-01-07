package com.verdantartifice.primalmagick.client.gui;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.gui.widgets.InactiveWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.research_table.AidListWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.research_table.AidUnlockWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.research_table.KnowledgeTotalWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.research_table.OtherRewardWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.research_table.ProjectMaterialSelectionCheckbox;
import com.verdantartifice.primalmagick.client.gui.widgets.research_table.ProjectMaterialWidgetFactory;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.menus.ResearchTableMenu;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.theorycrafting.CompleteProjectPacket;
import com.verdantartifice.primalmagick.common.network.packets.theorycrafting.SetProjectMaterialSelectionPacket;
import com.verdantartifice.primalmagick.common.network.packets.theorycrafting.StartProjectPacket;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.theorycrafting.AbstractProjectMaterial;
import com.verdantartifice.primalmagick.common.theorycrafting.Project;
import com.verdantartifice.primalmagick.common.theorycrafting.TheorycraftManager;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;

/**
 * GUI screen for the research table block.
 * 
 * @author Daedalus4096
 */
public class ResearchTableScreen extends AbstractContainerScreen<ResearchTableMenu> {
    private static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/research_table.png");
    private static final ResourceLocation OVERLAY = PrimalMagick.resource("textures/gui/research_table_overlay.png");
    private static final DecimalFormat FORMATTER = new DecimalFormat("###.#");
    
    protected long lastCheck = 0L;
    protected boolean progressing = false;
    protected boolean writingReady = false;
    protected boolean lastWritingReady = false;
    protected IPlayerKnowledge knowledge;
    protected Project project = null;
    protected Project lastProject = null;
    protected Button completeProjectButton = null;

    public ResearchTableScreen(ResearchTableMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
        this.imageWidth = 230;
        this.imageHeight = 222;
    }
    
    @Override
    protected void init() {
        super.init();
        Minecraft mc = this.getMinecraft();
        this.knowledge = PrimalMagickCapabilities.getKnowledge(mc.player).orElseThrow(() -> new IllegalStateException("No knowledge provider found for player"));
        this.project = this.knowledge.getActiveResearchProject();
        this.lastWritingReady = this.writingReady = this.menu.isWritingReady();
        this.initButtons();
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // Determine if we need to update the GUI based on how long it's been since the last refresh, or writing tool availability
        long millis = System.currentTimeMillis();
        this.lastWritingReady = this.writingReady;
        this.writingReady = this.menu.isWritingReady();
        if (millis > this.lastCheck || this.lastWritingReady != this.writingReady) {
            // Update more frequently if waiting for the server to process a progression
            this.lastCheck = this.progressing ? (millis + 250L) : (millis + 2000L);
            this.lastProject = this.project;
            this.project = this.knowledge.getActiveResearchProject();
            if (this.lastProject != this.project) {
                this.progressing = false;
            }
            this.initButtons();
        }

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
    
    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Minecraft mc = Minecraft.getInstance();
        
        if (this.isProjectReady()) {
            int y = 11;
            
            // Render title text
            Component titleText = Component.translatable(this.project.getNameTranslationKey()).withStyle(ChatFormatting.BOLD);
            int titleWidth = mc.font.width(titleText);
            guiGraphics.drawString(mc.font, titleText, 34 + ((162 - titleWidth) / 2), y, Color.BLACK.getRGB(), false);
            y += (int)(mc.font.lineHeight * 1.66D);
            
            // Render description text
            Component descText = Component.translatable(this.project.getTextTranslationKey());
            List<FormattedText> descLines = mc.font.getSplitter().splitLines(descText, 154, Style.EMPTY); // list formatted string to width
            for (FormattedText line : descLines) {
                guiGraphics.drawString(mc.font, line.getString(), 38, y, Color.BLACK.getRGB(), false);
                y += mc.font.lineHeight;
            }
        } else if (!this.menu.isWritingReady()) {
            // Render missing writing materials text
            Component text = Component.translatable("label.primalmagick.research_table.missing_writing_supplies");
            int width = mc.font.width(text.getString());
            guiGraphics.drawString(mc.font, text, 34 + ((162 - width) / 2), 7 + ((128 - mc.font.lineHeight) / 2), Color.BLACK.getRGB(), false);
        } else {
            // Render ready to start text
            Component text = Component.translatable("label.primalmagick.research_table.ready");
            int width = mc.font.width(text.getString());
            guiGraphics.drawString(mc.font, text, 34 + ((162 - width) / 2), 7 + ((128 - mc.font.lineHeight) / 2), Color.BLACK.getRGB(), false);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Render the GUI background
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        
        // If a research project is ready to go, render the page overlay
        if (this.isProjectReady()) {
            guiGraphics.blit(OVERLAY, this.leftPos + 34, this.topPos + 7, 0, 0, 162, 128);
        }
    }
    
    protected boolean isProjectReady() {
        return this.project != null && this.menu.isWritingReady();
    }
    
    public void setProgressing() {
        this.progressing = true;
        this.lastCheck = 0L;
    }
    
    public void setMaterialSelection(int index, boolean selected) {
        if (this.project != null && index >= 0 && index < this.project.getMaterials().size()) {
            // Update selection status on client and server side
            this.project.getMaterials().get(index).setSelected(selected);
            PacketHandler.sendToServer(new SetProjectMaterialSelectionPacket(index, selected));
            
            // Trigger a button refresh to recalculate success chance
            this.lastCheck = 0L;
        }
    }
    
    protected void initButtons() {
        this.clearWidgets();
        this.completeProjectButton = null;
        
        if (this.project == null && this.menu.isWritingReady()) {
            // Render theory progress widget
            this.addRenderableWidget(new KnowledgeTotalWidget(this.leftPos + 203, this.topPos + 116, KnowledgeType.THEORY));
            
            if (this.progressing) {
                // Render starting widget
                Component text = Component.translatable("label.primalmagick.research_table.starting");
                this.addRenderableWidget(new InactiveWidget(this.leftPos + 38, this.topPos + 111, 154, 20, text));
            } else {
                // Render start project button
                Component text = Component.translatable("label.primalmagick.research_table.start");
                this.addRenderableWidget(new StartProjectButton(this.leftPos + 38, this.topPos + 111, text, this));
            }
        } else if (this.isProjectReady()) {
            // Render theory progress widget with gain preview
            this.addRenderableWidget(new KnowledgeTotalWidget(this.leftPos + 203, this.topPos + 116, KnowledgeType.THEORY, OptionalInt.of(this.project.getTheoryPointReward())));
            
            // Render reward widget if non-theory rewards are present for this project
            if (!this.project.getOtherRewards().isEmpty()) {
                this.addRenderableWidget(new OtherRewardWidget(this.project.getOtherRewards(), this.leftPos + 203, this.topPos + 97));
            }
            
            if (this.progressing) {
                // Render completing widget
                Component text = Component.translatable("label.primalmagick.research_table.completing");
                this.addRenderableWidget(new InactiveWidget(this.leftPos + 38, this.topPos + 111, 154, 20, text));
            } else {
                this.menu.getContainerLevelAccess().execute((level, tablePos) -> {
                    // Render complete project button
                    Player player = this.minecraft.player;
                    double chance = 100.0D * this.project.getSuccessChance();
                    Set<Block> surroundings = TheorycraftManager.getSurroundings(level, tablePos);
                    Component text = Component.translatable("label.primalmagick.research_table.complete", FORMATTER.format(chance));
                    
                    this.completeProjectButton = this.addRenderableWidget(new CompleteProjectButton(this.leftPos + 38, this.topPos + 111, text, this));
                    this.completeProjectButton.active = this.project.isSatisfied(player, surroundings);
                    
                    // Render aid list widget, if applicable
                    List<Component> aidNames = this.menu.getNearbyAidBlockNames();
                    if (!aidNames.isEmpty()) {
                        this.addRenderableWidget(new AidListWidget(this.leftPos + 36, this.topPos + 9, aidNames));
                    }
                    
                    // Render unlock widget, if applicable
                    if (this.project.getAidBlock() != null) {
                        this.addRenderableWidget(new AidUnlockWidget(this.leftPos + 186, this.topPos + 9, this.project.getAidBlock()));
                    }
                    
                    // Render material widgets
                    int materialCount = this.project.getMaterials().size();
                    int startX = (152 - (38 * materialCount)) / 2;
                    for (int index = 0, x = startX; index < materialCount; index++, x += 38) {
                        // Render material checkbox
                        AbstractProjectMaterial material = this.project.getMaterials().get(index);
                        this.addRenderableWidget(new ProjectMaterialSelectionCheckbox(this.leftPos + 42 + x, this.topPos + 93, this, material.isSelected(), index));
                    }
                    for (int index = 0, x = startX; index < materialCount; index++, x += 38) {
                        // Render material widget
                        AbstractProjectMaterial material = this.project.getMaterials().get(index);
                        this.addRenderableWidget(ProjectMaterialWidgetFactory.create(material, this.leftPos + 58 + x, this.topPos + 93, surroundings));
                    }
                });
            }
        }
        
        // Render observation progress widget
        this.addRenderableWidget(new KnowledgeTotalWidget(this.leftPos + 11, this.topPos + 116, KnowledgeType.OBSERVATION));
    }
    
    /**
     * GUI button to start a new research project in the research table.
     * 
     * @author Daedalus4096
     */
    protected static class StartProjectButton extends Button {
        protected ResearchTableScreen screen;
        
        public StartProjectButton(int xIn, int yIn, Component text, ResearchTableScreen screen) {
            super(Button.builder(text, new Handler()).bounds(xIn, yIn, 154, 20));
            this.screen = screen;
        }
        
        public ResearchTableScreen getScreen() {
            return this.screen;
        }
        
        private static class Handler implements OnPress {
            @Override
            public void onPress(Button button) {
                if (button instanceof StartProjectButton) {
                    // Send a packet to the server and tell the screen to update more frequently until resolved
                    StartProjectButton spb = (StartProjectButton)button;
                    PacketHandler.sendToServer(new StartProjectPacket(spb.getScreen().menu.containerId));
                    spb.getScreen().setProgressing();
                }
            }
        }
    }
    
    /**
     * GUI button to complete a research project in the research table.
     * 
     * @author Daedalus4096
     */
    protected static class CompleteProjectButton extends Button {
        protected ResearchTableScreen screen;
        
        public CompleteProjectButton(int xIn, int yIn, Component text, ResearchTableScreen screen) {
            super(Button.builder(text, new Handler()).bounds(xIn, yIn, 154, 20));
            this.screen = screen;
        }
        
        public ResearchTableScreen getScreen() {
            return this.screen;
        }
        
        private static class Handler implements OnPress {
            @Override
            public void onPress(Button button) {
                if (button instanceof CompleteProjectButton) {
                    // Send a packet to the server and tell the screen to update more frequently until resolved
                    CompleteProjectButton cpb = (CompleteProjectButton)button;
                    PacketHandler.sendToServer(new CompleteProjectPacket(cpb.getScreen().menu.containerId));
                    cpb.getScreen().setProgressing();
                }
            }
        }
    }
}
