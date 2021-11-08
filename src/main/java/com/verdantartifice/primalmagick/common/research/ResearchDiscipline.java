package com.verdantartifice.primalmagick.common.research;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.stats.Stat;

import net.minecraft.resources.ResourceLocation;

/**
 * Definition of a research discipline.  A discipline represents a collection of research entries of
 * similar type (e.g. alchemy).  They are unlocked, yielding access to their entries, by completing
 * other research entries.
 * 
 * @author Daedalus4096
 */
public class ResearchDiscipline {
    protected final String key;
    protected final CompoundResearchKey unlockResearchKey;
    protected final ResourceLocation iconLocation;
    protected final Stat craftingStat;
    protected final Map<SimpleResearchKey, ResearchEntry> entries = new HashMap<>();
    
    protected ResearchDiscipline(@Nonnull String key, @Nullable CompoundResearchKey unlockResearchKey, @Nonnull ResourceLocation icon, @Nullable Stat craftingStat) {
        this.key = key;
        this.unlockResearchKey = unlockResearchKey;
        this.iconLocation = icon;
        this.craftingStat = craftingStat;
    }
    
    @Nullable
    public static ResearchDiscipline create(@Nullable String key, @Nullable CompoundResearchKey unlockResearchKey, @Nullable ResourceLocation icon, @Nullable Stat craftingStat) {
        return (key == null || icon == null) ? null : new ResearchDiscipline(key, unlockResearchKey, icon, craftingStat);
    }
    
    @Nonnull
    public String getKey() {
        return this.key;
    }
    
    @Nonnull
    public String getNameTranslationKey() {
        return "primalmagick.research_discipline." + this.key;
    }
    
    @Nullable
    public CompoundResearchKey getUnlockResearchKey() {
        return this.unlockResearchKey;
    }
    
    @Nonnull
    public ResourceLocation getIconLocation() {
        return this.iconLocation;
    }
    
    @Nullable
    public Stat getCraftingStat() {
        return this.craftingStat;
    }
    
    @Nullable
    public ResearchEntry getEntry(SimpleResearchKey key) {
        return this.entries.get(key);
    }
    
    @Nonnull
    public Collection<ResearchEntry> getEntries() {
        return Collections.unmodifiableCollection(this.entries.values());
    }
    
    public boolean addEntry(@Nullable ResearchEntry entry) {
        if (entry == null || this.entries.containsKey(entry.getKey())) {
            // Don't allow null or duplicate entries in a discipline
            return false;
        } else {
            this.entries.put(entry.getKey(), entry);
            return true;
        }
    }
    
    void clearEntries() {
        this.entries.clear();
    }
}
