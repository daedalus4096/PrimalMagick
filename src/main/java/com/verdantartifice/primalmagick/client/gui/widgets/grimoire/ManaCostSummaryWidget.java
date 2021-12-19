package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * Display widget for summarizing recipe mana costs.  Used on arcane recipe pages.
 * 
 * @author Daedalus4096
 */
public class ManaCostSummaryWidget extends AbstractWidget {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/mana_cost_summary_widget.png");

    protected SourceList manaCosts;
    
    public ManaCostSummaryWidget(SourceList manaCosts, int x, int y) {
        super(x, y, 16, 16, TextComponent.EMPTY);
        this.manaCosts = manaCosts;
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        // Render the base widget
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        matrixStack.pushPose();
        RenderSystem.setShaderTexture(0, TEXTURE);
        matrixStack.translate(this.x, this.y, 0.0F);
        matrixStack.scale(0.0625F, 0.0625F, 0.0625F);
        this.blit(matrixStack, 0, 0, 0, 0, 255, 255);
        matrixStack.popPose();

        // Render tooltip if hovered over
        if (this.isHoveredOrFocused()) {
            List<Component> tooltip = new ArrayList<>();
            if (this.manaCosts.isEmpty()) {
                tooltip.add(new TranslatableComponent("primalmagick.crafting.no_mana"));
            } else {
                tooltip.add(new TranslatableComponent("primalmagick.crafting.mana_cost_header"));
                for (Source source : this.manaCosts.getSourcesSorted()) {
                    boolean discovered = source.isDiscovered(mc.player);
                    Component sourceText = discovered ? 
                            source.getNameText() :
                            new TranslatableComponent(Source.getUnknownTranslationKey());
                    tooltip.add(new TranslatableComponent("primalmagick.crafting.mana_tooltip", this.manaCosts.getAmount(source), sourceText));
                }
            }
            GuiUtils.renderCustomTooltip(matrixStack, tooltip, this.x, this.y);
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {
    }
}
