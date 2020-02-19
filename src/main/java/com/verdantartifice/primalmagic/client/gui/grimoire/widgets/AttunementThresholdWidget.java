package com.verdantartifice.primalmagic.client.gui.grimoire.widgets;

import java.util.Arrays;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
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
    protected final Source source;
    protected final AttunementThreshold threshold;
    protected final ResourceLocation texture;
    protected final ITextComponent tooltipHeaderText;
    protected final ITextComponent tooltipTextText;
    
    public AttunementThresholdWidget(@Nonnull Source source, @Nonnull AttunementThreshold threshold, int x, int y) {
        super(x, y, 16, 16, "");
        this.source = source;
        this.threshold = threshold;
        this.texture = new ResourceLocation(PrimalMagic.MODID, "textures/attunements/threshold_" + source.getTag() + "_" + threshold.getTag() + ".png");
        
        ITextComponent thresholdText = new TranslationTextComponent(this.threshold.getTranslationKey());
        ITextComponent sourceText = new TranslationTextComponent(this.source.getNameTranslationKey()).applyTextStyle(this.source.getChatColor());
        this.tooltipHeaderText = new TranslationTextComponent("primalmagic.attunement.threshold.header", thresholdText, sourceText);
        this.tooltipTextText = new TranslationTextComponent("primalmagic.attunement.threshold." + source.getTag() + "." + threshold.getTag());
    }
    
    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        
        if (this.threshold == AttunementThreshold.MINOR) {
            // TODO Render casting wand into GUI
        } else {
            // Render the icon appropriate for this widget's source and threshold
            RenderSystem.pushMatrix();
            mc.getTextureManager().bindTexture(this.texture);
            RenderSystem.translatef(this.x, this.y, 0.0F);
            RenderSystem.scaled(0.0625D, 0.0625D, 0.0625D);
            this.blit(0, 0, 0, 0, 255, 255);
            RenderSystem.popMatrix();
        }
        
        if (this.isHovered()) {
            // Render tooltip
            GuiUtils.renderCustomTooltip(Arrays.asList(this.tooltipHeaderText, this.tooltipTextText), this.x, this.y);
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }
}
