package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import java.awt.Color;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.grimoire.widgets.ItemStackWidget;
import com.verdantartifice.primalmagic.client.gui.grimoire.widgets.ItemTagWidget;
import com.verdantartifice.primalmagic.client.gui.grimoire.widgets.KnowledgeWidget;
import com.verdantartifice.primalmagic.client.gui.grimoire.widgets.ResearchWidget;
import com.verdantartifice.primalmagic.common.research.Knowledge;
import com.verdantartifice.primalmagic.common.research.ResearchStage;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RequirementsPage extends AbstractPage {
    protected ResearchStage stage;
    
    public RequirementsPage(ResearchStage stage) {
        this.stage = stage;
    }
    
    @Override
    protected boolean renderTopTitleBar() {
        return false;
    }

    @Override
    protected String getTitleTranslationKey() {
        return "primalmagic.grimoire.requirements_header";
    }
    
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        y += 28;    // Make room for page title
        int startX = x;
        Minecraft mc = Minecraft.getInstance();
        
        // Init obtain requirement widgets
        if (!this.stage.getMustObtain().isEmpty()) {
            y += mc.fontRenderer.FONT_HEIGHT;   // Make room for section header
            List<Boolean> completion = this.stage.getObtainRequirementCompletion(mc.player);
            for (int index = 0; index < this.stage.getMustObtain().size(); index++) {
                Object obj = this.stage.getMustObtain().get(index);
                if (obj instanceof ItemStack) {
                    // Render item stack
                    screen.addWidgetToScreen(new ItemStackWidget((ItemStack)obj, x + (side * 152), y, completion.get(index).booleanValue()));
                } else if (obj instanceof ResourceLocation) {
                    // Render cycling stacks from tag
                    screen.addWidgetToScreen(new ItemTagWidget((ResourceLocation)obj, x + (side * 152), y, completion.get(index).booleanValue()));
                }
                x += 18;
            }
            x = startX;
            y += 24;
        }
        
        // Init craft requirement widgets
        if (!this.stage.getMustCraft().isEmpty()) {
            y += mc.fontRenderer.FONT_HEIGHT;   // Make room for section header
            List<Boolean> completion = this.stage.getCraftRequirementCompletion(mc.player);
            for (int index = 0; index < this.stage.getMustCraft().size(); index++) {
                Object obj = this.stage.getMustCraft().get(index);
                if (obj instanceof ItemStack) {
                    // Render item stack
                    screen.addWidgetToScreen(new ItemStackWidget((ItemStack)obj, x + (side * 152), y, completion.get(index).booleanValue()));
                } else if (obj instanceof ResourceLocation) {
                    // Render cycling stacks from tag
                    screen.addWidgetToScreen(new ItemTagWidget((ResourceLocation)obj, x + (side * 152), y, completion.get(index).booleanValue()));
                }
                x += 18;
            }
            x = startX;
            y += 24;
        }
        
        // Init knowledge requirement widgets
        if (!this.stage.getRequiredKnowledge().isEmpty()) {
            y += mc.fontRenderer.FONT_HEIGHT;   // Make room for section header
            List<Boolean> completion = this.stage.getKnowledgeRequirementCompletion(mc.player);
            for (int index = 0; index < this.stage.getRequiredKnowledge().size(); index++) {
                Knowledge know = this.stage.getRequiredKnowledge().get(index);
                screen.addWidgetToScreen(new KnowledgeWidget(know, x + (side * 152), y, completion.get(index).booleanValue()));
                x += 18;
            }
            x = startX;
            y += 24;
        }
        
        // Init research requirement widgets
        if (this.stage.getRequiredResearch() != null) {
            y += mc.fontRenderer.FONT_HEIGHT;   // Make room for section header
            List<Boolean> completion = this.stage.getResearchRequirementCompletion(mc.player);
            for (int index = 0; index < this.stage.getRequiredResearch().getKeys().size(); index++) {
                SimpleResearchKey key = this.stage.getRequiredResearch().getKeys().get(index);
                screen.addWidgetToScreen(new ResearchWidget(key, x + (side * 152), y, completion.get(index).booleanValue()));
                x += 18;
            }
            x = startX;
            y += 24;
        }
    }

    @Override
    public void render(int side, int x, int y, int mouseX, int mouseY) {
        // Render page title
        this.renderTitle(side, x, y, mouseX, mouseY);
        y += 28;
        
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft mc = Minecraft.getInstance();
        
        // Render obtain requirement section
        if (!this.stage.getMustObtain().isEmpty()) {
            ITextComponent leadComponent = new TranslationTextComponent("primalmagic.grimoire.must_obtain_header").applyTextStyle(TextFormatting.UNDERLINE);
            mc.fontRenderer.drawString(leadComponent.getFormattedText(), x - 15 + (side * 152), y - 6, Color.BLACK.getRGB());
            y += mc.fontRenderer.FONT_HEIGHT;
            y += 24;    // Make room for obtain widgets
        }
        
        // Render craft requirement section
        if (!this.stage.getMustCraft().isEmpty()) {
            ITextComponent leadComponent = new TranslationTextComponent("primalmagic.grimoire.must_craft_header").applyTextStyle(TextFormatting.UNDERLINE);
            mc.fontRenderer.drawString(leadComponent.getFormattedText(), x - 15 + (side * 152), y - 6, Color.BLACK.getRGB());
            y += mc.fontRenderer.FONT_HEIGHT;
            y += 24;    // Make room for craft widgets
        }
        
        // Render knowledge requirement section
        if (!this.stage.getRequiredKnowledge().isEmpty()) {
            ITextComponent leadComponent = new TranslationTextComponent("primalmagic.grimoire.required_knowledge_header").applyTextStyle(TextFormatting.UNDERLINE);
            mc.fontRenderer.drawString(leadComponent.getFormattedText(), x - 15 + (side * 152), y - 6, Color.BLACK.getRGB());
            y += mc.fontRenderer.FONT_HEIGHT;
            y += 24;    // Make room for knowledge widgets
        }
        
        // Render research requirement section
        if (this.stage.getRequiredResearch() != null) {
            ITextComponent leadComponent = new TranslationTextComponent("primalmagic.grimoire.required_research_header").applyTextStyle(TextFormatting.UNDERLINE);
            mc.fontRenderer.drawString(leadComponent.getFormattedText(), x - 15 + (side * 152), y - 6, Color.BLACK.getRGB());
            y += mc.fontRenderer.FONT_HEIGHT;
            y += 24;    // Make room for research widgets
        }
    }
}
