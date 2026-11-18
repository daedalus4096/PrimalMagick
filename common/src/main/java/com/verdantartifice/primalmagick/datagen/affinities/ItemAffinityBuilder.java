package com.verdantartifice.primalmagick.datagen.affinities;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.verdantartifice.primalmagick.common.affinities.AbstractAffinity;
import com.verdantartifice.primalmagick.common.affinities.AffinityType;
import com.verdantartifice.primalmagick.common.affinities.AffinityTypesPM;
import com.verdantartifice.primalmagick.common.affinities.ItemAffinity;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

public class ItemAffinityBuilder {
    protected static final Logger LOGGER = LogManager.getLogger();

    protected final ResourceKey<Item> targetId;
    protected ResourceKey<Item> baseId;
    protected boolean hasSetValues = false;
    protected SourceList.Builder setValues = SourceList.builder();
    protected SourceList.Builder addValues = SourceList.builder();
    protected SourceList.Builder removeValues = SourceList.builder();

    protected ItemAffinityBuilder(@Nonnull ItemLike item) {
        this.targetId = Services.ITEMS_REGISTRY.getResourceKey(item.asItem()).orElseThrow();
    }
    
    public static ItemAffinityBuilder itemAffinity(@Nonnull ItemLike item) {
        return new ItemAffinityBuilder(item);
    }
    
    public static ItemAffinityBuilder essenceAffinity(@Nonnull EssenceItem essence) {
        return itemAffinity(essence).set(essence.getSource(), essence.getEssenceType().getAffinity());
    }
    
    public static ItemAffinityBuilder emptyAffinity(@Nonnull ItemLike item) {
        return itemAffinity(item).set(SourceList.EMPTY);
    }
    
    public ItemAffinityBuilder base(@Nonnull ItemLike baseItem) {
        this.baseId = Services.ITEMS_REGISTRY.getResourceKey(baseItem.asItem()).orElseThrow();
        return this;
    }
    
    public ItemAffinityBuilder set(@Nonnull SourceList setValues) {
        this.setValues.with(setValues);
        this.hasSetValues = true;
        return this;
    }
    
    public ItemAffinityBuilder set(Source source, int amount) {
        this.setValues.with(source, amount);
        this.hasSetValues = true;
        return this;
    }
    
    public ItemAffinityBuilder add(@Nonnull SourceList addValues) {
        this.addValues.with(addValues);
        return this;
    }
    
    public ItemAffinityBuilder add(Source source, int amount) {
        this.addValues.with(source, amount);
        return this;
    }
    
    public ItemAffinityBuilder remove(@Nonnull SourceList removeValues) {
        this.removeValues.with(removeValues);
        return this;
    }
    
    public ItemAffinityBuilder remove(Source source, int amount) {
        this.removeValues.with(source, amount);
        return this;
    }
    
    private void validate(Identifier id) {
        if (this.targetId == null) {
            throw new IllegalStateException("No target item for affinity " + id);
        }
        if (!Services.ITEMS_REGISTRY.containsKey(this.targetId)) {
            throw new IllegalStateException("Unknown target item " + this.targetId + " for affinity " + id);
        }
        
        if (this.baseId != null && this.hasSetValues) {
            throw new IllegalStateException("Both base and set-values defined for affinity " + id);
        } else if (this.baseId != null && !Services.ITEMS_REGISTRY.containsKey(this.baseId)) {
            throw new IllegalStateException("Unknown base item " + this.baseId + " for affinity " + id);
        } else if (this.targetId.equals(this.baseId)) {
            throw new IllegalStateException("Target defines itself as a base for affinity " + id);
        } else if (this.baseId == null && !this.hasSetValues) {
            throw new IllegalStateException("Neither base nor set-values defined for affinity " + id);
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
        consumer.accept(new ItemAffinityBuilder.Result(id, this.targetId, this.baseId, this.setValues.build(), this.addValues.build(), this.removeValues.build()));
    }
    
    public static class Result implements IFinishedAffinity {
        protected final Identifier id;
        protected final ResourceKey<Item> targetId;
        protected final ResourceKey<Item> baseId;
        protected final SourceList setValues;
        protected final SourceList addValues;
        protected final SourceList removeValues;
        
        public Result(@Nonnull Identifier id, @Nonnull ResourceKey<Item> targetId, @Nullable ResourceKey<Item> baseId, @Nullable SourceList setValues, @Nullable SourceList addValues, @Nullable SourceList removeValues) {
            this.id = id;
            this.targetId = targetId;
            this.baseId = baseId;
            this.setValues = setValues;
            this.addValues = addValues;
            this.removeValues = removeValues;
        }
        
        @Override
        public AffinityType<?> getType() {
            return AffinityTypesPM.ITEM.get();
        }

        @Override
        public Identifier getId() {
            return this.id;
        }

        @Override
        public JsonElement serialize() {
            return AbstractAffinity.dispatchCodec().encodeStart(JsonOps.INSTANCE, this.getFinalAffinity()).resultOrPartial(LOGGER::error).orElseThrow();
        }

        protected ItemAffinity getFinalAffinity() {
            if (this.baseId != null) {
                SourceList add = this.addValues == null ? SourceList.EMPTY : this.addValues;
                SourceList remove = this.removeValues == null ? SourceList.EMPTY : this.removeValues;
                return ItemAffinity.derived(this.targetId, this.baseId, add, remove);
            } else if (this.setValues != null) {
                return ItemAffinity.fixed(this.targetId, this.setValues, Optional.empty());
            } else {
                throw new IllegalStateException("Built affinity is neither fixed nor derived");
            }
        }
    }
}
