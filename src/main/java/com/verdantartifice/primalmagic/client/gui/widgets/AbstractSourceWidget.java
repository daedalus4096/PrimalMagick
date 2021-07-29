package com.verdantartifice.primalmagic.client.gui.widgets;

import java.awt.Color;
import java.util.Collections;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Base class for display widgets which show a source icon with amount.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public abstract class AbstractSourceWidget extends AbstractWidget {
    protected Source source;
    protected int amount;

    public AbstractSourceWidget(Source source, int amount, int xIn, int yIn) {
        super(xIn, yIn, 16, 16, TextComponent.EMPTY);
        this.source = source;
        this.amount = amount;
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        boolean discovered = this.source.isDiscovered(mc.player);
        
        // Draw the colored source icon
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        matrixStack.pushPose();
        if (discovered) {
            mc.getTextureManager().bindForSetup(this.source.getImage());
        } else {
            mc.getTextureManager().bindForSetup(Source.getUnknownImage());
        }
        matrixStack.translate(this.x, this.y, 0.0F);
        matrixStack.scale(0.0625F, 0.0625F, 0.0625F);
        this.blit(matrixStack, 0, 0, 0, 0, 255, 255);
        matrixStack.popPose();
        
        // Draw the amount string
        matrixStack.pushPose();
        Component amountText = new TextComponent(Integer.toString(this.amount));
        int width = mc.font.width(amountText.getString());
        matrixStack.translate(this.x + 16 - width / 2, this.y + 12, 5.0F);
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        mc.font.drawShadow(matrixStack, amountText, 0.0F, 0.0F, Color.WHITE.getRGB());
        matrixStack.popPose();
        
        // Draw the tooltip if applicable
        if (this.isHovered()) {
            Component sourceText = discovered ? 
                    new TranslatableComponent(this.source.getNameTranslationKey()).withStyle(this.source.getChatColor()) :
                    new TranslatableComponent(Source.getUnknownTranslationKey());
            Component labelText = new TranslatableComponent(this.getTooltipTranslationKey(), this.amount, sourceText);
            GuiUtils.renderCustomTooltip(matrixStack, Collections.singletonList(labelText), this.x, this.y);
        }
    }
    
    /**
     * Get the translation key for this widget's tooltip
     * @return the translation key for this widget's tooltip
     */
    protected abstract String getTooltipTranslationKey();
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {
    }
}
