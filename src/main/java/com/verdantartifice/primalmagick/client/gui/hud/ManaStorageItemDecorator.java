package com.verdantartifice.primalmagick.client.gui.hud;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;

/**
 * An item decorator used to render a mana bar on item stacks with attached mana storage.
 * 
 * @author Daedalus4096
 */
public class ManaStorageItemDecorator implements IItemDecorator {
    protected static final Logger LOGGER = LogManager.getLogger();

    protected final Source source;
    
    public ManaStorageItemDecorator(Source source) {
        this.source = source;
    }
    
    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        stack.getCapability(PrimalMagickCapabilities.MANA_STORAGE).filter(manaCap -> manaCap.canStore(this.source)).ifPresent(manaCap -> {
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
        });
        return false;
    }

    protected int getBarWidth(ItemStack stack, Source source) {
        return stack.getCapability(PrimalMagickCapabilities.MANA_STORAGE).filter(manaCap -> manaCap.canStore(this.source)).map(manaCap -> {
            return Math.round((float)manaCap.getManaStored(source) * 13.0F / (float)manaCap.getMaxManaStored(source));
        }).orElse(0);
    }
    
    protected int getBarColor(ItemStack stack, Source source) {
        // FIXME Use source color?
        return 0x27E1C7;    // Primalite teal
    }
}
