package com.verdantartifice.primalmagick.common.runes;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

/**
 * Definition of a power rune data structure.  An optional rune to boost imbued enchantments.
 * 
 * @author Daedalus4096
 */
public class PowerRune extends Rune {
    public PowerRune(@Nonnull String tag, @Nonnull String discoveryTag, Rarity rarity, int limit) {
        super(tag, discoveryTag, rarity, true, limit);
    }
    
    public PowerRune(@Nonnull ResourceLocation id, @Nonnull SimpleResearchKey discoveryKey, Rarity rarity, int limit) {
        super(id, discoveryKey, rarity, true, limit);
    }
    
    @Override
    public RuneType getType() {
        return RuneType.POWER;
    }
}
