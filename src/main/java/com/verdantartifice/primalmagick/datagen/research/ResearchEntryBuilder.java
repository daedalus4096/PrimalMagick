package com.verdantartifice.primalmagick.datagen.research;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.ResearchName;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public class ResearchEntryBuilder {
    protected final SimpleResearchKey key;
    protected final String nameTranslationKey;
    protected final String disciplineName;
    protected final List<String> finales = new ArrayList<>();
    protected final List<SimpleResearchKey> parents = new ArrayList<>();
    protected final List<IFinishedResearchStage> stages = new ArrayList<>();
    protected final List<IFinishedResearchAddendum> addenda = new ArrayList<>();
    protected boolean hidden;
    protected boolean finaleExempt;
    protected ResearchEntry.Icon icon;
    
    protected ResearchEntryBuilder(@Nonnull String modId, @Nonnull SimpleResearchKey key, @Nonnull String discipline) {
        this.key = key.stripStage();
        this.nameTranslationKey = String.join(".", "research", modId.toLowerCase(), this.key.getRootKey().toLowerCase(), "title");
        this.disciplineName = discipline;
        this.hidden = false;
        this.finaleExempt = false;
    }
    
    public static ResearchEntryBuilder entry(@Nonnull ResearchName name, @Nonnull String discipline) {
        return new ResearchEntryBuilder(PrimalMagick.MODID, name.simpleKey(), discipline);
    }
    
    public static ResearchEntryBuilder entry(@Nonnull String name, @Nonnull String discipline) {
        return entry(ResearchNames.find(name).orElseThrow(), discipline);
    }
    
    public ResearchEntryBuilder parent(SimpleResearchKey parent) {
        this.parents.add(parent);
        return this;
    }
    
    public ResearchEntryBuilder parent(ResearchName parent) {
        this.parents.add(parent.simpleKey());
        return this;
    }
    
    public ResearchEntryBuilder parent(String parent) {
        return this.parent(ResearchNames.find(parent).orElseThrow());
    }
    
    public ResearchEntryBuilder hidden() {
        this.hidden = true;
        return this;
    }
    
    public ResearchEntryBuilder finaleExempt() {
        this.finaleExempt = true;
        return this;
    }
    
    public ResearchEntryBuilder icon(ItemLike item) {
        this.icon = ResearchEntry.Icon.of(item);
        return this;
    }
    
    public ResearchEntryBuilder icon(ResourceLocation loc) {
        this.icon = ResearchEntry.Icon.of(loc);
        return this;
    }
    
    public ResearchEntryBuilder icon(String path) {
        return icon(PrimalMagick.resource(path));
    }
    
    public ResearchEntryBuilder finale(String discipline) {
        this.finales.add(discipline);
        return this;
    }
    
    public ResearchEntryBuilder stage(IFinishedResearchStage stage) {
        int index = this.stages.size() + 1;
        this.stages.add(stage.setEntryKey(this.key.getRootKey()).setStageIndex(index));
        return this;
    }
    
    public ResearchEntryBuilder addendum(IFinishedResearchAddendum addendum) {
        int index = this.addenda.size() + 1;
        this.addenda.add(addendum.setEntryKey(this.key.getRootKey()).setAddendumIndex(index));
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
        this.build(consumer, PrimalMagick.resource(this.key.toString().toLowerCase()));
    }
    
    public void build(Consumer<IFinishedResearchEntry> consumer, String name) {
        this.build(consumer, new ResourceLocation(name));
    }
    
    public void build(Consumer<IFinishedResearchEntry> consumer, ResourceLocation id) {
        this.validate(id);
        consumer.accept(new ResearchEntryBuilder.Result(id, this.key, this.nameTranslationKey, this.disciplineName, this.hidden, this.finaleExempt, this.icon, this.finales, 
                this.parents, this.stages, this.addenda));
    }
    
    public static class Result implements IFinishedResearchEntry {
        protected final ResourceLocation id;
        protected final SimpleResearchKey key;
        protected final String name;
        protected final String discipline;
        protected final boolean hidden;
        protected final boolean finaleExempt;
        protected final ResearchEntry.Icon icon;
        protected final List<String> finales;
        protected final List<SimpleResearchKey> parents;
        protected final List<IFinishedResearchStage> stages;
        protected final List<IFinishedResearchAddendum> addenda;
        
        public Result(@Nonnull ResourceLocation id, @Nonnull SimpleResearchKey key, @Nonnull String name, @Nonnull String discipline, boolean hidden, boolean finaleExempt, @Nullable ResearchEntry.Icon icon, 
                @Nonnull List<String> finales, @Nonnull List<SimpleResearchKey> parents, @Nonnull List<IFinishedResearchStage> stages, @Nonnull List<IFinishedResearchAddendum> addenda) {
            this.id = id;
            this.key = key;
            this.name = name;
            this.discipline = discipline;
            this.hidden = hidden;
            this.finaleExempt = finaleExempt;
            this.icon = icon;
            this.finales = finales;
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
            
            if (this.hidden) {
                json.addProperty("hidden", this.hidden);
            }
            
            if (this.finaleExempt) {
                json.addProperty("finaleExempt", this.finaleExempt);
            }
            
            if (this.icon != null) {
                json.add("icon", this.icon.toJson());
            }
            
            if (!this.finales.isEmpty()) {
                JsonArray finalesArray = new JsonArray();
                for (String discipline : this.finales) {
                    finalesArray.add(discipline);
                }
                json.add("finales", finalesArray);
            }
            
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
