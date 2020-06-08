package com.verdantartifice.primalmagic.common.runes;

import javax.annotation.Nonnull;

import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;

/**
 * Definition of a verb rune data structure.  One of three rune types needed to imbue an enchantment.
 * 
 * @author Daedalus4096
 */
public class VerbRune extends Rune {
    public VerbRune(@Nonnull String tag) {
        super(tag, Rarity.COMMON, false);
    }
    
    public VerbRune(@Nonnull ResourceLocation id) {
        super(id, Rarity.COMMON, false);
    }
    
    @Override
    public RuneType getType() {
        return RuneType.VERB;
    }
}
