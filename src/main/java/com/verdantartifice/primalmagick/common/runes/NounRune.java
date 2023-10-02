package com.verdantartifice.primalmagick.common.runes;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.research.ResearchName;

import net.minecraft.world.item.Rarity;

/**
 * Definition of a noun rune data structure.  One of three rune types needed to imbue an enchantment.
 * 
 * @author Daedalus4096
 */
public class NounRune extends Rune {
    public NounRune(@Nonnull String tag, @Nonnull Supplier<ResearchName> discoveryKey) {
        super(tag, discoveryKey, Rarity.COMMON, false, -1);
    }
    
    @Override
    public RuneType getType() {
        return RuneType.NOUN;
    }
}
