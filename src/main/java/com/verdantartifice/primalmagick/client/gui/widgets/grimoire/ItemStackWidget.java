package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.util.GuiUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Display widget for showing a single itemstack.  Used on the requirements and recipe pages.
 * 
 * @author Daedalus4096
 */
public class ItemStackWidget extends AbstractWidget {
    protected static final ResourceLocation GRIMOIRE_TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/grimoire.png");

    protected ItemStack stack;
    protected boolean isComplete;
    
    public ItemStackWidget(ItemStack stack, int x, int y, boolean isComplete) {
        super(x, y, 16, 16, Component.empty());
        this.stack = stack;
        this.isComplete = isComplete;
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        
        // Draw stack icon
        GuiUtils.renderItemStack(matrixStack, this.stack, this.getX(), this.getY(), this.getMessage().getString(), false);
        
        // Draw amount string if applicable
        if (this.stack.getCount() > 1) {
            Component amountText = Component.literal(Integer.toString(this.stack.getCount()));
            int width = mc.font.width(amountText.getString());
            matrixStack.pushPose();
            matrixStack.translate(this.getX() + 16 - width / 2, this.getY() + 12, 900.0F);
            matrixStack.scale(0.5F, 0.5F, 1.0F);
            mc.font.drawShadow(matrixStack, amountText, 0.0F, 0.0F, Color.WHITE.getRGB());
            matrixStack.popPose();
        }
        
        if (this.isComplete) {
            // Render completion checkmark if appropriate
            matrixStack.pushPose();
            matrixStack.translate(this.getX() + 8, this.getY(), 200.0F);
            RenderSystem.setShaderTexture(0, GRIMOIRE_TEXTURE);
            this.blit(matrixStack, 0, 0, 159, 207, 10, 10);
            matrixStack.popPose();
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
    }

    @Override
    public void renderToolTip(PoseStack matrixStack, int mouseX, int mouseY) {
        // Render tooltip
        matrixStack.pushPose();
        matrixStack.translate(0, 0, 200);
        
        GuiUtils.renderItemTooltip(matrixStack, this.stack, mouseX, mouseY);
        
        matrixStack.popPose();
    }
}
