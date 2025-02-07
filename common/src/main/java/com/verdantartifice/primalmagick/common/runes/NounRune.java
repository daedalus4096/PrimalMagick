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
    
    public NounRune(@Nonnull String tag, @Nonnull ResourceKey<ResearchEntry> discoveryKey) {
        super(ResourceUtils.loc(tag), new ResearchRequirement(new ResearchEntryKey(discoveryKey)), Rarity.COMMON, false, -1);
    }
    
    @Override
    public RuneType getType() {
        return RuneType.NOUN;
    }
}
