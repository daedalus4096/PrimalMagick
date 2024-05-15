package com.verdantartifice.primalmagick.common.runes;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.research.ResearchName;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

/**
 * Definition of a source rune data structure.  One of three rune types needed to imbue an enchantment.
 * 
 * @author Daedalus4096
 */
public class SourceRune extends Rune {
    public static final Codec<SourceRune> CODEC = ResourceLocation.CODEC.<SourceRune>xmap(loc -> {
        if (Rune.getRune(loc) instanceof SourceRune source) {
            return source;
        } else {
            throw new IllegalArgumentException("Unknown source rune: " + loc.toString());
        }
    }, s -> s.getId());
    
    protected final Source source;
    
    public SourceRune(@Nonnull String tag, @Nonnull Supplier<ResearchName> discoveryKey, @Nonnull Source source) {
        super(tag, discoveryKey, Rarity.COMMON, false, -1);
        this.source = source;
    }
    
    @Override
    public RuneType getType() {
        return RuneType.SOURCE;
    }
    
    public Source getSource() {
        return this.source;
    }
}
