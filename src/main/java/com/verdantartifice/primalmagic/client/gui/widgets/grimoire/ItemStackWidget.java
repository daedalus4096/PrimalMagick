package com.verdantartifice.primalmagic.client.gui.widgets.grimoire;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.util.GuiUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Display widget for showing a single itemstack.  Used on the requirements and recipe pages.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class ItemStackWidget extends Widget {
    protected static final ResourceLocation GRIMOIRE_TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/grimoire.png");

    protected ItemStack stack;
    protected boolean isComplete;
    
    public ItemStackWidget(ItemStack stack, int x, int y, boolean isComplete) {
        super(x, y, 16, 16, StringTextComponent.EMPTY);
        this.stack = stack;
        this.isComplete = isComplete;
    }
    
    @Override
    public void renderButton(MatrixStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        
        // Draw stack icon
        GuiUtils.renderItemStack(this.stack, this.x, this.y, this.getMessage().getString(), false);
        
        // Draw amount string if applicable
        if (this.stack.getCount() > 1) {
            ITextComponent amountText = new StringTextComponent(Integer.toString(this.stack.getCount()));
            int width = mc.fontRenderer.getStringWidth(amountText.getString());
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.pushMatrix();
            RenderSystem.translatef(this.x + 16 - width / 2, this.y + 12, 5.0F);
            RenderSystem.scaled(0.5D, 0.5D, 0.5D);
            mc.fontRenderer.drawStringWithShadow(matrixStack, amountText.getString(), 0.0F, 0.0F, Color.WHITE.getRGB());
            RenderSystem.popMatrix();
        }
        
        if (this.isComplete) {
            // Render completion checkmark if appropriate
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.pushMatrix();
            RenderSystem.translatef(this.x + 8, this.y, 200.0F);
            Minecraft.getInstance().getTextureManager().bindTexture(GRIMOIRE_TEXTURE);
            this.blit(matrixStack, 0, 0, 159, 207, 10, 10);
            RenderSystem.popMatrix();
        }
        if (this.isHovered()) {
            // Render tooltip
        	StringTextComponent name = new StringTextComponent(this.stack.getDisplayName().getString());
            List<ITextComponent> textList = Collections.singletonList(name.mergeStyle(this.stack.getItem().getRarity(this.stack).color));
            GuiUtils.renderCustomTooltip(textList, this.x, this.y);
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }
}
