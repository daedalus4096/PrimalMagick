package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.grimoire.buttons.ItemStackWidget;
import com.verdantartifice.primalmagic.client.gui.grimoire.buttons.ItemTagWidget;
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
            for (Object obj : this.stage.getMustObtain()) {
                if (obj instanceof ItemStack) {
                    // Render item stack
                    screen.addWidgetToScreen(new ItemStackWidget((ItemStack)obj, x + (side * 152), y));
                } else if (obj instanceof ResourceLocation) {
                    // Render cycling stacks from tag
                    screen.addWidgetToScreen(new ItemTagWidget((ResourceLocation)obj, x + (side * 152), y));
                }
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
        int startX = x;
        
        // Render obtain requirement section
        if (!this.stage.getMustObtain().isEmpty()) {
            ITextComponent leadComponent = new TranslationTextComponent("primalmagic.grimoire.must_obtain_header").applyTextStyle(TextFormatting.UNDERLINE);
            mc.fontRenderer.drawString(leadComponent.getFormattedText(), x - 15 + (side * 152), y - 6, Color.BLACK.getRGB());
            y += mc.fontRenderer.FONT_HEIGHT;
            y += 24;    // Make room for obtain widgets
        }
        
        // TODO Render craft requirement section
        if (!this.stage.getMustCraft().isEmpty()) {
            ITextComponent leadComponent = new TranslationTextComponent("primalmagic.grimoire.must_craft_header").applyTextStyle(TextFormatting.UNDERLINE);
            mc.fontRenderer.drawString(leadComponent.getFormattedText(), x - 15 + (side * 152), y - 6, Color.BLACK.getRGB());
            y += mc.fontRenderer.FONT_HEIGHT;
            
            for (Object obj : this.stage.getMustCraft()) {
                if (obj instanceof ItemStack) {
                    // TODO Render item stack
                } else if (obj instanceof ResourceLocation) {
                    // TODO Render cycling stacks from tag
                }
                x += 18;
            }
            x = startX;
            y += 24;
        }
        
        // TODO Render knowledge requirement section
        if (!this.stage.getRequiredKnowledge().isEmpty()) {
            ITextComponent leadComponent = new TranslationTextComponent("primalmagic.grimoire.required_knowledge_header").applyTextStyle(TextFormatting.UNDERLINE);
            mc.fontRenderer.drawString(leadComponent.getFormattedText(), x - 15 + (side * 152), y - 6, Color.BLACK.getRGB());
            y += mc.fontRenderer.FONT_HEIGHT;
            
            for (Knowledge know : this.stage.getRequiredKnowledge()) {
                // TODO Render knowledge symbol
                x += 18;
            }
            x = startX;
            y += 24;
        }
        
        // TODO Render research requirement section
        if (this.stage.getRequiredResearch() != null) {
            ITextComponent leadComponent = new TranslationTextComponent("primalmagic.grimoire.required_research_header").applyTextStyle(TextFormatting.UNDERLINE);
            mc.fontRenderer.drawString(leadComponent.getFormattedText(), x - 15 + (side * 152), y - 6, Color.BLACK.getRGB());
            y += mc.fontRenderer.FONT_HEIGHT;
            
            for (SimpleResearchKey srk : this.stage.getRequiredResearch().getKeys()) {
                // TODO Render research symbol
                x += 18;
            }
            x = startX;
            y += 24;
        }
    }
}
