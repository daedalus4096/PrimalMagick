package com.verdantartifice.primalmagic.common.runes;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.Rarity;

/**
 * Base definition of a rune data structure.  Runes come in different types and can be combined on items
 * to grant enchantment effects.  Also contains a static registry of all types of runes in the mod.
 * 
 * @author Daedalus4096
 */
public abstract class Rune {
    protected static final Map<String, Rune> REGISTRY = new HashMap<>();
    
    public static final SourceRune EARTH = new SourceRune("earth");
    
    protected final String tag;
    protected final Rarity rarity;
    protected final boolean glint;
    
    public Rune(@Nonnull String tag, @Nonnull Rarity rarity, boolean glint) {
        if (REGISTRY.containsKey(tag)) {
            // Don't allow a given rune to be registered more than once
            throw new IllegalArgumentException("Rune " + tag + " already registered!");
        }
        this.tag = tag;
        this.rarity = rarity;
        this.glint = glint;
        REGISTRY.put(tag, this);
    }
    
    @Nonnull
    public String getTag() {
        return this.tag;
    }
    
    @Nonnull
    public Rarity getRarity() {
        return this.rarity;
    }
    
    public boolean hasGlint() {
        return this.glint;
    }
    
    @Nonnull
    public String getTooltipTranslationKey() {
        return "tooltip.primalmagic.rune." + this.tag;
    }
    
    @Nonnull
    public abstract RuneType getType();
    
    @Nonnull
    public static Collection<Rune> getAllRunes() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }
    
    @Nullable
    public static Rune getRune(@Nonnull String tag) {
        return REGISTRY.get(tag);
    }
}
