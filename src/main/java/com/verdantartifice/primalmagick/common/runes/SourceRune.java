package com.verdantartifice.primalmagick.common.runes;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

/**
 * Definition of a source rune data structure.  One of three rune types needed to imbue an enchantment.
 * 
 * @author Daedalus4096
 */
public class SourceRune extends Rune {
    protected final Source source;
    
    public SourceRune(@Nonnull String tag, @Nonnull String discoveryTag, @Nonnull Source source) {
        super(tag, discoveryTag, Rarity.COMMON, false, -1);
        this.source = source;
    }
    
    public SourceRune(@Nonnull ResourceLocation id, @Nonnull SimpleResearchKey discoveryKey, @Nonnull Source source) {
        super(id, discoveryKey, Rarity.COMMON, false, -1);
        this.source = source;
    }
    
    @Override
    public RuneType getType() {
        return RuneType.SOURCE;
    }
    
    public Source getSource() {
        return this.source;
    }
}
