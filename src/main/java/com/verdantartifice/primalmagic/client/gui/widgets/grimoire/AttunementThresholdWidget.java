package com.verdantartifice.primalmagic.client.gui.widgets.grimoire;

import java.util.Collections;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.wands.WandCap;
import com.verdantartifice.primalmagic.common.wands.WandCore;
import com.verdantartifice.primalmagic.common.wands.WandGem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Display widget for showing attunement threshold bonuses.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class AttunementThresholdWidget extends Widget {
    protected static final ItemStack WAND_STACK = Util.make(new ItemStack(ItemsPM.MODULAR_WAND.get()), stack -> {
        ItemsPM.MODULAR_WAND.get().setWandCore(stack, WandCore.HEARTWOOD);
        ItemsPM.MODULAR_WAND.get().setWandCap(stack, WandCap.IRON);
        ItemsPM.MODULAR_WAND.get().setWandGem(stack, WandGem.APPRENTICE);
    });
    
    protected final Source source;
    protected final AttunementThreshold threshold;
    protected final ResourceLocation texture;
    protected final ITextComponent tooltipText;
    
    public AttunementThresholdWidget(@Nonnull Source source, @Nonnull AttunementThreshold threshold, int x, int y) {
        super(x, y, 18, 18, StringTextComponent.EMPTY);
        this.source = source;
        this.threshold = threshold;
        this.texture = new ResourceLocation(PrimalMagic.MODID, "textures/attunements/threshold_" + source.getTag() + "_" + threshold.getTag() + ".png");
        this.tooltipText = new TranslationTextComponent("primalmagic.attunement.threshold." + source.getTag() + "." + threshold.getTag());
    }
    
    @Override
    public void renderWidget(MatrixStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        
        if (this.threshold == AttunementThreshold.MINOR) {
            // Render casting wand into GUI
            mc.getItemRenderer().renderItemIntoGUI(WAND_STACK, this.x + 1, this.y + 1);
        } else {
            // Render the icon appropriate for this widget's source and threshold
            RenderSystem.pushMatrix();
            mc.getTextureManager().bindTexture(this.texture);
            RenderSystem.translatef(this.x, this.y, 0.0F);
            RenderSystem.scaled(0.0703125D, 0.0703125D, 0.0703125D);
            this.blit(matrixStack, 0, 0, 0, 0, 255, 255);
            RenderSystem.popMatrix();
        }
        
        if (this.isHovered()) {
            // Render tooltip
            GuiUtils.renderCustomTooltip(matrixStack, Collections.singletonList(this.tooltipText), this.x, this.y);
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }
}
