package com.verdantartifice.primalmagick.datagen.lang;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import net.minecraft.data.PackOutput;

/**
 * Language data provider for US English.
 * 
 * @author Daedalus4096
 */
public class LanguageProviderEnUs extends AbstractLanguageProviderPM {
    public LanguageProviderEnUs(PackOutput output) {
        super(output, "en_us");
    }

    @Override
    protected void addLocalizations() {
        // Generate block localizations
        this.block(BlocksPM.MARBLE_RAW).name("Marble").build();
        
        // Generate item localizations
        this.item(ItemsPM.HALLOWED_ORB).name("Hallowed Orb").tooltip("A sense of peace washes over", "you as you hold this").build();
        this.item(ItemsPM.RUNE_EARTH).name("Earth Rune").tooltip("\"Teq\"").build();
    }
}
