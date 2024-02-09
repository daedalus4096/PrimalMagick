package com.verdantartifice.primalmagick.client.gui.scribe_table;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.ScribeTableMode;
import com.verdantartifice.primalmagick.common.menus.ScribeTranscribeWorksMenu;

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
    }

    @Override
    protected ScribeTableMode getMode() {
        return ScribeTableMode.TRANSCRIBE_WORKS;
    }

    @Override
    protected ResourceLocation getBgTexture() {
        return TEXTURE;
    }
}
