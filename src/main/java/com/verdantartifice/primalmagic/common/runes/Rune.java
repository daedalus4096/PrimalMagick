package com.verdantartifice.primalmagic.common.runes;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;

/**
 * Base definition of a rune data structure.  Runes come in different types and can be combined on items
 * to grant enchantment effects.  Also contains a static registry of all types of runes in the mod.
 * 
 * @author Daedalus4096
 */
public abstract class Rune {
    protected static final Map<ResourceLocation, Rune> REGISTRY = new HashMap<>();
    
    public static final SourceRune EARTH = new SourceRune("earth");
    public static final SourceRune SEA = new SourceRune("sea");
    public static final SourceRune SKY = new SourceRune("sky");
    public static final SourceRune SUN = new SourceRune("sun");
    public static final SourceRune MOON = new SourceRune("moon");
    public static final SourceRune BLOOD = new SourceRune("blood");
    public static final SourceRune INFERNAL = new SourceRune("infernal");
    public static final SourceRune VOID = new SourceRune("void");
    public static final SourceRune HALLOWED = new SourceRune("hallowed");
    public static final VerbRune ABSORB = new VerbRune("absorb");
    public static final VerbRune DISPEL = new VerbRune("dispel");
    public static final VerbRune PROJECT = new VerbRune("project");
    public static final VerbRune PROTECT = new VerbRune("protect");
    public static final VerbRune SUMMON = new VerbRune("summon");
    public static final NounRune AREA = new NounRune("area");
    public static final NounRune CREATURE = new NounRune("creature");
    public static final NounRune ITEM = new NounRune("item");
    public static final NounRune SELF = new NounRune("self");
    public static final PowerRune POWER = new PowerRune("power");
    
    protected final ResourceLocation id;
    protected final Rarity rarity;
    protected final boolean glint;
    
    public Rune(@Nonnull String tag, @Nonnull Rarity rarity, boolean glint) {
        this(new ResourceLocation(PrimalMagic.MODID, tag), rarity, glint);
    }
    
    public Rune(@Nonnull ResourceLocation id, @Nonnull Rarity rarity, boolean glint) {
        if (REGISTRY.containsKey(id)) {
            // Don't allow a given rune to be registered more than once
            throw new IllegalArgumentException("Rune " + id.toString() + " already registered!");
        }
        this.id = id;
        this.rarity = rarity;
        this.glint = glint;
        REGISTRY.put(id, this);
    }
    
    @Nonnull
    public ResourceLocation getId() {
        return this.id;
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
        return "tooltip." + this.id.getNamespace() + ".rune." + this.id.getPath();
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
