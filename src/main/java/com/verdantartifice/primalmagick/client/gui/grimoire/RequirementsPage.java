package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.awt.Color;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.InactiveWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ItemStackWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ItemTagWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.KnowledgeWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ProgressButton;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ResearchWidget;
import com.verdantartifice.primalmagick.common.research.Knowledge;
import com.verdantartifice.primalmagick.common.research.ResearchStage;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

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
        
        // Init obtain requirement widgets
        if (!this.stage.getMustObtain().isEmpty()) {
            y += mc.font.lineHeight;   // Make room for section header
            List<Boolean> completion = this.stage.getObtainRequirementCompletion(mc.player);
            for (int index = 0; index < this.stage.getMustObtain().size(); index++) {
                if (index > 0 && index % ITEMS_PER_ROW == 0) {
                    // If the number of items exceeds the capacity of the row, wrap to the next one
                    x = startX;
                    y += 18;
                }
                Object obj = this.stage.getMustObtain().get(index);
                if (obj instanceof ItemStack) {
                    // Render item stack
                    screen.addWidgetToScreen(new ItemStackWidget((ItemStack)obj, x + 8 + (side * 144), y, completion.get(index).booleanValue()));
                } else if (obj instanceof ResourceLocation) {
                    // Render cycling stacks from tag
                    screen.addWidgetToScreen(new ItemTagWidget((ResourceLocation)obj, x + 8 + (side * 144), y, completion.get(index).booleanValue()));
                }
                x += 18;
            }
            x = startX;
            y += 18;
        }
        
        // Init craft requirement widgets
        if (!this.stage.getMustCraft().isEmpty()) {
            y += mc.font.lineHeight;   // Make room for section header
            List<Boolean> completion = this.stage.getCraftRequirementCompletion(mc.player);
            for (int index = 0; index < this.stage.getMustCraft().size(); index++) {
                if (index > 0 && index % ITEMS_PER_ROW == 0) {
                    // If the number of items exceeds the capacity of the row, wrap to the next one
                    x = startX;
                    y += 18;
                }
                Object obj = this.stage.getMustCraft().get(index);
                if (obj instanceof ItemStack) {
                    // Render item stack
                    screen.addWidgetToScreen(new ItemStackWidget((ItemStack)obj, x + 8 + (side * 144), y, completion.get(index).booleanValue()));
                } else if (obj instanceof ResourceLocation) {
                    // Render cycling stacks from tag
                    screen.addWidgetToScreen(new ItemTagWidget((ResourceLocation)obj, x + 8 + (side * 144), y, completion.get(index).booleanValue()));
                }
                x += 18;
            }
            x = startX;
            y += 18;
        }
        
        // Init knowledge requirement widgets
        if (!this.stage.getRequiredKnowledge().isEmpty()) {
            y += mc.font.lineHeight;   // Make room for section header
            List<Boolean> completion = this.stage.getKnowledgeRequirementCompletion(mc.player);
            for (int index = 0; index < this.stage.getRequiredKnowledge().size(); index++) {
                if (index > 0 && index % ITEMS_PER_ROW == 0) {
                    // If the number of items exceeds the capacity of the row, wrap to the next one
                    x = startX;
                    y += 18;
                }
                Knowledge know = this.stage.getRequiredKnowledge().get(index);
                screen.addWidgetToScreen(new KnowledgeWidget(know, x + 8 + (side * 144), y, completion.get(index).booleanValue()));
                x += 18;
            }
            x = startX;
            y += 18;
        }
        
        // Init research requirement widgets
        if (!this.stage.getRequiredResearch().isEmpty()) {
            y += mc.font.lineHeight;   // Make room for section header
            List<Boolean> completion = this.stage.getResearchRequirementCompletion(mc.player);
            for (int index = 0; index < this.stage.getRequiredResearch().getKeys().size(); index++) {
                if (index > 0 && index % ITEMS_PER_ROW == 0) {
                    // If the number of items exceeds the capacity of the row, wrap to the next one
                    x = startX;
                    y += 18;
                }
                SimpleResearchKey key = this.stage.getRequiredResearch().getKeys().get(index);
                screen.addWidgetToScreen(new ResearchWidget(key, x + 8 + (side * 144), y, completion.get(index).booleanValue(), this.stage.getHints().contains(key)));
                x += 18;
            }
            x = startX;
            y += 18;
        }
        
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
        
        // Render obtain requirement section
        if (!this.stage.getMustObtain().isEmpty()) {
            Component leadComponent = Component.translatable("grimoire.primalmagick.must_obtain_header").withStyle(ChatFormatting.UNDERLINE);
            guiGraphics.drawString(mc.font, leadComponent, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB(), false);
            y += mc.font.lineHeight;
            y += (18 * (1 + (this.stage.getMustObtain().size() / ITEMS_PER_ROW)));  // Make room for obtain widgets
        }
        
        // Render craft requirement section
        if (!this.stage.getMustCraft().isEmpty()) {
            Component leadComponent = Component.translatable("grimoire.primalmagick.must_craft_header").withStyle(ChatFormatting.UNDERLINE);
            guiGraphics.drawString(mc.font, leadComponent, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB(), false);
            y += mc.font.lineHeight;
            y += (18 * (1 + (this.stage.getMustCraft().size() / ITEMS_PER_ROW)));   // Make room for craft widgets
        }
        
        // Render knowledge requirement section
        if (!this.stage.getRequiredKnowledge().isEmpty()) {
            Component leadComponent = Component.translatable("grimoire.primalmagick.required_knowledge_header").withStyle(ChatFormatting.UNDERLINE);
            guiGraphics.drawString(mc.font, leadComponent, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB(), false);
            y += mc.font.lineHeight;
            y += (18 * (1 + (this.stage.getRequiredKnowledge().size() / ITEMS_PER_ROW)));   // Make room for knowledge widgets
        }
        
        // Render research requirement section
        if (!this.stage.getRequiredResearch().isEmpty()) {
            Component leadComponent = Component.translatable("grimoire.primalmagick.required_research_header").withStyle(ChatFormatting.UNDERLINE);
            guiGraphics.drawString(mc.font, leadComponent, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB(), false);
            y += mc.font.lineHeight;
            y += (18 * (1 + (this.stage.getRequiredResearch().getKeys().size() / ITEMS_PER_ROW)));  // Make room for research widgets
        }
        
        guiGraphics.pose().popPose();
    }
}
