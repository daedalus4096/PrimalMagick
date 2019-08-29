package com.verdantartifice.primalmagic.common.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ResearchEntry {
    protected String key;
    protected String categoryKey;
    protected String nameTranslationKey;
    protected Set<String> parentKeys = new HashSet<>();
    protected List<ResearchStage> stages = new ArrayList<>();
    protected List<ResearchAddendum> addenda = new ArrayList<>();
    
    protected ResearchEntry(@Nonnull String key, @Nonnull String categoryKey, @Nonnull String nameTranslationKey) {
        this.key = key;
        this.categoryKey = categoryKey;
        this.nameTranslationKey = nameTranslationKey;
    }
    
    @Nullable
    public static ResearchEntry create(@Nullable String key, @Nullable String categoryKey, @Nullable String nameTranslationKey) {
        if (key == null || categoryKey == null || nameTranslationKey == null) {
            return null;
        } else {
            return new ResearchEntry(key, categoryKey, nameTranslationKey);
        }
    }
    
    @Nonnull
    public String getKey() {
        return this.key;
    }
    
    @Nonnull
    public String getCategoryKey() {
        return this.categoryKey;
    }
    
    @Nonnull
    public String getNameTranslationKey() {
        return this.nameTranslationKey;
    }
    
    @Nonnull
    public Set<String> getParentKeys() {
        return Collections.unmodifiableSet(this.parentKeys);
    }
    
    public boolean addParentKey(@Nullable String key) {
        return (key == null) ? false : this.parentKeys.add(key);
    }
    
    @Nonnull
    public List<ResearchStage> getStages() {
        return Collections.unmodifiableList(this.stages);
    }
    
    public boolean appendStage(@Nullable ResearchStage stage) {
        return (stage == null) ? false : this.stages.add(stage);
    }
    
    @Nonnull
    public List<ResearchAddendum> getAddenda() {
        return Collections.unmodifiableList(this.addenda);
    }
    
    public boolean appendAddendum(@Nullable ResearchAddendum addendum) {
        return (addendum == null) ? false : this.addenda.add(addendum);
    }
}
