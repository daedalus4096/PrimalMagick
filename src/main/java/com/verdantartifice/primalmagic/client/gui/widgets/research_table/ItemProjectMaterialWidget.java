package com.verdantartifice.primalmagic.client.gui.widgets.research_table;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemProjectMaterial;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Display widget for an item research project material.  Used on the research table screen.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class ItemProjectMaterialWidget extends AbstractProjectMaterialWidget {
    protected ItemProjectMaterial material;
    
    public ItemProjectMaterialWidget(ItemProjectMaterial material, int x, int y) {
        super(material, x, y);
        this.material = material;
    }
    
    @Override
    public void renderWidget(MatrixStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        // Draw stack icon and, if applicable, amount string
        Minecraft mc = Minecraft.getInstance();
        GuiUtils.renderItemStack(this.material.getItemStack(), this.x, this.y, this.getMessage().getString(), false);
        if (this.material.getItemStack().getCount() > 1) {
            ITextComponent amountText = new StringTextComponent(Integer.toString(this.material.getItemStack().getCount()));
            int width = mc.fontRenderer.getStringWidth(amountText.getString());
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.pushMatrix();
            RenderSystem.translatef(this.x + 16 - width / 2, this.y + 12, 500.0F);
            RenderSystem.scaled(0.5D, 0.5D, 0.5D);
            mc.fontRenderer.drawStringWithShadow(matrixStack, amountText.getString(), 0.0F, 0.0F, Color.WHITE.getRGB());
            RenderSystem.popMatrix();
        }
        
        // Draw base class stuff
        super.renderWidget(matrixStack, p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
    }
    
    @Override
    protected ITextComponent getHoverText() {
        return this.material.getItemStack().getDisplayName();
    }
}
