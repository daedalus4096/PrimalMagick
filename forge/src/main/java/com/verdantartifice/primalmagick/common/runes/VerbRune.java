package com.verdantartifice.primalmagick.common.runes;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

/**
 * Definition of a verb rune data structure.  One of three rune types needed to imbue an enchantment.
 * 
 * @author Daedalus4096
 */
public class VerbRune extends Rune {
    public static final Codec<VerbRune> CODEC = ResourceLocation.CODEC.<VerbRune>xmap(loc -> {
        if (Rune.getRune(loc) instanceof VerbRune verb) {
            return verb;
        } else {
            throw new IllegalArgumentException("Unknown verb rune: " + loc.toString());
        }
    }, v -> v.getId());
    
    public VerbRune(@Nonnull String tag, @Nonnull ResourceKey<ResearchEntry> discoveryKey) {
        super(PrimalMagick.resource(tag), new ResearchRequirement(new ResearchEntryKey(discoveryKey)), Rarity.COMMON, false, -1);
    }
    
    @Override
    public RuneType getType() {
        return RuneType.VERB;
    }
}
