package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.awt.Color;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.InactiveWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ProgressButton;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RequirementWidgetFactory;
import com.verdantartifice.primalmagick.common.research.ResearchStage;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

/**
 * Grimoire page showing the requirements needed to advance a research entry to its next stage.
 * 
 * @author Daedalus4096
 */
public class RequirementsPage extends AbstractPage {
    protected static final int ITEMS_PER_ROW = 7;
    
    protected ResearchStage stage;
    
    public RequirementsPage(ResearchStage stage) {
        this.stage = stage;
    }
    
    @Override
    protected boolean renderTopTitleBar() {
        return false;
    }

    @Override
    protected Component getTitleText() {
        return Component.translatable("grimoire.primalmagick.requirements_header");
    }
    
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        int startX = x;
        int startY = y;
        y += 27;    // Make room for page title
        
        Minecraft mc = Minecraft.getInstance();
        List<AbstractRequirement<?>> reqs;
        
        // TODO Abstract these blocks into a shared function after figuring out how to deal with hints
        // Init obtain requirement widgets
        reqs = this.stage.getRequirementsByCategory(RequirementCategory.MUST_OBTAIN);
        if (!reqs.isEmpty()) {
            y += mc.font.lineHeight;   // Make room for section header
            List<Boolean> completion = this.stage.getRequirementCompletionByCategory(mc.player, RequirementCategory.MUST_OBTAIN);
            for (int index = 0; index < reqs.size(); index++) {
                if (index > 0 && index % ITEMS_PER_ROW == 0) {
                    // If the number of items exceeds the capacity of the row, wrap to the next one
                    x = startX;
                    y += 18;
                }
                AbstractWidget widget = RequirementWidgetFactory.fromRequirement(reqs.get(index), x + 8 + (side * 144), y, completion.get(index), this.stage);
                if (widget != null) {
                    screen.addWidgetToScreen(widget);
                }
                x += 18;
            }
            x = startX;
            y += 18;
        }
        
        // Init craft requirement widgets
        reqs = this.stage.getRequirementsByCategory(RequirementCategory.MUST_CRAFT);
        if (!reqs.isEmpty()) {
            y += mc.font.lineHeight;   // Make room for section header
            List<Boolean> completion = this.stage.getRequirementCompletionByCategory(mc.player, RequirementCategory.MUST_CRAFT);
            for (int index = 0; index < reqs.size(); index++) {
                if (index > 0 && index % ITEMS_PER_ROW == 0) {
                    // If the number of items exceeds the capacity of the row, wrap to the next one
                    x = startX;
                    y += 18;
                }
                AbstractWidget widget = RequirementWidgetFactory.fromRequirement(reqs.get(index), x + 8 + (side * 144), y, completion.get(index), this.stage);
                if (widget != null) {
                    screen.addWidgetToScreen(widget);
                }
                x += 18;
            }
            x = startX;
            y += 18;
        }
        
        // Init knowledge requirement widgets
        reqs = this.stage.getRequirementsByCategory(RequirementCategory.KNOWLEDGE);
        if (!reqs.isEmpty()) {
            y += mc.font.lineHeight;   // Make room for section header
            List<Boolean> completion = this.stage.getRequirementCompletionByCategory(mc.player, RequirementCategory.KNOWLEDGE);
            for (int index = 0; index < reqs.size(); index++) {
                if (index > 0 && index % ITEMS_PER_ROW == 0) {
                    // If the number of items exceeds the capacity of the row, wrap to the next one
                    x = startX;
                    y += 18;
                }
                AbstractWidget widget = RequirementWidgetFactory.fromRequirement(reqs.get(index), x + 8 + (side * 144), y, completion.get(index), this.stage);
                if (widget != null) {
                    screen.addWidgetToScreen(widget);
                }
                x += 18;
            }
            x = startX;
            y += 18;
        }
        
        // Init research requirement widgets
        reqs = this.stage.getRequirementsByCategory(RequirementCategory.RESEARCH);
        if (!reqs.isEmpty()) {
            y += mc.font.lineHeight;   // Make room for section header
            List<Boolean> completion = this.stage.getRequirementCompletionByCategory(mc.player, RequirementCategory.RESEARCH);
            for (int index = 0; index < reqs.size(); index++) {
                if (index > 0 && index % ITEMS_PER_ROW == 0) {
                    // If the number of items exceeds the capacity of the row, wrap to the next one
                    x = startX;
                    y += 18;
                }
                AbstractWidget widget = RequirementWidgetFactory.fromRequirement(reqs.get(index), x + 8 + (side * 144), y, completion.get(index), this.stage);
                if (widget != null) {
                    screen.addWidgetToScreen(widget);
                }
                x += 18;
            }
            x = startX;
            y += 18;
        }
        
        // TODO Init stat research widgets
        
        // Init progress button if applicable
        y = startY + 141;
        if (screen.isProgressing()) {
            Component text = Component.translatable("grimoire.primalmagick.completing_text");
            screen.addWidgetToScreen(new InactiveWidget(startX + 16 + (side * 136), y, 119, 20, text));
        } else if (this.stage.arePrerequisitesMet(mc.player)) {
            Component text = Component.translatable("grimoire.primalmagick.complete_button");
            screen.addWidgetToScreen(new ProgressButton(this.stage, startX + 16 + (side * 136), y, text, screen));
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        // Render page title
        this.renderTitle(guiGraphics, side, x, y, mouseX, mouseY, null);
        y += 53;
        
        guiGraphics.pose().pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        guiGraphics.pose().translate(0.0F, 0.0F, 1.0F);  // Bump up slightly in the Z-order to prevent the underline from being swallowed
        Minecraft mc = Minecraft.getInstance();
        List<AbstractRequirement<?>> reqs;
        
        // TODO Abstract these blocks into a shared function
        // Render obtain requirement section
        reqs = this.stage.getRequirementsByCategory(RequirementCategory.MUST_OBTAIN);
        if (!reqs.isEmpty()) {
            Component leadComponent = Component.translatable("grimoire.primalmagick.must_obtain_header").withStyle(ChatFormatting.UNDERLINE);
            guiGraphics.drawString(mc.font, leadComponent, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB(), false);
            y += mc.font.lineHeight;
            y += (18 * (1 + (reqs.size() / ITEMS_PER_ROW)));  // Make room for obtain widgets
        }
        
        // Render craft requirement section
        reqs = this.stage.getRequirementsByCategory(RequirementCategory.MUST_CRAFT);
        if (!reqs.isEmpty()) {
            Component leadComponent = Component.translatable("grimoire.primalmagick.must_craft_header").withStyle(ChatFormatting.UNDERLINE);
            guiGraphics.drawString(mc.font, leadComponent, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB(), false);
            y += mc.font.lineHeight;
            y += (18 * (1 + (reqs.size() / ITEMS_PER_ROW)));   // Make room for craft widgets
        }
        
        // Render knowledge requirement section
        reqs = this.stage.getRequirementsByCategory(RequirementCategory.KNOWLEDGE);
        if (!reqs.isEmpty()) {
            Component leadComponent = Component.translatable("grimoire.primalmagick.required_knowledge_header").withStyle(ChatFormatting.UNDERLINE);
            guiGraphics.drawString(mc.font, leadComponent, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB(), false);
            y += mc.font.lineHeight;
            y += (18 * (1 + (reqs.size() / ITEMS_PER_ROW)));   // Make room for knowledge widgets
        }
        
        // Render research requirement section
        reqs = this.stage.getRequirementsByCategory(RequirementCategory.RESEARCH);
        if (!reqs.isEmpty()) {
            Component leadComponent = Component.translatable("grimoire.primalmagick.required_research_header").withStyle(ChatFormatting.UNDERLINE);
            guiGraphics.drawString(mc.font, leadComponent, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB(), false);
            y += mc.font.lineHeight;
            y += (18 * (1 + (reqs.size() / ITEMS_PER_ROW)));  // Make room for research widgets
        }
        
        // TODO Render stat requirement section
        
        guiGraphics.pose().popPose();
    }
}
