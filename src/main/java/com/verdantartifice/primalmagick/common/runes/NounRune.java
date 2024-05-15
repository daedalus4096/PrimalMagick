package com.verdantartifice.primalmagick.common.runes;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.research.ResearchName;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

/**
 * Definition of a noun rune data structure.  One of three rune types needed to imbue an enchantment.
 * 
 * @author Daedalus4096
 */
public class NounRune extends Rune {
    public static final Codec<NounRune> CODEC = ResourceLocation.CODEC.<NounRune>xmap(loc -> {
        if (Rune.getRune(loc) instanceof NounRune noun) {
            return noun;
        } else {
            throw new IllegalArgumentException("Unknown noun rune: " + loc.toString());
        }
    }, n -> n.getId());
    
    public NounRune(@Nonnull String tag, @Nonnull Supplier<ResearchName> discoveryKey) {
        super(tag, discoveryKey, Rarity.COMMON, false, -1);
    }
    
    @Override
    public RuneType getType() {
        return RuneType.NOUN;
    }
}
