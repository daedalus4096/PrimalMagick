package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import java.util.List;
import java.util.Optional;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.research.IconDefinition;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * Display widget for showing a specific required research entry on the requirements page.
 * 
 * @author Daedalus4096
 */
public class ResearchWidget extends AbstractWidget {
    protected static final ResourceLocation UNKNOWN_TEXTURE = PrimalMagick.resource("textures/research/research_unknown.png");
    protected static final ResourceLocation GRIMOIRE_TEXTURE = PrimalMagick.resource("textures/gui/grimoire.png");
    
    protected final AbstractResearchKey<?> key;
    protected final ResearchEntry researchEntry;
    protected final boolean isComplete;
    protected MutableComponent lastTooltip = Component.empty();
    protected MutableComponent tooltip = Component.empty();
    
    public ResearchWidget(AbstractResearchKey<?> key, int x, int y, boolean isComplete) {
        super(x, y, 16, 16, Component.empty());
        this.key = key;
        this.isComplete = isComplete;
        
        Minecraft mc = Minecraft.getInstance();
        if (this.key instanceof ResearchEntryKey entryKey) {
            this.researchEntry = ResearchEntries.getEntry(mc.level.registryAccess(), entryKey);
        } else {
            this.researchEntry = null;
        }
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        IconDefinition iconDef = this.key.getIcon(mc.level.registryAccess());
        long time = System.currentTimeMillis();
        
        // Render the icon
        GuiUtils.renderIconFromDefinition(guiGraphics, iconDef, this.getX(), this.getY());
        
        if (this.isComplete) {
            // Render completion checkmark if appropriate
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(this.getX() + 8, this.getY(), 250.0F);
            guiGraphics.blit(GRIMOIRE_TEXTURE, 0, 0, 159, 207, 10, 10);
            guiGraphics.pose().popPose();
        }
        
        // Prepare the tooltip
        this.lastTooltip = this.tooltip;
        this.tooltip = Component.empty();
        if (this.researchEntry != null) {
            // If there's a research entry behind this key, use its info for the tooltip
            this.researchEntry.getHintTranslationKey().ifPresentOrElse(hintTranslationKey -> {
                if (Screen.hasShiftDown()) {
                    this.tooltip.append(Component.translatable(hintTranslationKey));
                } else {
                    this.tooltip.append(Component.translatable(this.researchEntry.getNameTranslationKey()));
                    this.tooltip.append(CommonComponents.NEW_LINE);
                    this.tooltip.append(Component.translatable("tooltip.primalmagick.more_info").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                }
            }, () -> {
                this.tooltip.append(Component.translatable(this.researchEntry.getNameTranslationKey()));
            });
        } else {
            // If there's no research entry behind this key, use the tooltip data baked into the icon definition, if any
            getIconTooltip(iconDef, time).ifPresent(this.tooltip::append);
        }
        if (!this.lastTooltip.equals(this.tooltip)) {
            this.setTooltip(Tooltip.create(this.tooltip));
        }
    }
    
    protected static Optional<Component> getIconTooltip(IconDefinition iconDef, long time) {
        List<Component> lines = iconDef.getTooltipLines();
        if (!lines.isEmpty()) {
            int index = (int)((time / 1000L) % lines.size());
            return Optional.ofNullable(lines.get(index));
        }
        return Optional.empty();
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
    }
}
