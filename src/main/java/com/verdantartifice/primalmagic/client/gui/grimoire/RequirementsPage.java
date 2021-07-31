package com.verdantartifice.primalmagic.client.gui.grimoire;

import java.awt.Color;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.ItemStackWidget;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.ItemTagWidget;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.KnowledgeWidget;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.ProgressButton;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.ProgressingWidget;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.ResearchWidget;
import com.verdantartifice.primalmagic.common.research.Knowledge;
import com.verdantartifice.primalmagic.common.research.ResearchStage;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Grimoire page showing the requirements needed to advance a research entry to its next stage.
 * 
 * @author Daedalus4096
 */
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
        int startX = x;
        int startY = y;
        y += 27;    // Make room for page title
        Minecraft mc = Minecraft.getInstance();
        
        // Init obtain requirement widgets
        if (!this.stage.getMustObtain().isEmpty()) {
            y += mc.font.lineHeight;   // Make room for section header
            List<Boolean> completion = this.stage.getObtainRequirementCompletion(mc.player);
            for (int index = 0; index < this.stage.getMustObtain().size(); index++) {
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
                Knowledge know = this.stage.getRequiredKnowledge().get(index);
                screen.addWidgetToScreen(new KnowledgeWidget(know, x + 8 + (side * 144), y, completion.get(index).booleanValue()));
                x += 18;
            }
            x = startX;
            y += 18;
        }
        
        // Init research requirement widgets
        if (this.stage.getRequiredResearch() != null) {
            y += mc.font.lineHeight;   // Make room for section header
            List<Boolean> completion = this.stage.getResearchRequirementCompletion(mc.player);
            for (int index = 0; index < this.stage.getRequiredResearch().getKeys().size(); index++) {
                SimpleResearchKey key = this.stage.getRequiredResearch().getKeys().get(index);
                screen.addWidgetToScreen(new ResearchWidget(key, x + 8 + (side * 144), y, completion.get(index).booleanValue()));
                x += 18;
            }
            x = startX;
            y += 18;
        }
        
        // Init progress button if applicable
        y = startY + 141;
        if (screen.isProgressing()) {
            Component text = new TranslatableComponent("primalmagic.grimoire.completing_text");
            screen.addWidgetToScreen(new ProgressingWidget(startX + 16 + (side * 136), y, text));
        } else if (this.stage.arePrerequisitesMet(mc.player)) {
            Component text = new TranslatableComponent("primalmagic.grimoire.complete_button");
            screen.addWidgetToScreen(new ProgressButton(this.stage, startX + 16 + (side * 136), y, text, screen));
        }
    }

    @Override
    public void render(PoseStack matrixStack, int side, int x, int y, int mouseX, int mouseY) {
        // Render page title
        this.renderTitle(matrixStack, side, x, y, mouseX, mouseY, null);
        y += 53;
        
        matrixStack.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        matrixStack.translate(0.0F, 0.0F, 1.0F);  // Bump up slightly in the Z-order to prevent the underline from being swallowed
        Minecraft mc = Minecraft.getInstance();
        
        // Render obtain requirement section
        if (!this.stage.getMustObtain().isEmpty()) {
            Component leadComponent = new TranslatableComponent("primalmagic.grimoire.must_obtain_header").withStyle(ChatFormatting.UNDERLINE);
            mc.font.draw(matrixStack, leadComponent, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB());
            y += mc.font.lineHeight;
            y += 18;    // Make room for obtain widgets
        }
        
        // Render craft requirement section
        if (!this.stage.getMustCraft().isEmpty()) {
            Component leadComponent = new TranslatableComponent("primalmagic.grimoire.must_craft_header").withStyle(ChatFormatting.UNDERLINE);
            mc.font.draw(matrixStack, leadComponent, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB());
            y += mc.font.lineHeight;
            y += 18;    // Make room for craft widgets
        }
        
        // Render knowledge requirement section
        if (!this.stage.getRequiredKnowledge().isEmpty()) {
            Component leadComponent = new TranslatableComponent("primalmagic.grimoire.required_knowledge_header").withStyle(ChatFormatting.UNDERLINE);
            mc.font.draw(matrixStack, leadComponent, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB());
            y += mc.font.lineHeight;
            y += 18;    // Make room for knowledge widgets
        }
        
        // Render research requirement section
        if (this.stage.getRequiredResearch() != null) {
            Component leadComponent = new TranslatableComponent("primalmagic.grimoire.required_research_header").withStyle(ChatFormatting.UNDERLINE);
            mc.font.draw(matrixStack, leadComponent, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB());
            y += mc.font.lineHeight;
            y += 18;    // Make room for research widgets
        }
        
        matrixStack.popPose();
    }
}
