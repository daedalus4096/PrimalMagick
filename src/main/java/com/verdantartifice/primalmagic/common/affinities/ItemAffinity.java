package com.verdantartifice.primalmagic.common.affinities;

import java.util.function.BiFunction;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.util.JsonUtils;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemAffinity extends AbstractAffinity {
    public static final Serializer SERIALIZER = new Serializer();

    protected ResourceLocation baseEntryId;
    protected IAffinity baseEntry;
    protected SourceList setValues;
    protected SourceList addValues;
    protected SourceList removeValues;
    
    protected ItemAffinity(@Nonnull ResourceLocation target, @Nonnull BiFunction<AffinityType, ResourceLocation, IAffinity> lookupFunc) {
        super(target, lookupFunc);
    }
    
    public ItemAffinity(@Nonnull ResourceLocation target, @Nonnull SourceList values) {
        super(target, AbstractAffinity.DUMMY);
        this.setValues = values;
    }

    @Override
    public AffinityType getType() {
        return AffinityType.ITEM;
    }

    @Override
    public IAffinitySerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    protected SourceList calculateTotal() {
        if (this.setValues != null) {
            return this.setValues;
        } else if (this.baseEntryId != null) {
            if (this.baseEntry == null) {
                this.baseEntry = this.lookupFunc.apply(this.getType(), this.baseEntryId);
            }
            SourceList retVal = this.baseEntry.getTotal();
            if (retVal != null) {
                if (this.addValues != null) {
                    retVal = retVal.add(this.addValues);
                }
                if (this.removeValues != null) {
                    retVal = retVal.remove(this.removeValues);
                }
            }
            return retVal;
        } else {
            throw new IllegalStateException("Item affinity has neither set values nor a base entry");
        }
    }
    
    public static class Serializer implements IAffinitySerializer<ItemAffinity> {
        @Override
        public ItemAffinity read(ResourceLocation affinityId, JsonObject json) {
            String target = json.getAsJsonPrimitive("target").getAsString();
            if (target == null) {
                throw new JsonSyntaxException("Illegal affinity target in affinity JSON for " + affinityId.toString());
            }
            
            ResourceLocation targetId = new ResourceLocation(target);
            if (!ForgeRegistries.ITEMS.containsKey(targetId)) {
                throw new JsonSyntaxException("Unknown target item " + target + " in affinity JSON for " + affinityId.toString());
            }
            
            ItemAffinity entry = new ItemAffinity(targetId, AffinityController.getInstance()::getAffinity);
            if (json.has("set") && json.has("base")) {
                throw new JsonParseException("Affinity entry may not have both set and base attributes");
            } else if (json.has("set")) {
                entry.setValues = JsonUtils.toSourceList(json.get("set").getAsJsonObject());
            } else if (json.has("base")) {
                entry.baseEntryId = new ResourceLocation(json.getAsJsonPrimitive("base").getAsString());
                if (!ForgeRegistries.ITEMS.containsKey(entry.baseEntryId)) {
                    throw new JsonSyntaxException("Unknown base item " + target + " in affinity JSON for " + affinityId.toString());
                }
                if (json.has("add")) {
                    entry.addValues = JsonUtils.toSourceList(json.get("add").getAsJsonObject());
                }
                if (json.has("remove")) {
                    entry.removeValues = JsonUtils.toSourceList(json.get("remove").getAsJsonObject());
                }
            } else {
                throw new JsonParseException("Affinity entry must have either set or base attributes");
            }

            return entry;
        }
    }
}
