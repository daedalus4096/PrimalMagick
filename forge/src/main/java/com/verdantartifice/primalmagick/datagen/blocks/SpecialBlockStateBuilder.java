package com.verdantartifice.primalmagick.datagen.blocks;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider.ConfiguredModelList;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.IGeneratedBlockState;

/**
 * Builds "special" block state asset files, ones that are not necessarily based on a block.  Very
 * simple for the time being and only support a single model for the whole state.  Should not be
 * manually instantiated, instead use LINK.
 * 
 * @author Daedalus4096
 */
public class SpecialBlockStateBuilder implements IGeneratedBlockState {
    private final ResourceLocation owner;
    private ConfiguredModelList models = null;
    
    public SpecialBlockStateBuilder(ResourceLocation owner) {
        this.owner = owner;
    }

    @Override
    public JsonObject toJson() {
        Preconditions.checkNotNull(this.models, "Blockstate for owner %s does not have a model specified", this.owner);
        JsonObject variants = new JsonObject();
        variants.add("", this.models.toJSON());
        JsonObject main = new JsonObject();
        main.add("variants", variants);
        return main;
    }

    public SpecialBlockStateBuilder setModels(ConfiguredModel... models) {
        Preconditions.checkArgument(this.models == null, "Models have already been set in blockstate for owner %s", this.owner);
        Preconditions.checkArgument(models.length > 0, "Cannot set models to empty array");
        this.models = new ConfiguredModelList(models);
        return this;
    }
}
