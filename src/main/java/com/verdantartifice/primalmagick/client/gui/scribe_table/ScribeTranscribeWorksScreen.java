package com.verdantartifice.primalmagick.client.gui.scribe_table;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.ScribeTableMode;
import com.verdantartifice.primalmagick.common.menus.ScribeTranscribeWorksMenu;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.scribe_table.TranscribeActionPacket;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for the transcribe works mode of the scribe table block.
 * 
 * @author Daedalus4096
 */
public class ScribeTranscribeWorksScreen extends AbstractScribeTableScreen<ScribeTranscribeWorksMenu> {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/scribe_transcribe_works.png");
    
    public ScribeTranscribeWorksScreen(ScribeTranscribeWorksMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 222;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected ScribeTableMode getMode() {
        return ScribeTableMode.TRANSCRIBE_WORKS;
    }

    @Override
    protected ResourceLocation getBgTexture() {
        return TEXTURE;
    }
    
    @Override
    protected void init() {
        super.init();
        this.initControlWidgets();
    }

    protected void initControlWidgets() {
        this.addRenderableWidget(new TranscribeButton(this.menu, this.leftPos, this.topPos));
    }
    
    protected static class TranscribeButton extends ImageButton {
        protected static final WidgetSprites BUTTON_SPRITES = new WidgetSprites(PrimalMagick.resource("scribe_table/transcribe_button"), PrimalMagick.resource("scribe_table/transcribe_button_highlighted"));
        protected static final Component TRANSCRIBE_BUTTON_TOOLTIP = Component.translatable("tooltip.primalmagick.scribe_table.button.transcribe");

        public TranscribeButton(ScribeTranscribeWorksMenu menu, int leftPos, int topPos) {
            super(leftPos + 91, topPos + 62, 20, 18, BUTTON_SPRITES, button -> {
                PacketHandler.sendToServer(new TranscribeActionPacket(menu.containerId));
            });
            this.setTooltip(Tooltip.create(TRANSCRIBE_BUTTON_TOOLTIP));
        }
    }
}
