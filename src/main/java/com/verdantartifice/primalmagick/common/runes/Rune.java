package com.verdantartifice.primalmagick.common.runes;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.ResearchName;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

/**
 * Base definition of a rune data structure.  Runes come in different types and can be combined on items
 * to grant enchantment effects.  Also contains a static registry of all types of runes in the mod.
 * 
 * @author Daedalus4096
 */
public abstract class Rune {
    protected static final Map<ResourceLocation, Rune> REGISTRY = new HashMap<>();
    
    public static final SourceRune EARTH = new SourceRune("earth", ResearchNames.RUNE_EARTH, Source.EARTH);
    public static final SourceRune SEA = new SourceRune("sea", ResearchNames.RUNE_SEA, Source.SEA);
    public static final SourceRune SKY = new SourceRune("sky", ResearchNames.RUNE_SKY, Source.SKY);
    public static final SourceRune SUN = new SourceRune("sun", ResearchNames.RUNE_SUN, Source.SUN);
    public static final SourceRune MOON = new SourceRune("moon", ResearchNames.RUNE_MOON, Source.MOON);
    public static final SourceRune BLOOD = new SourceRune("blood", ResearchNames.RUNE_BLOOD, Source.BLOOD);
    public static final SourceRune INFERNAL = new SourceRune("infernal", ResearchNames.RUNE_INFERNAL, Source.INFERNAL);
    public static final SourceRune VOID = new SourceRune("void", ResearchNames.RUNE_VOID, Source.VOID);
    public static final SourceRune HALLOWED = new SourceRune("hallowed", ResearchNames.RUNE_HALLOWED, Source.HALLOWED);
    public static final VerbRune ABSORB = new VerbRune("absorb", ResearchNames.RUNE_ABSORB);
    public static final VerbRune DISPEL = new VerbRune("dispel", ResearchNames.RUNE_DISPEL);
    public static final VerbRune PROJECT = new VerbRune("project", ResearchNames.RUNE_PROJECT);
    public static final VerbRune PROTECT = new VerbRune("protect", ResearchNames.RUNE_PROTECT);
    public static final VerbRune SUMMON = new VerbRune("summon", ResearchNames.RUNE_SUMMON);
    public static final NounRune AREA = new NounRune("area", ResearchNames.RUNE_AREA);
    public static final NounRune CREATURE = new NounRune("creature", ResearchNames.RUNE_CREATURE);
    public static final NounRune ITEM = new NounRune("item", ResearchNames.RUNE_ITEM);
    public static final NounRune SELF = new NounRune("self", ResearchNames.RUNE_SELF);
    public static final PowerRune INSIGHT = new PowerRune("insight", ResearchNames.RUNE_INSIGHT, Rarity.UNCOMMON, 1);
    public static final PowerRune POWER = new PowerRune("power", ResearchNames.RUNE_POWER, Rarity.RARE, 1);
    public static final PowerRune GRACE = new PowerRune("grace", ResearchNames.RUNE_GRACE, Rarity.EPIC, -1);
    
    protected final ResourceLocation id;
    protected final Supplier<SimpleResearchKey> discoveryKey;
    protected final Rarity rarity;
    protected final boolean glint;
    protected final int limit;
    
    public Rune(@Nonnull String tag, @Nonnull Supplier<ResearchName> discoveryKey, @Nonnull Rarity rarity, boolean glint, int limit) {
        this(PrimalMagick.resource(tag), ResearchNames.simpleKey(discoveryKey), rarity, glint, limit);
    }
    
    public Rune(@Nonnull ResourceLocation id, @Nonnull Supplier<SimpleResearchKey> discoveryKey, @Nonnull Rarity rarity, boolean glint, int limit) {
        if (REGISTRY.containsKey(id)) {
            // Don't allow a given rune to be registered more than once
            throw new IllegalArgumentException("Rune " + id.toString() + " already registered!");
        }
        this.id = id;
        this.discoveryKey = discoveryKey;
        this.rarity = rarity;
        this.glint = glint;
        this.limit = limit;
        REGISTRY.put(id, this);
    }
    
    @Nonnull
    public ResourceLocation getId() {
        return this.id;
    }
    
    @Nonnull
    public SimpleResearchKey getDiscoveryKey() {
        return this.discoveryKey.get();
    }
    
    @Nonnull
    public Rarity getRarity() {
        return this.rarity;
    }
    
    public boolean hasGlint() {
        return this.glint;
    }
    
    public boolean hasLimit() {
        return this.limit > -1;
    }
    
    public int getLimit() {
        return this.limit;
    }
    
    @Nonnull
    public abstract RuneType getType();
    
    @Nonnull
    public static Collection<Rune> getAllRunes() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }
    
    @Nullable
    public static Rune getRune(@Nonnull ResourceLocation tag) {
        return REGISTRY.get(tag);
    }
}
