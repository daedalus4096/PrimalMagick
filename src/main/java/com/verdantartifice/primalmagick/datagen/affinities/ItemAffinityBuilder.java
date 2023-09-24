package com.verdantartifice.primalmagick.datagen.affinities;

import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.affinities.AffinityType;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemAffinityBuilder {
    protected final ResourceLocation targetId;
    protected ResourceLocation baseId;
    protected boolean hasSetValues = false;
    protected SourceList.Builder setValues = SourceList.builder();
    protected SourceList.Builder addValues = SourceList.builder();
    protected SourceList.Builder removeValues = SourceList.builder();

    protected ItemAffinityBuilder(@Nonnull ItemLike item) {
        this.targetId = ForgeRegistries.ITEMS.getKey(item.asItem());
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
        this.baseId = ForgeRegistries.ITEMS.getKey(baseItem.asItem());
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
    
    private void validate(ResourceLocation id) {
        if (this.targetId == null) {
            throw new IllegalStateException("No target item for affinity " + id.toString());
        }
        if (!ForgeRegistries.ITEMS.containsKey(this.targetId)) {
            throw new IllegalStateException("Unknown target item " + this.targetId.toString() + " for affinity " + id.toString());
        }
        
        if (this.baseId != null && this.hasSetValues) {
            throw new IllegalStateException("Both base and set-values defined for affinity " + id.toString());
        } else if (this.baseId != null && !ForgeRegistries.ITEMS.containsKey(this.baseId)) {
            throw new IllegalStateException("Unknown base item " + this.baseId.toString() + " for affinity " + id.toString());
        } else if (this.baseId != null && this.targetId.equals(this.baseId)) {
            throw new IllegalStateException("Target defines itself as a base for affinity " + id.toString());
        } else if (this.baseId == null && !this.hasSetValues) {
            throw new IllegalStateException("Neither base nor set-values defined for affinity " + id.toString());
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
        consumer.accept(new ItemAffinityBuilder.Result(id, this.targetId, this.baseId, this.setValues.build(), this.addValues.build(), this.removeValues.build()));
    }
    
    public static class Result implements IFinishedAffinity {
        protected final ResourceLocation id;
        protected final ResourceLocation targetId;
        protected final ResourceLocation baseId;
        protected final SourceList setValues;
        protected final SourceList addValues;
        protected final SourceList removeValues;
        
        public Result(@Nonnull ResourceLocation id, @Nonnull ResourceLocation targetId, @Nullable ResourceLocation baseId, @Nullable SourceList setValues, @Nullable SourceList addValues, @Nullable SourceList removeValues) {
            this.id = id;
            this.targetId = targetId;
            this.baseId = baseId;
            this.setValues = setValues;
            this.addValues = addValues;
            this.removeValues = removeValues;
        }
        
        @Override
        public AffinityType getType() {
            return AffinityType.ITEM;
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("target", this.targetId.toString());
            if (this.baseId != null) {
                json.addProperty("base", this.baseId.toString());
            } else if (this.setValues != null) {
                json.add("set", this.setValues.serializeJson());
            }
            if (this.addValues != null && !this.addValues.isEmpty()) {
                json.add("add", this.addValues.serializeJson());
            }
            if (this.removeValues != null && !this.removeValues.isEmpty()) {
                json.add("remove", this.removeValues.serializeJson());
            }
        }
    }
}
