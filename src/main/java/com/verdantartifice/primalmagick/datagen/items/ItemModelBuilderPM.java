package com.verdantartifice.primalmagick.datagen.items;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Builder for item models, adds the ability to set textures with armor trim suffixes 
 * via {@link #palattedTexture(String, ResourceLocation, String)}.  Duplicates override functionality
 * from ItemModelBuilder.
 * 
 * @author Daedalus4096
 */
public class ItemModelBuilderPM extends ModelBuilder<ItemModelBuilderPM> {
    protected List<OverrideBuilder> overrides = new ArrayList<>();

    public ItemModelBuilderPM(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
        super(outputLocation, existingFileHelper);
    }
    
    public ItemModelBuilderPM palattedTexture(String key, ResourceLocation baseTexture, String palatteSuffix) {
        Preconditions.checkNotNull(key, "Key must not be null");
        Preconditions.checkNotNull(baseTexture, "Base texture must not be null");
        // TODO Check that base texture exists if ModelProvider.TEXTURE ever becomes accessible
        this.textures.put(key, baseTexture.withSuffix("_" + palatteSuffix).toString());
        return this;
    }

    public OverrideBuilder override() {
        OverrideBuilder ret = new OverrideBuilder();
        this.overrides.add(ret);
        return ret;
    }

    @Override
    public JsonObject toJson() {
        JsonObject root = super.toJson();
        if (!this.overrides.isEmpty()) {
            JsonArray overridesJson = new JsonArray();
            this.overrides.stream().map(OverrideBuilder::toJson).forEach(overridesJson::add);
            root.add("overrides", overridesJson);
        }
        return root;
    }

    public class OverrideBuilder {
        private ModelFile model;
        private final Map<ResourceLocation, Float> predicates = new LinkedHashMap<>();

        public OverrideBuilder model(ModelFile model) {
            this.model = model;
            model.assertExistence();
            return this;
        }

        public OverrideBuilder predicate(ResourceLocation key, float value) {
            this.predicates.put(key, value);
            return this;
        }

        public ItemModelBuilderPM end() { return ItemModelBuilderPM.this; }

        JsonObject toJson() {
            JsonObject ret = new JsonObject();
            JsonObject predicatesJson = new JsonObject();
            this.predicates.forEach((key, val) -> predicatesJson.addProperty(key.toString(), val));
            ret.add("predicate", predicatesJson);
            ret.addProperty("model", this.model.getLocation().toString());
            return ret;
        }
    }
}
