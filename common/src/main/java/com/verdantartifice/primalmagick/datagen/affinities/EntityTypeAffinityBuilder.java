package com.verdantartifice.primalmagick.datagen.affinities;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.verdantartifice.primalmagick.common.affinities.AbstractAffinity;
import com.verdantartifice.primalmagick.common.affinities.AffinityType;
import com.verdantartifice.primalmagick.common.affinities.AffinityTypesPM;
import com.verdantartifice.primalmagick.common.affinities.EntityTypeAffinity;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class EntityTypeAffinityBuilder {
    protected static final Logger LOGGER = LogManager.getLogger();

    protected final ResourceKey<EntityType<?>> targetId;
    protected SourceList.Builder values = SourceList.builder();

    protected EntityTypeAffinityBuilder(@Nonnull EntityType<?> target) {
        this.targetId = Services.ENTITY_TYPES_REGISTRY.getResourceKey(target).orElseThrow();
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
    
    private void validate(Identifier id) {
        if (this.targetId == null) {
            throw new IllegalStateException("No target entity type for affinity " + id);
        }
        if (!Services.ENTITY_TYPES_REGISTRY.containsKey(this.targetId)) {
            throw new IllegalStateException("Unknown target entity type " + this.targetId + " for affinity " + id);
        }
    }

    public void build(Consumer<IFinishedAffinity> consumer) {
        this.build(consumer, this.targetId.identifier());
    }
    
    public void build(Consumer<IFinishedAffinity> consumer, String name) {
        this.build(consumer, Identifier.parse(name));
    }

    public void build(Consumer<IFinishedAffinity> consumer, Identifier id) {
        this.validate(id);
        consumer.accept(new EntityTypeAffinityBuilder.Result(id, this.targetId, this.values.build()));
    }
    
    public static class Result implements IFinishedAffinity {
        protected final Identifier id;
        protected final ResourceKey<EntityType<?>> targetId;
        protected final SourceList values;
        
        public Result(@Nonnull Identifier id, @Nonnull ResourceKey<EntityType<?>> targetId, @Nullable SourceList values) {
            this.id = id;
            this.targetId = targetId;
            this.values = values == null ? SourceList.EMPTY : values;
        }

        @Override
        public AffinityType<?> getType() {
            return AffinityTypesPM.ENTITY_TYPE.get();
        }

        @Override
        public Identifier getId() {
            return this.id;
        }

        @Override
        public JsonElement serialize() {
            return AbstractAffinity.dispatchCodec().encodeStart(JsonOps.INSTANCE, new EntityTypeAffinity(this.targetId, this.values)).resultOrPartial(LOGGER::error).orElseThrow();
        }
    }
}
