package com.verdantartifice.primalmagick.common.runes;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.sources.Sources;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Base definition of a rune data structure.  Runes come in different types and can be combined on items
 * to grant enchantment effects.  Also contains a static registry of all types of runes in the mod.
 * 
 * @author Daedalus4096
 */
public abstract class Rune {
    public static final Codec<Rune> CODEC = ResourceLocation.CODEC.xmap(Rune::getRune, Rune::getId);
    public static final StreamCodec<ByteBuf, Rune> STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(Rune::getRune, Rune::getId);
    
    protected static final Map<ResourceLocation, Rune> REGISTRY = new HashMap<>();
    
    public static final SourceRune EARTH = new SourceRune("earth", ResearchEntries.RUNE_EARTH, Sources.EARTH);
    public static final SourceRune SEA = new SourceRune("sea", ResearchEntries.RUNE_SEA, Sources.SEA);
    public static final SourceRune SKY = new SourceRune("sky", ResearchEntries.RUNE_SKY, Sources.SKY);
    public static final SourceRune SUN = new SourceRune("sun", ResearchEntries.RUNE_SUN, Sources.SUN);
    public static final SourceRune MOON = new SourceRune("moon", ResearchEntries.RUNE_MOON, Sources.MOON);
    public static final SourceRune BLOOD = new SourceRune("blood", ResearchEntries.RUNE_BLOOD, Sources.BLOOD);
    public static final SourceRune INFERNAL = new SourceRune("infernal", ResearchEntries.RUNE_INFERNAL, Sources.INFERNAL);
    public static final SourceRune VOID = new SourceRune("void", ResearchEntries.RUNE_VOID, Sources.VOID);
    public static final SourceRune HALLOWED = new SourceRune("hallowed", ResearchEntries.RUNE_HALLOWED, Sources.HALLOWED);
    public static final VerbRune ABSORB = new VerbRune("absorb", ResearchEntries.RUNE_ABSORB);
    public static final VerbRune DISPEL = new VerbRune("dispel", ResearchEntries.RUNE_DISPEL);
    public static final VerbRune PROJECT = new VerbRune("project", ResearchEntries.RUNE_PROJECT);
    public static final VerbRune PROTECT = new VerbRune("protect", ResearchEntries.RUNE_PROTECT);
    public static final VerbRune SUMMON = new VerbRune("summon", ResearchEntries.RUNE_SUMMON);
    public static final NounRune AREA = new NounRune("area", ResearchEntries.RUNE_AREA);
    public static final NounRune CREATURE = new NounRune("creature", ResearchEntries.RUNE_CREATURE);
    public static final NounRune ITEM = new NounRune("item", ResearchEntries.RUNE_ITEM);
    public static final NounRune SELF = new NounRune("self", ResearchEntries.RUNE_SELF);
    public static final PowerRune INSIGHT = new PowerRune("insight", ResearchEntries.RUNE_INSIGHT, Rarity.UNCOMMON, 1);
    public static final PowerRune POWER = new PowerRune("power", ResearchEntries.RUNE_POWER, Rarity.RARE, 1);
    public static final PowerRune GRACE = new PowerRune("grace", ResearchEntries.RUNE_GRACE, Rarity.EPIC, -1);
    
    protected final ResourceLocation id;
    protected final AbstractRequirement<?> requirement;
    protected final Rarity rarity;
    protected final boolean glint;
    protected final int limit;
    
    public Rune(@Nonnull ResourceLocation id, @Nonnull AbstractRequirement<?> requirement, @Nonnull Rarity rarity, boolean glint, int limit) {
        if (REGISTRY.containsKey(id)) {
            // Don't allow a given rune to be registered more than once
            throw new IllegalArgumentException("Rune " + id.toString() + " already registered!");
        }
        this.id = id;
        this.requirement = requirement;
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
    public AbstractRequirement<?> getRequirement() {
        return this.requirement;
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
