package com.verdantartifice.primalmagic.client.gui.widgets.research_table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProjectMaterial;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

/**
 * Base class for a display widget for a research project material.  Used on the research table screen.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractProjectMaterialWidget extends AbstractWidget {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/research_table_overlay.png");

    protected boolean complete;
    protected boolean consumed;
    protected boolean hasBonus;
    
    public AbstractProjectMaterialWidget(AbstractProjectMaterial material, int x, int y, Set<Block> surroundings) {
        super(x, y, 16, 16, TextComponent.EMPTY);
        Minecraft mc = Minecraft.getInstance();
        this.hasBonus = material.getBonusReward() > 0.0D;
        this.consumed = material.isConsumed();
        this.complete = material.isSatisfied(mc.player, this.consumed ? Collections.emptySet() : surroundings);
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
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
            matrixStack.translate(this.x - 3, this.y, 200.0F);
            this.blit(matrixStack, 0, 0, 172, 0, 10, 10);
            matrixStack.popPose();
        }
        if (this.isHovered()) {
            // Render tooltip
            List<Component> tooltip = new ArrayList<>(this.getHoverText());
            if (this.consumed) {
                tooltip.add(new TranslatableComponent("tooltip.primalmagic.research_table.material.consumed").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            }
            if (this.hasBonus) {
                tooltip.add(new TranslatableComponent("tooltip.primalmagic.research_table.material.has_bonus").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            }
            GuiUtils.renderCustomTooltip(matrixStack, tooltip, this.x, this.y);
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
    public void updateNarration(NarrationElementOutput output) {
    }
}
