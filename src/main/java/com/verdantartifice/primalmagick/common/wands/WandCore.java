package com.verdantartifice.primalmagick.common.wands;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;

/**
 * Definition of a wand core data structure.  Wand cores determine the number of spells that may be
 * inscribed onto the wand.  It specifies a base number of spell slots, and optionally a bonus slot,
 * into which only a spell of a given source type may be inscribed.  Cores also determine any primal
 * alignments the wand has, causing them to naturally regenerate mana of those sources.  Also contains
 * a static registry of all types of wand cores in the mod.
 * 
 * @author Daedalus4096
 */
public class WandCore implements IWandComponent {
    protected static final Map<String, WandCore> REGISTRY = new HashMap<>();
    
    public static final WandCore HEARTWOOD = new WandCore("heartwood", Rarity.COMMON, 1, null, Collections.emptyList());
    public static final WandCore OBSIDIAN = new WandCore("obsidian", Rarity.COMMON, 1, Source.EARTH, Arrays.asList(Source.EARTH));
    public static final WandCore CORAL = new WandCore("coral", Rarity.COMMON, 1, Source.SEA, Arrays.asList(Source.SEA));
    public static final WandCore BAMBOO = new WandCore("bamboo", Rarity.COMMON, 1, Source.SKY, Arrays.asList(Source.SKY));
    public static final WandCore SUNWOOD = new WandCore("sunwood", Rarity.COMMON, 1, Source.SUN, Arrays.asList(Source.SUN));
    public static final WandCore MOONWOOD = new WandCore("moonwood", Rarity.COMMON, 1, Source.MOON, Arrays.asList(Source.MOON));
    public static final WandCore BONE = new WandCore("bone", Rarity.UNCOMMON, 2, Source.BLOOD, Arrays.asList(Source.BLOOD));
    public static final WandCore BLAZE_ROD = new WandCore("blaze_rod", Rarity.UNCOMMON, 2, Source.INFERNAL, Arrays.asList(Source.INFERNAL));
    public static final WandCore PURPUR = new WandCore("purpur", Rarity.UNCOMMON, 2, Source.VOID, Arrays.asList(Source.VOID));
    public static final WandCore PRIMAL = new WandCore("primal", Rarity.UNCOMMON, 2, null, Arrays.asList(Source.EARTH, Source.SEA, Source.SKY, Source.SUN, Source.MOON));
    public static final WandCore DARK_PRIMAL = new WandCore("dark_primal", Rarity.RARE, 3, null, Arrays.asList(Source.EARTH, Source.SEA, Source.SKY, Source.SUN, Source.MOON, Source.BLOOD, Source.INFERNAL, Source.VOID));
    public static final WandCore PURE_PRIMAL = new WandCore("pure_primal", Rarity.EPIC, 4, null, Arrays.asList(Source.EARTH, Source.SEA, Source.SKY, Source.SUN, Source.MOON, Source.BLOOD, Source.INFERNAL, Source.VOID, Source.HALLOWED));
    
    protected final String tag;                     // Unique identifier for the wand core
    protected final Rarity rarity;                  // The core's rarity, used to color its name and determine completed wand rarity
    protected final int spellSlots;                 // The base number of spell slots offered by the core
    protected final Source bonusSlot;               // The source of the core's bonus spell slot, if any
    protected final List<Source> aligned;           // List of sources to which the core is aligned
    protected final ResourceLocation wandMrlNamespace;  // Resource location of the wand core's model, stored in a blockstate file
    protected final ResourceLocation staffMrlNamespace; // Resource location of the staff core's model, stored in a blockstate file

    public WandCore(@Nonnull String tag, @Nonnull Rarity rarity, int spellSlots, @Nullable Source bonusSlot, @Nonnull List<Source> aligned) {
        this(tag, rarity, spellSlots, bonusSlot, aligned, PrimalMagick.resource(tag + "_wand_core"), PrimalMagick.resource(tag + "_staff_core"));
    }
    
    public WandCore(@Nonnull String tag, @Nonnull Rarity rarity, int spellSlots, @Nullable Source bonusSlot, @Nonnull List<Source> aligned, @Nonnull ResourceLocation wmrln, @Nonnull ResourceLocation smrln) {
        if (REGISTRY.containsKey(tag)) {
            // Don't allow a given core to be registered more than once
            throw new IllegalArgumentException("Wand core " + tag + " already registered!");
        }
        this.tag = tag;
        this.rarity = rarity;
        this.spellSlots = spellSlots;
        this.bonusSlot = bonusSlot;
        this.aligned = aligned;
        this.wandMrlNamespace = wmrln;
        this.staffMrlNamespace = smrln;
        REGISTRY.put(tag, this);
    }
    
    @Override
    public String getTag() {
        return this.tag;
    }
    
    @Override
    public Rarity getRarity() {
        return this.rarity;
    }
    
    @Override
    public Type getComponentType() {
        return IWandComponent.Type.CORE;
    }
    
    public int getSpellSlots() {
        return this.spellSlots;
    }
    
    @Nullable
    public Source getBonusSlot() {
        return this.bonusSlot;
    }
    
    @Nonnull
    public List<Source> getAlignedSources() {
        return Collections.unmodifiableList(this.aligned);
    }
    
    @Nonnull
    public ResourceLocation getWandModelResourceLocationNamespace() {
        return this.wandMrlNamespace;
    }
    
    @Nonnull
    public ResourceLocation getStaffModelResourceLocationNamespace() {
        return this.staffMrlNamespace;
    }
    
    public static Collection<WandCore> getAllWandCores() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }
    
    @Nullable
    public static WandCore getWandCore(@Nullable String tag) {
        return REGISTRY.get(tag);
    }
}
