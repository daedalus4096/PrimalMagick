package com.verdantartifice.primalmagick.datagen.lang;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.sources.Source;

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
        // Generate magickal source localizations
        this.source(Source.EARTH).name("Earth").build();
        this.source(Source.SEA).name("Sea").build();
        this.source(Source.SKY).name("Sky").build();
        this.source(Source.SUN).name("Sun").build();
        this.source(Source.MOON).name("Moon").build();
        this.source(Source.BLOOD).name("Blood").build();
        this.source(Source.INFERNAL).name("Infernal").build();
        this.source(Source.VOID).name("Void").build();
        this.source(Source.HALLOWED).name("Hallowed").build();
        this.add(Source.getUnknownTranslationKey(), "Unknown");
        
        // Generate block localizations
        this.block(BlocksPM.MARBLE_RAW).name("Marble").build();
        
        // Generate item localizations
        this.item(ItemsPM.HALLOWED_ORB).name("Hallowed Orb").tooltip("A sense of peace washes over", "you as you hold this").build();
        this.item(ItemsPM.RUNE_EARTH).name("Earth Rune").tooltip("\"Teq\"").build();
    }
}
