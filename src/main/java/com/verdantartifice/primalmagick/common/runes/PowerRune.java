package com.verdantartifice.primalmagick.common.runes;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.research.ResearchName;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

/**
 * Definition of a power rune data structure.  An optional rune to boost imbued enchantments.
 * 
 * @author Daedalus4096
 */
public class PowerRune extends Rune {
    public static final Codec<PowerRune> CODEC = ResourceLocation.CODEC.<PowerRune>xmap(loc -> {
        if (Rune.getRune(loc) instanceof PowerRune power) {
            return power;
        } else {
            throw new IllegalArgumentException("Unknown power rune: " + loc.toString());
        }
    }, p -> p.getId());
    
    public PowerRune(@Nonnull String tag, @Nonnull Supplier<ResearchName> discoveryKey, Rarity rarity, int limit) {
        super(tag, discoveryKey, rarity, true, limit);
    }
    
    @Override
    public RuneType getType() {
        return RuneType.POWER;
    }
}
