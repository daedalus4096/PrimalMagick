package com.verdantartifice.primalmagick.datagen.affinities;

import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.affinities.AffinityType;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityTypeAffinityBuilder {
    protected final ResourceLocation targetId;
    protected SourceList.Builder values = SourceList.builder();

    protected EntityTypeAffinityBuilder(@Nonnull EntityType<?> target) {
        this.targetId = ForgeRegistries.ENTITY_TYPES.getKey(target);
    }
    
    public static EntityTypeAffinityBuilder entityAffinity(@Nonnull EntityType<?> target) {
        return new EntityTypeAffinityBuilder(target);
    }
    
    public EntityTypeAffinityBuilder values(SourceList values) {
        this.values.with(values);
        return this;
    }
    
    public EntityTypeAffinityBuilder value(Source source, int amount) {
        this.values.with(source, amount);
        return this;
    }
    
    private void validate(ResourceLocation id) {
        if (this.targetId == null) {
            throw new IllegalStateException("No target entity type for affinity " + id.toString());
        }
        if (!ForgeRegistries.ENTITY_TYPES.containsKey(this.targetId)) {
            throw new IllegalStateException("Unknown target entity type " + this.targetId.toString() + " for affinity " + id.toString());
        }
    }

    public void build(Consumer<IFinishedAffinity> consumer) {
        this.build(consumer, this.targetId);
    }
    
    public void build(Consumer<IFinishedAffinity> consumer, String name) {
        this.build(consumer, new ResourceLocation(name));
    }

    public void build(Consumer<IFinishedAffinity> consumer, ResourceLocation id) {
        this.validate(id);
        consumer.accept(new EntityTypeAffinityBuilder.Result(id, this.targetId, this.values.build()));
    }
    
    public static class Result implements IFinishedAffinity {
        protected final ResourceLocation id;
        protected final ResourceLocation targetId;
        protected final SourceList values;
        
        public Result(@Nonnull ResourceLocation id, @Nonnull ResourceLocation targetId, @Nullable SourceList values) {
            this.id = id;
            this.targetId = targetId;
            this.values = values;
        }

        @Override
        public AffinityType getType() {
            return AffinityType.ENTITY_TYPE;
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("target", this.targetId.toString());
            if (this.values != null && !this.values.isEmpty()) {
                json.add("values", this.values.serializeJson());
            }
        }
    }
}
