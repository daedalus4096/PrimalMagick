package com.verdantartifice.primalmagick.common.runes;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.research.ResearchName;

import net.minecraft.world.item.Rarity;

/**
 * Definition of a power rune data structure.  An optional rune to boost imbued enchantments.
 * 
 * @author Daedalus4096
 */
public class PowerRune extends Rune {
    public PowerRune(@Nonnull String tag, @Nonnull Supplier<ResearchName> discoveryKey, Rarity rarity, int limit) {
        super(tag, discoveryKey, rarity, true, limit);
    }
    
    @Override
    public RuneType getType() {
        return RuneType.POWER;
    }
}
