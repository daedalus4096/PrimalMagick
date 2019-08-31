package com.verdantartifice.primalmagic.common.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ResearchEntry {
    protected SimpleResearchKey key;
    protected String disciplineKey;
    protected String nameTranslationKey;
    protected CompoundResearchKey parentResearch;
    protected List<ResearchStage> stages = new ArrayList<>();
    protected List<ResearchAddendum> addenda = new ArrayList<>();
    
    protected ResearchEntry(@Nonnull SimpleResearchKey key, @Nonnull String disciplineKey, @Nonnull String nameTranslationKey) {
        this.key = key;
        this.disciplineKey = disciplineKey;
        this.nameTranslationKey = nameTranslationKey;
    }
    
    @Nullable
    public static ResearchEntry create(@Nullable SimpleResearchKey key, @Nullable String disciplineKey, @Nullable String nameTranslationKey) {
        if (key == null || disciplineKey == null || nameTranslationKey == null) {
            return null;
        } else {
            // ResearchEntry main keys should never have a stage
            return new ResearchEntry(key.stripStage(), disciplineKey, nameTranslationKey);
        }
    }
    
    @Nonnull
    public static ResearchEntry parse(JsonObject obj) throws Exception {
        ResearchEntry entry = create(
            SimpleResearchKey.parse(obj.getAsJsonPrimitive("key").getAsString()),
            obj.getAsJsonPrimitive("discipline").getAsString(),
            obj.getAsJsonPrimitive("name").getAsString()
        );
        if (entry == null) {
            throw new Exception("Invalid entry data in research JSON");
        }
        
        if (obj.has("parents")) {
            entry.parentResearch = CompoundResearchKey.parse(obj.get("parents").getAsJsonArray());
        }
        
        for (JsonElement element : obj.get("stages").getAsJsonArray()) {
            entry.stages.add(ResearchStage.parse(element.getAsJsonObject()));
        }
        
        if (obj.has("addenda")) {
            for (JsonElement element : obj.get("addenda").getAsJsonArray()) {
                entry.addenda.add(ResearchAddendum.parse(element.getAsJsonObject()));
            }
        }
        
        return entry;
    }
    
    @Nonnull
    public SimpleResearchKey getKey() {
        return this.key;
    }
    
    @Nonnull
    public String getDisciplineKey() {
        return this.disciplineKey;
    }
    
    @Nonnull
    public String getNameTranslationKey() {
        return this.nameTranslationKey;
    }
    
    @Nullable
    public CompoundResearchKey getParentResearch() {
        return this.parentResearch;
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
