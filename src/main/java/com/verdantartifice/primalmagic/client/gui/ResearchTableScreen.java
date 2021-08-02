package com.verdantartifice.primalmagic.client.gui;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.widgets.research_table.AidUnlockWidget;
import com.verdantartifice.primalmagic.client.gui.widgets.research_table.KnowledgeTotalWidget;
import com.verdantartifice.primalmagic.client.gui.widgets.research_table.ProjectMaterialSelectionCheckbox;
import com.verdantartifice.primalmagic.client.gui.widgets.research_table.ProjectMaterialWidgetFactory;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.containers.ResearchTableContainer;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.theorycrafting.CompleteProjectPacket;
import com.verdantartifice.primalmagic.common.network.packets.theorycrafting.SetProjectMaterialSelectionPacket;
import com.verdantartifice.primalmagic.common.network.packets.theorycrafting.StartProjectPacket;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.Project;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

/**
 * GUI screen for the research table block.
 * 
 * @author Daedalus4096
 */
public class ResearchTableScreen extends AbstractContainerScreen<ResearchTableContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/research_table.png");
    private static final ResourceLocation OVERLAY = new ResourceLocation(PrimalMagic.MODID, "textures/gui/research_table_overlay.png");
    private static final DecimalFormat FORMATTER = new DecimalFormat("###.#");
    
    protected long lastCheck = 0L;
    protected boolean progressing = false;
    protected boolean writingReady = false;
    protected boolean lastWritingReady = false;
    protected IPlayerKnowledge knowledge;
    protected Project project = null;
    protected Project lastProject = null;
    protected Button completeProjectButton = null;

    public ResearchTableScreen(ResearchTableContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 230;
        this.imageHeight = 222;
    }
    
    @Override
    protected void init() {
        super.init();
        Minecraft mc = this.getMinecraft();
        this.knowledge = PrimalMagicCapabilities.getKnowledge(mc.player);
        if (this.knowledge == null) {
            throw new IllegalStateException("No knowledge provider found for player");
        }
        this.project = this.knowledge.getActiveResearchProject();
        this.lastWritingReady = this.writingReady = this.menu.isWritingReady();
        this.initButtons();
    }
    
    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
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

        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
    
    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        Minecraft mc = Minecraft.getInstance();
        
        if (this.isProjectReady()) {
            int y = 11;
            
            // Render title text
            Component titleText = new TranslatableComponent(this.project.getNameTranslationKey()).withStyle(ChatFormatting.BOLD);
            int titleWidth = mc.font.width(titleText);
            mc.font.draw(matrixStack, titleText, 34 + ((162 - titleWidth) / 2), y, Color.BLACK.getRGB());
            y += (int)(mc.font.lineHeight * 1.66D);
            
            // Render description text
            Component descText = new TranslatableComponent(this.project.getTextTranslationKey());
            List<FormattedText> descLines = mc.font.getSplitter().splitLines(descText, 154, Style.EMPTY); // list formatted string to width
            for (FormattedText line : descLines) {
                mc.font.draw(matrixStack, line.getString(), 38, y, Color.BLACK.getRGB());
                y += mc.font.lineHeight;
            }
        } else if (!this.menu.isWritingReady()) {
            // Render missing writing materials text
            Component text = new TranslatableComponent("primalmagic.research_table.missing_writing_supplies");
            int width = mc.font.width(text.getString());
            mc.font.draw(matrixStack, text, 34 + ((162 - width) / 2), 7 + ((128 - mc.font.lineHeight) / 2), Color.BLACK.getRGB());
        } else {
            // Render ready to start text
            Component text = new TranslatableComponent("primalmagic.research_table.ready");
            int width = mc.font.width(text.getString());
            mc.font.draw(matrixStack, text, 34 + ((162 - width) / 2), 7 + ((128 - mc.font.lineHeight) / 2), Color.BLACK.getRGB());
        }
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        // Render the GUI background
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        
        // If a research project is ready to go, render the page overlay
        if (this.isProjectReady()) {
            RenderSystem.setShaderTexture(0, OVERLAY);
            this.blit(matrixStack, this.leftPos + 34, this.topPos + 7, 0, 0, 162, 128);
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
            if (this.progressing) {
                // Render starting widget
                Component text = new TranslatableComponent("primalmagic.research_table.starting");
                this.addRenderableWidget(new WaitingWidget(this.leftPos + 38, this.topPos + 111, text));
            } else {
                // Render start project button
                Component text = new TranslatableComponent("primalmagic.research_table.start");
                this.addRenderableWidget(new StartProjectButton(this.leftPos + 38, this.topPos + 111, text, this));
            }
        } else if (this.isProjectReady()) {
            if (this.progressing) {
                // Render completing widget
                Component text = new TranslatableComponent("primalmagic.research_table.completing");
                this.addRenderableWidget(new WaitingWidget(this.leftPos + 38, this.topPos + 111, text));
            } else {
                // Render complete project button
                Player player = this.minecraft.player;
                double chance = 100.0D * this.project.getSuccessChance();
                Component text = new TranslatableComponent("primalmagic.research_table.complete", FORMATTER.format(chance));
                this.completeProjectButton = this.addRenderableWidget(new CompleteProjectButton(this.leftPos + 38, this.topPos + 111, text, this));
                this.completeProjectButton.active = this.project.isSatisfied(player);
                
                // Render unlock widget, if applicable
                if (this.project.getAidBlock() != null) {
                    this.addRenderableWidget(new AidUnlockWidget(this.leftPos + 186, this.topPos + 9, this.project.getAidBlock()));
                }
                
                // Render material widgets
                int materialCount = this.project.getMaterials().size();
                int x = (152 - (38 * materialCount)) / 2;
                for (int index = 0; index < materialCount; index++) {
                    AbstractProjectMaterial material = this.project.getMaterials().get(index);

                    // Render material checkbox
                    this.addRenderableWidget(new ProjectMaterialSelectionCheckbox(this.leftPos + 42 + x, this.topPos + 93, this, material.isSelected(), index));
                    // Render material widget
                    this.addRenderableWidget(ProjectMaterialWidgetFactory.create(material, this.leftPos + 58 + x, this.topPos + 93));
                    
                    x += 38;
                }
            }
        }
        
        // Render knowledge total widgets
        this.addRenderableWidget(new KnowledgeTotalWidget(this.leftPos + 11, this.topPos + 116, IPlayerKnowledge.KnowledgeType.OBSERVATION));
        this.addRenderableWidget(new KnowledgeTotalWidget(this.leftPos + 203, this.topPos + 116, IPlayerKnowledge.KnowledgeType.THEORY));
    }
    
    /**
     * GUI widget for a disabled text button to use while waiting for the server to update.
     * 
     * @author Daedalus4096
     */
    protected static class WaitingWidget extends AbstractWidget {
        public WaitingWidget(int xIn, int yIn, Component msg) {
            super(xIn, yIn, 154, 20, msg);
            this.active = false;
        }

        @Override
        public void updateNarration(NarrationElementOutput output) {
        }
    }
    
    /**
     * GUI button to start a new research project in the research table.
     * 
     * @author Daedalus4096
     */
    protected static class StartProjectButton extends Button {
        protected ResearchTableScreen screen;
        
        public StartProjectButton(int xIn, int yIn, Component text, ResearchTableScreen screen) {
            super(xIn, yIn, 154, 20, text, new Handler());
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
            super(xIn, yIn, 154, 20, text, new Handler());
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
