package com.verdantartifice.primalmagic.datagen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.research.ResearchDisciplines;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.util.ResourceLocation;

public class ResearchEntryBuilder {
    protected final SimpleResearchKey key;
    protected final String nameTranslationKey;
    protected final String disciplineName;
    protected final List<SimpleResearchKey> parents = new ArrayList<>();
    protected final List<IFinishedResearchStage> stages = new ArrayList<>();
    protected final List<IFinishedResearchAddendum> addenda = new ArrayList<>();
    
    protected ResearchEntryBuilder(@Nonnull SimpleResearchKey key, @Nonnull String name, @Nonnull String discipline) {
        this.key = key.stripStage();
        this.nameTranslationKey = name;
        this.disciplineName = discipline;
    }
    
    public static ResearchEntryBuilder entry(@Nonnull SimpleResearchKey key, @Nonnull String name, @Nonnull String discipline) {
        return new ResearchEntryBuilder(key, name, discipline);
    }
    
    public static ResearchEntryBuilder entry(@Nonnull String keyStr, @Nonnull String name, @Nonnull String discipline) {
        return new ResearchEntryBuilder(SimpleResearchKey.parse(keyStr), name, discipline);
    }
    
    public ResearchEntryBuilder parent(SimpleResearchKey parent) {
        this.parents.add(parent);
        return this;
    }
    
    public ResearchEntryBuilder parent(String parentStr) {
        this.parents.add(SimpleResearchKey.parse(parentStr));
        return this;
    }
    
    public ResearchEntryBuilder stage(IFinishedResearchStage stage) {
        this.stages.add(stage);
        return this;
    }
    
    public ResearchEntryBuilder addendum(IFinishedResearchAddendum addendum) {
        this.addenda.add(addendum);
        return this;
    }
    
    private void validate(ResourceLocation id) {
        if (this.key == null) {
            throw new IllegalStateException("No key for research entry " + id.toString());
        }
        if (this.nameTranslationKey == null) {
            throw new IllegalStateException("No name translation key for research entry " + id.toString());
        }
        if (this.disciplineName == null) {
            throw new IllegalStateException("Invalid discipline for research entry " + id.toString());
        }
        if (this.stages.isEmpty()) {
            throw new IllegalStateException("No stages defined for research entry " + id.toString());
        }
    }
    
    public void build(Consumer<IFinishedResearchEntry> consumer) {
        this.build(consumer, new ResourceLocation(PrimalMagic.MODID, this.key.toString().toLowerCase()));
    }
    
    public void build(Consumer<IFinishedResearchEntry> consumer, String name) {
        this.build(consumer, new ResourceLocation(name));
    }
    
    public void build(Consumer<IFinishedResearchEntry> consumer, ResourceLocation id) {
        this.validate(id);
        consumer.accept(new ResearchEntryBuilder.Result(id, this.key, this.nameTranslationKey, this.disciplineName, this.parents, this.stages, this.addenda));
    }
    
    public static class Result implements IFinishedResearchEntry {
        protected final ResourceLocation id;
        protected final SimpleResearchKey key;
        protected final String name;
        protected final String discipline;
        protected final List<SimpleResearchKey> parents;
        protected final List<IFinishedResearchStage> stages;
        protected final List<IFinishedResearchAddendum> addenda;
        
        public Result(@Nonnull ResourceLocation id, @Nonnull SimpleResearchKey key, @Nonnull String name, @Nonnull String discipline, @Nonnull List<SimpleResearchKey> parents, @Nonnull List<IFinishedResearchStage> stages, @Nonnull List<IFinishedResearchAddendum> addenda) {
            this.id = id;
            this.key = key;
            this.name = name;
            this.discipline = discipline;
            this.parents = parents;
            this.stages = stages;
            this.addenda = addenda;
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("key", this.key.toString());
            json.addProperty("name", this.name);
            json.addProperty("discipline", this.discipline);
            
            JsonArray parentsArray = new JsonArray();
            for (SimpleResearchKey parent : this.parents) {
                parentsArray.add(parent.toString());
            }
            json.add("parents", parentsArray);
            
            JsonArray stagesArray = new JsonArray();
            for (IFinishedResearchStage stage : this.stages) {
                stagesArray.add(stage.getStageJson());
            }
            json.add("stages", stagesArray);
            
            JsonArray addendaArray = new JsonArray();
            for (IFinishedResearchAddendum addendum : this.addenda) {
                addendaArray.add(addendum.getAddendumJson());
            }
            json.add("addenda", addendaArray);
        }
    }
}
