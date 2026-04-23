package com.verdantartifice.primalmagick.client.gui.scribe_table;

import com.verdantartifice.primalmagick.common.books.ScribeTableMode;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.scribe_table.ChangeScribeTableModePacket;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * Tab button for a mode on the scribe table GUI.
 * 
 * @author Daedalus4096
 */
public class ScribeTableModeTabButton extends ImageButton {
    protected static final WidgetSprites SPRITES = new WidgetSprites(ResourceUtils.loc("scribe_table/tab"), ResourceUtils.loc("scribe_table/tab_selected"));

    protected final ScribeTableMode mode;
    protected final AbstractScribeTableScreen<?> owner;

    private boolean selected = false;

    public ScribeTableModeTabButton(ScribeTableMode mode, AbstractScribeTableScreen<?> screen) {
        super(0, 0, 35, 27, SPRITES, ScribeTableModeTabButton::onClick);
        this.mode = mode;
        this.owner = screen;
        this.setTooltip(Tooltip.create(mode.getTooltip()));
    }

    @Override
    public void renderContents(@NotNull GuiGraphicsExtractor pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Identifier spriteLoc = this.sprites.get(true, this.selected);
        int x = this.getX();
        if (this.selected) {
            x -= 2;
        }

        pGuiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, spriteLoc, x, this.getY(), this.width, this.height);
        this.renderIcon(pGuiGraphics);
    }

    protected void renderIcon(GuiGraphicsExtractor pGuiGraphics) {
        Identifier spriteLoc = this.mode.getIconSprite();
        int dx = this.selected ? -2 : 0;
        pGuiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, spriteLoc, this.getX() + 9 + dx, this.getY() + 5, 16, 16);
    }
    
    public ScribeTableMode getMode() {
        return this.mode;
    }

    protected static void onClick(Button button) {
        if (button instanceof ScribeTableModeTabButton tabButton && !tabButton.selected) {
            PacketHandler.sendToServer(new ChangeScribeTableModePacket(tabButton.owner.getMenu().containerId, tabButton.getMode()));
        }
    }

    public void setSelected(boolean value) {
        this.selected = value;
    }
}
