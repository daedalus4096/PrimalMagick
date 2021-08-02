package com.verdantartifice.primalmagic.client.gui.widgets;

import java.awt.Color;
import java.util.Collections;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * Class for display widgets which show a gauge of a mana pool.
 * 
 * @author Daedalus4096
 */
public class ManaGaugeWidget extends AbstractWidget {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/mana_gauge.png");

    protected final Source source;
    protected int maxAmount;
    protected int curAmount;
    
    public ManaGaugeWidget(int xPos, int yPos, Source source, int curAmount, int maxAmount) {
        super(xPos, yPos, 12, 52, TextComponent.EMPTY);
        this.source = source;
        this.curAmount = curAmount;
        this.maxAmount = maxAmount;
    }
    
    public void setCurrentMana(int amount) {
        this.curAmount = amount;
    }
    
    public void setMaxMana(int amount) {
        this.maxAmount = amount;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Disable click behavior
        return false;
    }

    @Override
    public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, TEXTURE);
        
        matrixStack.pushPose();
        matrixStack.translate(this.x, this.y, 0.0F);

        // Render gauge background texture
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.blit(matrixStack, 0, 0, 12, 0, this.width, this.height);
        
        // Render colored gauge
        int mana = this.getScaledMana();
        Color manaColor = new Color(this.source.getColor());
        RenderSystem.setShaderColor(manaColor.getRed() / 255.0F, manaColor.getGreen() / 255.0F, manaColor.getBlue() / 255.0F, 1.0F);
        this.blit(matrixStack, 1, 51 - mana, 1, 1, 10, mana);

        // Render gauge foreground texture
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.blit(matrixStack, 0, 0, 24, 0, this.width, this.height);

        matrixStack.popPose();
        
        if (this.isHovered()) {
            Component sourceText = new TranslatableComponent(this.source.getNameTranslationKey()).withStyle(this.source.getChatColor());
            Component labelText = new TranslatableComponent("primalmagic.source.mana_gauge_tooltip", sourceText, (this.curAmount / 100.0D), (this.maxAmount / 100.0D));
            GuiUtils.renderCustomTooltip(matrixStack, Collections.singletonList(labelText), this.x, this.y);
        }
    }
    
    protected int getScaledMana() {
        if (this.maxAmount != 0 && this.curAmount != 0) {
            return (this.curAmount * 50 / this.maxAmount);
        } else {
            return 0;
        }
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {
    }
}
