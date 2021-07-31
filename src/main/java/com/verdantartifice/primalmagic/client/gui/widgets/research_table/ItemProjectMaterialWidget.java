package com.verdantartifice.primalmagic.client.gui.widgets.research_table;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemProjectMaterial;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        // Draw stack icon and, if applicable, amount string
        Minecraft mc = Minecraft.getInstance();
        GuiUtils.renderItemStack(matrixStack, this.material.getItemStack(), this.x, this.y, this.getMessage().getString(), false);
        if (this.material.getItemStack().getCount() > 1) {
            Component amountText = new TextComponent(Integer.toString(this.material.getItemStack().getCount()));
            int width = mc.font.width(amountText);
            matrixStack.pushPose();
            matrixStack.translate(this.x + 16 - width / 2, this.y + 12, 500.0F);
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            mc.font.drawShadow(matrixStack, amountText, 0.0F, 0.0F, Color.WHITE.getRGB());
            matrixStack.popPose();
        }
        
        // Draw base class stuff
        super.renderButton(matrixStack, p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
    }
    
    @Override
    protected List<Component> getHoverText() {
        Minecraft mc = Minecraft.getInstance();
        ItemStack stack = this.material.getItemStack();
        List<Component> textList = new ArrayList<>();
        textList.add(stack.getHoverName().copy().withStyle(stack.getItem().getRarity(stack).color));
        stack.getItem().appendHoverText(stack, mc.level, textList, mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
        return textList;
    }
}
