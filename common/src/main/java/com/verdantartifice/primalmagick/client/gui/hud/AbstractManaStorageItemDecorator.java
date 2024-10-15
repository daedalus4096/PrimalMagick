package com.verdantartifice.primalmagick.client.gui.hud;

import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An item decorator used to render a mana bar on item stacks with attached mana storage.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractManaStorageItemDecorator {
    protected static final Logger LOGGER = LogManager.getLogger();

    protected final Source source;
    
    public AbstractManaStorageItemDecorator(Source source) {
        this.source = source;
    }
    
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        ManaStorage manaCap = stack.get(DataComponentsPM.CAPABILITY_MANA_STORAGE.get());
        if (manaCap != null && manaCap.canStore(this.source)) {
            int width = this.getBarWidth(stack, this.source);
            int color = this.getBarColor(stack, this.source);
            int xPos = xOffset + 2;
            int yPos = yOffset + 13;
            if (stack.isBarVisible()) {
                // Bump up the mana bar by a pixel if the durability bar is visible
                guiGraphics.fill(RenderType.guiOverlay(), xPos, yPos - 1, xPos + 13, yPos, -16777216);
                guiGraphics.fill(RenderType.guiOverlay(), xPos, yPos - 1, xPos + width, yPos, color | -16777216);
            } else {
                guiGraphics.fill(RenderType.guiOverlay(), xPos, yPos, xPos + 13, yPos + 2, -16777216);
                guiGraphics.fill(RenderType.guiOverlay(), xPos, yPos, xPos + width, yPos + 1, color | -16777216);
            }
        }
        return false;
    }

    protected int getBarWidth(ItemStack stack, Source source) {
        ManaStorage manaCap = stack.get(DataComponentsPM.CAPABILITY_MANA_STORAGE.get());
        if (manaCap != null && manaCap.canStore(this.source)) {
            return Math.round((float)manaCap.getManaStored(source) * 13.0F / (float)manaCap.getMaxManaStored(source));
        } else {
            return 0;
        }
    }
    
    protected int getBarColor(ItemStack stack, Source source) {
        // FIXME Use source color?
        return 0x27E1C7;    // Primalite teal
    }
}
