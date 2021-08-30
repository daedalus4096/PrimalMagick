package com.verdantartifice.primalmagic.common.sources;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Definition of a list of primal sources and their respective amounts.  Used for anything requiring
 * a specified amount of one or more sources, such as affinities and mana costs.
 * 
 * @author Daedalus4096
 */
public class SourceList implements INBTSerializable<CompoundTag> {
    protected Map<Source, Integer> sources = new HashMap<>();
    
    public SourceList() {}
    
    @Nonnull
    public static SourceList fromNetwork(@Nonnull FriendlyByteBuf buf) {
        SourceList retVal = new SourceList();
        for (Source source : Source.SORTED_SOURCES) {
            retVal.add(source, buf.readVarInt());
        }
        return retVal;
    }
    
    public static void toNetwork(@Nonnull FriendlyByteBuf buf, @Nonnull SourceList sources) {
        for (Source source : Source.SORTED_SOURCES) {
            buf.writeVarInt(sources.getAmount(source));
        }
    }
    
    public int getAmount(@Nullable Source source) {
        // Return zero if the given source is not present in this list
        return this.sources.getOrDefault(source, Integer.valueOf(0)).intValue();
    }
    
    public void clear() {
        this.sources.clear();
    }
    
    @Nonnull
    public SourceList add(@Nullable Source source, int amount) {
        // Add the given amount of the given source to this list.  Do not allow non-positive values;
        // use reduce or remove to subtract.
        if (source != null && amount > 0) {
            this.sources.put(source, amount + this.getAmount(source));
        }
        return this;
    }
    
    @Nonnull
    public SourceList add(@Nullable SourceList list) {
        // Add the given source list to this one
        if (list != null) {
            for (Source source : list.getSources()) {
                this.add(source, list.getAmount(source));
            }
        }
        return this;
    }
    
    public boolean reduce(@Nullable Source source, int amount) {
        // Reduce the given source in this list by the given amount, but only if it has at least that much already
        if (source != null) {
            int newAmount = this.getAmount(source) - amount;
            if (newAmount == 0) {
                // If the new amount of source is exactly zero, remove it from the list entirely
                this.remove(source);
                return true;
            } else if (newAmount > 0) {
                // If it's still positive, save the new value
                this.sources.put(source, Integer.valueOf(newAmount));
                return true;
            }
        }
        
        // Return failure if the given source is null or if this list doesn't have at least as much as the given amount
        return false;
    }
    
    @Nonnull
    public SourceList remove(@Nullable Source source) {
        // Remove all of the given source from this list
        this.sources.remove(source);
        return this;
    }
    
    @Nonnull
    public SourceList remove(@Nullable Source source, int amount) {
        // Reduce the given source in this list by the given amount, even if it doesn't have that much
        if (source != null) {
            int newAmount = this.getAmount(source) - amount;
            if (newAmount <= 0) {
                // If the new amount is non-positive, just remove all of the given source
                this.remove(source);
            } else {
                // Otherwise save the new value
                this.sources.put(source, Integer.valueOf(newAmount));
            }
        }
        return this;
    }
    
    @Nonnull
    public SourceList remove(@Nullable SourceList list) {
        // Reduce the sources in this list by the given amounts, even if it doesn't have that much of them
        if (list != null) {
            for (Source source : list.getSources()) {
                this.remove(source, list.getAmount(source));
            }
        }
        return this;
    }
    
    @Nonnull
    public SourceList merge(@Nullable Source source, int amount) {
        // Set this list to have the given amount of the given source, but only if it's greater than already present
        if (source != null) {
            this.sources.put(source, Integer.valueOf(Math.max(amount, this.getAmount(source))));
        }
        return this;
    }
    
    @Nonnull
    public SourceList merge(@Nullable SourceList list) {
        // Merge the given source list with this one, keeping the greater value of each source
        if (list != null) {
            for (Source source : list.getSources()) {
                this.merge(source, list.getAmount(source));
            }
        }
        return this;
    }
    
    @Nonnull
    public SourceList set(@Nullable Source source, int amount) {
        // Set this list to have the given amount of the given source, even if it's less than what's already there or negative
        if (source != null) {
            this.sources.put(source, Integer.valueOf(amount));
        }
        return this;
    }
    
    @Nonnull
    public SourceList set(@Nullable SourceList list) {
        // Set the contents of the given list into this one.  Keeps any sources in this list that were not present in the given one
        if (list != null) {
            for (Source source : list.getSources()) {
                this.set(source, list.getAmount(source));
            }
        }
        return this;
    }
    
    @Nonnull
    public SourceList multiply(int multiplier) {
        // Scale each value in the source list by the given multiplier
        if (multiplier != 1) {
            for (Source source : this.getSources()) {
                this.sources.put(source, Integer.valueOf(multiplier * this.getAmount(source)));
            }
        }
        return this;
    }
    
    @Nonnull
    public Set<Source> getSources() {
        // Get the sources in this list in arbitrary order
        return Collections.unmodifiableSet(this.sources.keySet());
    }
    
    @Nonnull
    public List<Source> getSourcesSorted() {
        // Get the sources in this list in prescribed order
        return Source.SORTED_SOURCES.stream().filter(s -> (this.getAmount(s) > 0)).collect(Collectors.toList());
    }
    
    public int getSize() {
        return this.sources.size();
    }
    
    public int getManaSize() {
        int retVal = 0;
        for (Source source : this.sources.keySet()) {
            retVal += this.getAmount(source);
        }
        return retVal;
    }
    
    public boolean isEmpty() {
        return this.sources.isEmpty();
    }
    
    @Nonnull
    public SourceList copy() {
        SourceList retVal = new SourceList();
        for (Source source : this.sources.keySet()) {
            retVal.add(source, this.getAmount(source));
        }
        return retVal;
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag tagList = new ListTag();
        for (Source source : this.getSources()) {
            if (source != null) {
                CompoundTag singleTag = new CompoundTag();
                singleTag.putString("key", source.getTag());
                singleTag.putInt("amount", this.getAmount(source));
                tagList.add(singleTag);
            }
        }
        tag.put("Sources", tagList);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.sources.clear();
        ListTag tagList = nbt.getList("Sources", Constants.NBT.TAG_COMPOUND);
        for (int index = 0; index < tagList.size(); index++) {
            CompoundTag singleTag = tagList.getCompound(index);
            if (singleTag.contains("key")) {
                this.add(Source.getSource(singleTag.getString("key")), singleTag.getInt("amount"));
            }
        }
    }
    
    @Nonnull
    public JsonObject serializeJson() {
        JsonObject json = new JsonObject();
        for (Source source : Source.SORTED_SOURCES) {
            int value = this.getAmount(source);
            if (value > 0) {
                json.addProperty(source.getTag(), value);
            }
        }
        return json;
    }
}
