package com.verdantartifice.primalmagick.common.runes;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

/**
 * Definition of a source rune data structure.  One of three rune types needed to imbue an enchantment.
 * 
 * @author Daedalus4096
 */
public class SourceRune extends Rune {
    public SourceRune(@Nonnull String tag, @Nonnull String discoveryTag) {
        super(tag, discoveryTag, Rarity.COMMON, false);
    }
    
    public SourceRune(@Nonnull ResourceLocation id, @Nonnull SimpleResearchKey discoveryKey) {
        super(id, discoveryKey, Rarity.COMMON, false);
    }
    
    @Override
    public RuneType getType() {
        return RuneType.SOURCE;
    }
}
