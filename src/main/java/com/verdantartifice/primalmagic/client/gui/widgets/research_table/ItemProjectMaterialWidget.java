package com.verdantartifice.primalmagic.client.gui.widgets.research_table;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemProjectMaterial;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
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
            int width = mc.fontRenderer.getStringPropertyWidth(amountText);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.push();
            matrixStack.translate(this.x + 16 - width / 2, this.y + 12, 500.0F);
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            mc.fontRenderer.drawTextWithShadow(matrixStack, amountText, 0.0F, 0.0F, Color.WHITE.getRGB());
            matrixStack.pop();
        }
        
        // Draw base class stuff
        super.renderWidget(matrixStack, p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
    }
    
    @Override
    protected List<ITextComponent> getHoverText() {
        Minecraft mc = Minecraft.getInstance();
        ItemStack stack = this.material.getItemStack();
        List<ITextComponent> textList = new ArrayList<>();
        textList.add(stack.getDisplayName().deepCopy().mergeStyle(stack.getItem().getRarity(stack).color));
        stack.getItem().addInformation(stack, mc.world, textList, mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
        return textList;
    }
}
