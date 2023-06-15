package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.theorycrafting.AbstractProjectMaterial;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

/**
 * Base class for a display widget for a research project material.  Used on the research table screen.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractProjectMaterialWidget extends AbstractWidget {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/research_table_overlay.png");

    protected boolean complete;
    protected boolean consumed;
    protected boolean hasBonus;
    
    public AbstractProjectMaterialWidget(AbstractProjectMaterial material, int x, int y, Set<Block> surroundings) {
        super(x, y, 16, 16, Component.empty());
        Minecraft mc = Minecraft.getInstance();
        this.hasBonus = material.getBonusReward() > 0.0D;
        this.consumed = material.isConsumed();
        this.complete = material.isSatisfied(mc.player, this.consumed ? Collections.emptySet() : surroundings);
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        if (this.complete) {
            // Render completion checkmark if appropriate
            matrixStack.pushPose();
            matrixStack.translate(this.x + 8, this.y, 200.0F);
            this.blit(matrixStack, 0, 0, 162, 0, 10, 10);
            matrixStack.popPose();
        }
        if (this.consumed) {
            // Render consumption exclamation point if appropriate
            matrixStack.pushPose();
            matrixStack.translate(this.x - 3, this.y - 2, 200.0F);
            this.blit(matrixStack, 0, 0, 172, 0, 10, 10);
            matrixStack.popPose();
        }
        if (this.hasBonus) {
            // Render bonus indicator if appropriate
            matrixStack.pushPose();
            matrixStack.translate(this.x - 1, this.y + 10, 200.0F);
            this.blit(matrixStack, 0, 0, 215, 0, 6, 5);
            matrixStack.popPose();
        }
    }
    
    /**
     * Get the text component to show in a tooltip when this widget is hovered over.
     * 
     * @return the text component to show in a tooltip
     */
    protected abstract List<Component> getHoverText();
    
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
        List<Component> tooltip = new ArrayList<>(this.getHoverText());
        if (this.consumed) {
            tooltip.add(Component.translatable("tooltip.primalmagick.research_table.material.consumed").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
        if (this.hasBonus) {
            tooltip.add(Component.translatable("tooltip.primalmagick.research_table.material.has_bonus").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
        GuiUtils.renderCustomTooltip(matrixStack, tooltip, mouseX, mouseY);
        matrixStack.popPose();
    }
}
