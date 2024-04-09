package com.verdantartifice.primalmagick.client.gui.scribe_table;

import java.util.ArrayList;
import java.util.List;

import com.verdantartifice.primalmagick.client.gui.AbstractContainerScreenPM;
import com.verdantartifice.primalmagick.common.books.ScribeTableMode;
import com.verdantartifice.primalmagick.common.menus.AbstractScribeTableMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Base class for GUI screens for the scribe table.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractScribeTableScreen<T extends AbstractScribeTableMenu> extends AbstractContainerScreenPM<T> {
    protected final List<ScribeTableModeTabButton> tabButtons = new ArrayList<>();
    
    public AbstractScribeTableScreen(T menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }
    
    protected abstract ScribeTableMode getMode();
    
    protected abstract ResourceLocation getBgTexture();

    @Override
    protected void init() {
        super.init();
        int tabPosX = this.leftPos - 30;
        int tabPosY = this.topPos + 3;
        int tabCount = 0;
        this.tabButtons.clear();
        for (ScribeTableMode mode : ScribeTableMode.values()) {
            ScribeTableModeTabButton tab = new ScribeTableModeTabButton(mode, this);
            tab.setPosition(tabPosX, tabPosY + 27 * tabCount++);
            tab.setStateTriggered(mode == this.getMode());
            this.tabButtons.add(tab);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        // Render background texture
        pGuiGraphics.blit(this.getBgTexture(), this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        this.tabButtons.forEach(tab -> tab.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick));
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        for (ScribeTableModeTabButton tab : this.tabButtons) {
            if (tab.mouseClicked(pMouseX, pMouseY, pButton)) {
                return true;
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }
}
