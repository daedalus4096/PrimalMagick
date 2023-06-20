package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import java.util.Collections;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Display widget for showing attunement threshold bonuses.
 * 
 * @author Daedalus4096
 */
public class AttunementThresholdWidget extends AbstractWidget {
    protected static final ItemStack WAND_STACK = Util.make(new ItemStack(ItemsPM.MODULAR_WAND.get()), stack -> {
        ItemsPM.MODULAR_WAND.get().setWandCore(stack, WandCore.HEARTWOOD);
        ItemsPM.MODULAR_WAND.get().setWandCap(stack, WandCap.IRON);
        ItemsPM.MODULAR_WAND.get().setWandGem(stack, WandGem.APPRENTICE);
    });
    
    protected final Source source;
    protected final AttunementThreshold threshold;
    protected final ResourceLocation texture;
    protected final Component tooltipText;
    
    public AttunementThresholdWidget(@Nonnull Source source, @Nonnull AttunementThreshold threshold, int x, int y) {
        super(x, y, 18, 18, Component.empty());
        this.source = source;
        this.threshold = threshold;
        this.texture = new ResourceLocation(PrimalMagick.MODID, "textures/attunements/threshold_" + source.getTag() + "_" + threshold.getTag() + ".png");
        this.tooltipText = Component.translatable("primalmagick.attunement.threshold." + source.getTag() + "." + threshold.getTag());
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        
        if (this.threshold == AttunementThreshold.MINOR) {
            // Render casting wand into GUI
            guiGraphics.renderItem(WAND_STACK, this.getX() + 1, this.getY() + 1);
        } else {
            // Render the icon appropriate for this widget's source and threshold
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(this.getX(), this.getY(), 0.0F);
            guiGraphics.pose().scale(0.0703125F, 0.0703125F, 0.0703125F);
            guiGraphics.blit(this.texture, 0, 0, 0, 0, 255, 255);
            guiGraphics.pose().popPose();
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
    public void renderToolTip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Render tooltip
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 200);
        
        GuiUtils.renderCustomTooltip(guiGraphics, Collections.singletonList(this.tooltipText), mouseX, mouseY);
        
        guiGraphics.pose().popPose();
    }
}
