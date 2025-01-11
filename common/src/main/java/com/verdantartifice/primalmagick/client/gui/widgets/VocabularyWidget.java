package com.verdantartifice.primalmagick.client.gui.widgets;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.awt.Color;
import java.util.Collections;

/**
 * Display widget for showing how much vocabulary a player knows for a given static book language.
 * 
 * @author Daedalus4096
 */
public class VocabularyWidget extends AbstractWidget {
    private static final ResourceLocation BORDER_SPRITE = ResourceUtils.loc("scribe_table/glyph_border");

    protected Holder<BookLanguage> language;
    protected int amount;
    
    public VocabularyWidget(Holder<BookLanguage> language, int amount, int xIn, int yIn) {
        super(xIn, yIn, 16, 16, Component.empty());
        this.language = language;
        this.amount = amount;
    }
    
    public Holder<BookLanguage> getLanguage() {
        return this.language;
    }
    
    public void setLanguage(Holder<BookLanguage> language) {
        this.language = language;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(this.getX(), this.getY(), 0.0F);

        // Render border sprite
        pGuiGraphics.blitSprite(BORDER_SPRITE, 0, 0, 16, 16);
        
        // Render language glyph
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(4, 4, 2.0F);
        pGuiGraphics.blitSprite(this.getLanguage().value().getGlyphSprite(), 0, 0, 8, 8);
        pGuiGraphics.pose().popPose();

        // Render the amount string
        pGuiGraphics.pose().pushPose();
        Component amountText = Component.literal(Integer.toString(this.getAmount()));
        int width = mc.font.width(amountText.getString());
        pGuiGraphics.pose().translate(16 - width / 2, 12, 5.0F);
        pGuiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
        pGuiGraphics.drawString(mc.font, amountText, 0, 0, Color.WHITE.getRGB());
        pGuiGraphics.pose().popPose();

        pGuiGraphics.pose().popPose();

        // Draw the tooltip if applicable
        if (this.isHoveredOrFocused()) {
            pGuiGraphics.renderComponentTooltip(mc.font, Collections.singletonList(Component.translatable("tooltip.primalmagick.scribe_table.widget.language", this.getLanguage().value().getName(), this.getAmount())),
                    pMouseX, pMouseY, ItemStack.EMPTY);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
    }

}
