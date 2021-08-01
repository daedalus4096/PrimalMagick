package com.verdantartifice.primalmagic.common.runes;

import javax.annotation.Nonnull;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

/**
 * Definition of a source rune data structure.  One of three rune types needed to imbue an enchantment.
 * 
 * @author Daedalus4096
 */
public class SourceRune extends Rune {
    public SourceRune(@Nonnull String tag) {
        super(tag, Rarity.COMMON, false);
    }
    
    public SourceRune(@Nonnull ResourceLocation id) {
        super(id, Rarity.COMMON, false);
    }
    
    @Override
    public RuneType getType() {
        return RuneType.SOURCE;
    }
}
