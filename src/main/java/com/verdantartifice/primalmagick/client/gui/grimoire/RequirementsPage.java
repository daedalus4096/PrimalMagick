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
    
    protected int initWidgetCategory(GrimoireScreen screen, RequirementCategory category, int x, int y) {
        int startX = x;
        int widgetHeight = Integer.MIN_VALUE;
        Minecraft mc = Minecraft.getInstance();

        List<AbstractRequirement<?>> reqs = this.stage.getRequirementsByCategory(category);
        if (!reqs.isEmpty()) {
            y += mc.font.lineHeight;   // Make room for section header
            List<Boolean> completion = this.stage.getRequirementCompletionByCategory(mc.player, category);
            for (int index = 0; index < reqs.size(); index++) {
                if (index > 0 && index % ITEMS_PER_ROW == 0 && widgetHeight > 0) {
                    // If the number of items exceeds the capacity of the row, wrap to the next one
                    x = startX;
                    y += (widgetHeight + 2);
                }
                AbstractWidget widget = RequirementWidgetFactory.fromRequirement(reqs.get(index), x, y, completion.get(index));
                if (widget != null) {
                    screen.addWidgetToScreen(widget);
                    widgetHeight = Math.max(widgetHeight, widget.getHeight());
                }
                x += 18;
            }
            if (widgetHeight > 0) {
                y += (widgetHeight + 2);
            }
        }
        
        return y;
    }
    
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        int startX = x;
        int startY = y;
        y += 27;    // Make room for page title
        
        Minecraft mc = Minecraft.getInstance();
        
        int widgetX = x + 8 + (side * 144);
        y = this.initWidgetCategory(screen, RequirementCategory.MUST_OBTAIN, widgetX, y);   // Init obtain requirement widgets
        y = this.initWidgetCategory(screen, RequirementCategory.MUST_CRAFT, widgetX, y);    // Init craft requirement widgets
        y = this.initWidgetCategory(screen, RequirementCategory.KNOWLEDGE, widgetX, y);     // Init knowledge requirement widgets
        y = this.initWidgetCategory(screen, RequirementCategory.RESEARCH, widgetX, y);      // Init research requirement widgets
        y = this.initWidgetCategory(screen, RequirementCategory.STAT, widgetX, y);          // Init stat research widgets
        
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
    
    public int renderRequirementsLabel(GuiGraphics guiGraphics, RequirementCategory category, String textTranslationKey, int x, int y, int widgetHeight) {
        Minecraft mc = Minecraft.getInstance();
        List<AbstractRequirement<?>> reqs = this.stage.getRequirementsByCategory(category);
        if (!reqs.isEmpty()) {
            Component leadComponent = Component.translatable(textTranslationKey).withStyle(ChatFormatting.UNDERLINE);
            guiGraphics.drawString(mc.font, leadComponent, x, y - 6, Color.BLACK.getRGB(), false);
            y += mc.font.lineHeight;
            y += ((widgetHeight + 2) * (1 + (reqs.size() / ITEMS_PER_ROW)));  // Make room for widgets
        }
        return y;
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
        
        int labelX = x - 3 + (side * 140);
        y = this.renderRequirementsLabel(guiGraphics, RequirementCategory.MUST_OBTAIN, "grimoire.primalmagick.must_obtain_header", labelX, y, 16);      // Render obtain requirement section
        y = this.renderRequirementsLabel(guiGraphics, RequirementCategory.MUST_CRAFT, "grimoire.primalmagick.must_craft_header", labelX, y, 16);        // Render craft requirement section
        y = this.renderRequirementsLabel(guiGraphics, RequirementCategory.KNOWLEDGE, "grimoire.primalmagick.required_knowledge_header", labelX, y, 16); // Render knowledge requirement section
        y = this.renderRequirementsLabel(guiGraphics, RequirementCategory.RESEARCH, "grimoire.primalmagick.required_research_header", labelX, y, 16);   // Render research requirement section
        y = this.renderRequirementsLabel(guiGraphics, RequirementCategory.STAT, "grimoire.primalmagick.required_stats_header", labelX, y, 18);          // Render stat requirement section
        
        guiGraphics.pose().popPose();
    }
}
