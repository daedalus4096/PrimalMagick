package com.verdantartifice.primalmagick.common.runes;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

import javax.annotation.Nonnull;

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
    
    public PowerRune(@Nonnull String tag, @Nonnull ResourceKey<ResearchEntry> discoveryKey, Rarity rarity, int limit) {
        super(ResourceUtils.loc(tag), new ResearchRequirement(new ResearchEntryKey(discoveryKey)), rarity, true, limit);
    }
    
    @Override
    public RuneType getType() {
        return RuneType.POWER;
    }
}
