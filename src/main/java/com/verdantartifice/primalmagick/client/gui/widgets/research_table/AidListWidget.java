package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.util.GuiUtils;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Display widget for showing all nearby research aids on the research table.
 * 
 * @author Daedalus4096
 */
public class AidListWidget extends AbstractWidget {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/research_table_overlay.png");
    
    protected final List<Component> aidNames;

    public AidListWidget(int x, int y, List<Component> aidNames) {
        super(x, y, 8, 8, Component.empty());
        this.aidNames = aidNames;
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // Draw padlock icon
        matrixStack.pushPose();
        RenderSystem.setShaderTexture(0, TEXTURE);
        matrixStack.translate(this.getX(), this.getY(), 0.0F);
        this.blit(matrixStack, 0, 0, 206, 0, 8, 8);
        matrixStack.popPose();
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput p_169152_) {
    }

    @Override
    public void renderToolTip(PoseStack matrixStack, int mouseX, int mouseY) {
        if (!this.aidNames.isEmpty()) {
            // Render tooltip
            List<Component> tooltip = new ArrayList<>();
            tooltip.add(Component.translatable("primalmagick.research_table.aid_header"));
            tooltip.addAll(this.aidNames);
            GuiUtils.renderCustomTooltip(matrixStack, tooltip, mouseX, mouseY);
        }
    }
}
