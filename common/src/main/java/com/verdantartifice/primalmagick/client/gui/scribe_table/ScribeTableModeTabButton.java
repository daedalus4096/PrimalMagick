package com.verdantartifice.primalmagick.client.gui.scribe_table;

import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.common.books.ScribeTableMode;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.scribe_table.ChangeScribeTableModePacket;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.resources.ResourceLocation;

/**
 * Tab button for a mode on the scribe table GUI.
 * 
 * @author Daedalus4096
 */
public class ScribeTableModeTabButton extends StateSwitchingButton {
    protected static final WidgetSprites SPRITES = new WidgetSprites(ResourceUtils.loc("scribe_table/tab"), ResourceUtils.loc("scribe_table/tab_selected"));

    protected final ScribeTableMode mode;
    protected final AbstractScribeTableScreen<?> owner;
    
    public ScribeTableModeTabButton(ScribeTableMode mode, AbstractScribeTableScreen<?> screen) {
        super(0, 0, 35, 27, false);
        this.mode = mode;
        this.owner = screen;
        this.initTextureValues(SPRITES);
        this.setTooltip(Tooltip.create(mode.getTooltip()));
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (this.sprites != null) {
            RenderSystem.disableDepthTest();
            ResourceLocation spriteLoc = this.sprites.get(true, this.isStateTriggered);
            int x = this.getX();
            if (this.isStateTriggered) {
                x -= 2;
            }
            
            pGuiGraphics.blitSprite(spriteLoc, x, this.getY(), this.width, this.height);
            RenderSystem.enableDepthTest();
            this.renderIcon(pGuiGraphics);
        }
    }

    protected void renderIcon(GuiGraphics pGuiGraphics) {
        ResourceLocation spriteLoc = this.mode.getIconSprite();
        int dx = this.isStateTriggered() ? -2 : 0;
        pGuiGraphics.blitSprite(spriteLoc, this.getX() + 9 + dx, this.getY() + 5, 16, 16);
    }
    
    public ScribeTableMode getMode() {
        return this.mode;
    }

    @Override
    public void onClick(double pMouseX, double pMouseY) {
        if (!this.isStateTriggered()) {
            PacketHandler.sendToServer(new ChangeScribeTableModePacket(this.owner.getMenu().containerId, this.getMode()));
        }
    }
}
