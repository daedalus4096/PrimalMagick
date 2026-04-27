package com.verdantartifice.primalmagick.client.gui.widgets;

import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Collections;

/**
 * Class for display widgets which show a gauge of a mana pool.
 * 
 * @author Daedalus4096
 */
public class ManaGaugeWidget extends AbstractWidget {
    protected static final Identifier TEXTURE = ResourceUtils.loc("textures/gui/mana_gauge.png");
    protected static final DecimalFormat MANA_FORMATTER = new DecimalFormat("#######.##");

    protected final Source source;
    protected int maxAmount;
    protected int curAmount;
    
    public ManaGaugeWidget(int xPos, int yPos, Source source, int curAmount, int maxAmount) {
        super(xPos, yPos, 12, 52, Component.empty());
        this.source = source;
        this.curAmount = curAmount;
        this.maxAmount = maxAmount;
    }
    
    public void setPosition(int newX, int newY) {
        this.setX(newX);
        this.setY(newY);
    }
    
    public void setCurrentMana(int amount) {
        this.curAmount = amount;
    }
    
    public void setMaxMana(int amount) {
        this.maxAmount = amount;
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean doubleClick) {
        // Disable click behavior
        return false;
    }

    @Override
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.pose().pushMatrix();
        pGuiGraphics.pose().translate(this.getX(), this.getY());

        // Render gauge background texture
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        pGuiGraphics.blit(TEXTURE, 0, 0, 12, 0, this.width, this.height);
        
        // Render colored gauge
        int mana = this.getScaledMana();
        Color manaColor = new Color(this.source.getColor());
        pGuiGraphics.setColor(manaColor.getRed() / 255.0F, manaColor.getGreen() / 255.0F, manaColor.getBlue() / 255.0F, 1.0F);
        pGuiGraphics.blit(TEXTURE, 1, 51 - mana, 1, 1, 10, mana);

        // Render gauge foreground texture
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        pGuiGraphics.blit(TEXTURE, 0, 0, 24, 0, this.width, this.height);

        pGuiGraphics.pose().popMatrix();
        
        if (this.isHoveredOrFocused()) {
            Component sourceText = this.source.getNameText();
            Component labelText = Component.translatable("tooltip.primalmagick.source.mana_gauge", sourceText, this.getManaText(), this.getMaxManaText());
            GuiUtils.renderCustomTooltip(pGuiGraphics, Collections.singletonList(labelText), this.getX(), this.getY());
        }
    }
    
    protected int getScaledMana() {
        if (this.maxAmount == -1) {
            return 50;
        } else if (this.maxAmount != 0 && this.curAmount != 0) {
            return (this.curAmount * 50 / this.maxAmount);
        } else {
            return 0;
        }
    }

    protected MutableComponent getManaText() {
        if (this.maxAmount == -1) {
            // If the given source has infinite mana, show the infinity symbol
            return Component.literal(Character.toString('\u221E'));
        } else {
            // Otherwise show the current whole mana value for that source
            return Component.literal(MANA_FORMATTER.format(this.curAmount / 100.0D));
        }
    }

    public MutableComponent getMaxManaText() {
        if (this.maxAmount == -1) {
            // If the given source has infinite mana, show the infinity symbol
            return Component.literal(Character.toString('\u221E'));
        } else {
            // Otherwise show the max whole mana value for that source
            return Component.literal(MANA_FORMATTER.format(this.maxAmount / 100.0D));
        }
    }
    
    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
    }
}
