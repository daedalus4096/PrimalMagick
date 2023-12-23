package com.verdantartifice.primalmagick.common.sources;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringRepresentable;

/**
 * Definition of a list of primal sources and their respective amounts.  Used for anything requiring
 * a specified amount of one or more sources, such as affinities and mana costs.
 * 
 * @author Daedalus4096
 */
@Immutable
public class SourceList {
    public static final Codec<SourceList> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(Codec.simpleMap(Source.CODEC, Codec.INT, StringRepresentable.keys(Source.SORTED_SOURCES.toArray(Source[]::new))).xmap(Object2IntOpenHashMap::new, Object2IntOpenHashMap::new).fieldOf("sources").forGetter(sl -> sl.sources)).apply(instance, SourceList::new);
    });
    
    public static final SourceList EMPTY = new SourceList();

    protected final Object2IntOpenHashMap<Source> sources;
    
    protected SourceList() {
        this.sources = new Object2IntOpenHashMap<>();
    }
    
    protected SourceList(SourceList other) {
        this.sources = new Object2IntOpenHashMap<>(other.sources);
    }
    
    protected SourceList(Map<Source, Integer> values) {
        this();
        values.entrySet().forEach(entry -> {
            this.setInner(entry.getKey(), entry.getValue().intValue());
        });
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    @Nonnull
    public static SourceList fromNetwork(@Nonnull FriendlyByteBuf buf) {
        SourceList.Builder retVal = SourceList.builder();
        for (Source source : Source.SORTED_SOURCES) {
            retVal.with(source, buf.readVarInt());
        }
        return retVal.build();
    }
    
    public static void toNetwork(@Nonnull FriendlyByteBuf buf, @Nonnull SourceList sources) {
        Source.SORTED_SOURCES.stream().map(sources::getAmount).forEach(buf::writeVarInt);
    }
    
    public int getAmount(@Nullable Source source) {
        // Return zero if the given source is not present in this list
        return this.sources.getOrDefault(source, 0);
    }
    
    /**
     * Returns a copy of this list with the given amount of the given source of mana added to it.
     * Does not allow negative values; use {@link #reduce(Source, int)} or 
     * {@link #remove(Source, int)} to subtract.
     * 
     * @param source the source of mana to be added
     * @param amount the amount of mana to be added
     * @return a new source list with the updated values
     */
    @Nonnull
    public SourceList add(@Nullable Source source, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount may not be negative");
        } else if (source != null && amount > 0) {
            SourceList retVal = new SourceList(this);
            retVal.addInner(source, amount);
            return retVal;
        } else {
            return this;
        }
    }

    /**
     * Returns a copy of this list with the given list's mana added to it.
     * 
     * @param list the source list to be added
     * @return a new source list with the updated values
     */
    @Nonnull
    public SourceList add(@Nullable SourceList list) {
        if (list != null && !list.isEmpty()) {
            SourceList retVal = new SourceList(this);
            for (Source source : list.getSources()) {
                retVal.addInner(source, list.getAmount(source));
            }
            return retVal;
        } else {
            return this;
        }
    }
    
    private void addInner(@Nonnull Source source, int amount) {
        this.setInner(source, this.getAmount(source) + amount);
    }
    
    /**
     * Returns a copy of this list with the given source of mana reduced by the given amount.  If this
     * list does not have that much mana, no modification occurs.  Does not allow negative values; 
     * use {@link #add(Source, int)} to add.
     * 
     * @param source the source of mana to be reduced
     * @param amount the amount of mana to be reduced
     * @return a new source list with the updated values
     */
    public SourceList reduce(@Nullable Source source, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount may not be negative");
        } else if (source != null && amount > 0) {
            // Reduce the given source in this list by the given amount, but only if it has at least that much already
            int newAmount = this.getAmount(source) - amount;
            if (newAmount == 0) {
                // If the new amount of source is exactly zero, remove it from the list entirely
                SourceList retVal = new SourceList(this);
                retVal.sources.removeInt(source);
                return retVal;
            } else if (newAmount > 0) {
                // If it's still positive, save the new value
                SourceList retVal = new SourceList(this);
                retVal.setInner(source, newAmount);
                return retVal;
            }
        }

        // Do nothing if the given source is null, the amount is zero, or this list doesn't have at least as much as the given amount
        return this;
    }
    
    /**
     * Returns a copy of this list with all mana of the given source removed.
     * 
     * @param source the source of mana to be removed
     * @return a new source list with the updated values
     */
    @Nonnull
    public SourceList remove(@Nullable Source source) {
        if (source != null && this.sources.containsKey(source)) {
            SourceList retVal = new SourceList(this);
            retVal.sources.removeInt(source);
            return retVal;
        } else {
            return this;
        }
    }
    
    /**
     * Returns a copy of this list with the given amount of the given source of mana subtracted from
     * it.  Clamps the new list's minimum mana value to zero.  Does not allow negative values; use
     * {@link #add(Source, int)} to add.
     * 
     * @param source the source of mana to be removed
     * @param amount the amount of mana to be removed
     * @return a new source list with the updated values
     */
    @Nonnull
    public SourceList remove(@Nullable Source source, int amount) {
        // Reduce the given source in this list by the given amount, even if it doesn't have that much
        if (amount < 0) {
            throw new IllegalArgumentException("Amount may not be non-positive");
        } else if (source != null && amount > 0) {
            int newAmount = this.getAmount(source) - amount;
            if (newAmount <= 0) {
                // If the new amount is non-positive, just remove all of the given source
                return this.remove(source);
            } else {
                // Otherwise save the new value
                SourceList retVal = new SourceList(this);
                retVal.setInner(source, newAmount);
                return retVal;
            }
        } else {
            return this;
        }
    }
    
    /**
     * Returns a copy of this list with the given source list subtracted from it.  Clamps the new list's
     * mininmum mana values to zero.
     * 
     * @param list the source list to be subtracted
     * @return a new source list with the updated values
     */
    @Nonnull
    public SourceList remove(@Nullable SourceList list) {
        // Reduce the sources in this list by the given amounts, even if it doesn't have that much of them
        if (list != null && !list.isEmpty()) {
            SourceList retVal = new SourceList(this);
            for (Source source : list.getSources()) {
                int newAmount = this.getAmount(source) - list.getAmount(source);
                if (newAmount <= 0) {
                    retVal.sources.removeInt(source);
                } else {
                    retVal.setInner(source, newAmount);
                }
            }
            return retVal;
        } else {
            return this;
        }
    }
    
    /**
     * Returns a copy of this list with the given amount of the given source of mana instead, if and
     * only if that amount is greater than the amount already present in this list.  Otherwise, returns
     * this list.
     * 
     * @param source the source of mana to be merged
     * @param amount the amount of mana to be merged
     * @return a new source list with the updated values
     */
    @Nonnull
    public SourceList merge(@Nullable Source source, int amount) {
        // Set this list to have the given amount of the given source, but only if it's greater than already present
        if (source != null && amount > this.getAmount(source)) {
            SourceList retVal = new SourceList(this);
            retVal.setInner(source, amount);
            return retVal;
        } else {
            return this;
        }
    }
    
    /**
     * Returns a copy of this list merged with the given one, keeping the greater value of each source.
     * 
     * @param list the source list to be merged
     * @return a new source list with the updated values
     */
    @Nonnull
    public SourceList merge(@Nullable SourceList list) {
        // Merge the given source list with this one, keeping the greater value of each source
        if (list != null && !list.isEmpty()) {
            SourceList retVal = new SourceList(this);
            for (Source source : list.getSources()) {
                retVal.setInner(source, Math.max(this.getAmount(source), list.getAmount(source)));
            }
            return retVal;
        } else {
            return this;
        }
    }
    
    /**
     * Returns a copy of this list with the given source of mana set to the given amount, even if it's
     * less than what's already there or negative.
     * 
     * @param source the source of mana to be set
     * @param amount the amount of mana to be set
     * @return a new source list with the updated values
     */
    @Nonnull
    public SourceList set(@Nullable Source source, int amount) {
        if (source != null) {
            SourceList retVal = new SourceList(this);
            retVal.setInner(source, amount);
            return retVal;
        } else {
            return this;
        }
    }
    
    /**
     * Returns a copy of this list with the given source list set into it.  Keeps any sources in this
     * list that were not present in the given one.
     * 
     * @param list the source list to be set
     * @return a new source list with the updated values
     */
    @Nonnull
    public SourceList set(@Nullable SourceList list) {
        // Set the contents of the given list into this one.  Keeps any sources in this list that were not present in the given one
        if (list != null && !list.isEmpty()) {
            SourceList retVal = new SourceList(this);
            for (Source source : list.getSources()) {
                retVal.setInner(source, list.getAmount(source));
            }
            return retVal;
        } else {
            return this;
        }
    }
    
    private void setInner(@Nonnull Source source, int amount) {
        if (amount == 0) {
            // If the new amount of mana is zero, just remove it
            this.sources.removeInt(source);
        } else {
            this.sources.put(source, amount);
        }
    }
    
    /**
     * Returns a copy of this list with each source of mana scaled by the given multiplier.  Does not
     * allow negative values.
     * 
     * @param multiplier the scale factor to be applied
     * @return a new source list with the updated values
     */
    @Nonnull
    public SourceList multiply(int multiplier) {
        // Scale each value in the source list by the given multiplier
        if (multiplier == 0) {
            return SourceList.EMPTY;
        } else if (multiplier == 1) {
            return this;
        } else if (multiplier > 0) {
            SourceList retVal = new SourceList(this);
            for (Source source : this.getSources()) {
                retVal.setInner(source, multiplier * this.getAmount(source));
            }
            return retVal;
        } else {
            throw new IllegalArgumentException("Multiplier may not be negative");
        }
    }
    
    @Nonnull
    public Set<Source> getSources() {
        // Get the sources in this list in arbitrary order
        return Collections.unmodifiableSet(this.sources.keySet());
    }
    
    @Nonnull
    public List<Source> getSourcesSorted() {
        // Get the sources in this list in prescribed order
        return Source.SORTED_SOURCES.stream().filter(this::isPresent).toList();
    }
    
    public int getSize() {
        return this.sources.size();
    }
    
    public int getManaSize() {
        return this.sources.values().intStream().sum();
    }
    
    public boolean isEmpty() {
        return this.sources.isEmpty();
    }
    
    public boolean isPresent(Source source) {
        return this.getAmount(source) > 0;
    }
    
    @Nonnull
    public SourceList copy() {
        return new SourceList(this);
    }
    
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

    /**
     * Deserializes source list data from the given tag into a new object.
     */
    public static SourceList deserializeNBT(CompoundTag nbt) {
        SourceList.Builder retVal = SourceList.builder();
        ListTag tagList = nbt.getList("Sources", Tag.TAG_COMPOUND);
        for (int index = 0; index < tagList.size(); index++) {
            CompoundTag singleTag = tagList.getCompound(index);
            if (singleTag.contains("key")) {
                retVal.with(Source.getSource(singleTag.getString("key")), singleTag.getInt("amount"));
            }
        }
        return retVal.build();
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
    
    public Component getText() {
        List<Source> contents = this.getSourcesSorted();
        MutableComponent output = Component.literal("");
        for (int index = 0; index < contents.size(); index++) {
            Source source = contents.get(index);
            if (index != 0) {
                output = output.append(Component.literal(", "));
            }
            output = output.append(Component.translatable("tooltip.primalmagick.spells.details.mana_cost.piece", this.getAmount(source), source.getNameText()));
        }
        return output;
    }
    
    @Override
    public String toString() {
        List<String> pieces = Source.SORTED_SOURCES.stream().filter(this.sources::containsKey).map(source -> String.join("=", source.getTag(), Integer.toString(this.sources.getInt(source)))).toList();
        return "SourceList[" + String.join(",", pieces) + "]";
    }

    public static class Builder {
        protected final Object2IntOpenHashMap<Source> sources;
        
        protected Builder() {
            this.sources = new Object2IntOpenHashMap<>();
        }
        
        public Builder with(Source source, int amount) {
            this.sources.put(source, amount);
            return this;
        }
        
        public Builder with(SourceList list) {
            list.getSources().stream().forEach(source -> this.sources.put(source, list.getAmount(source)));
            return this;
        }
        
        public Builder withEarth(int amount) {
            return this.with(Source.EARTH, amount);
        }
        
        public Builder withSea(int amount) {
            return this.with(Source.SEA, amount);
        }
        
        public Builder withSky(int amount) {
            return this.with(Source.SKY, amount);
        }
        
        public Builder withSun(int amount) {
            return this.with(Source.SUN, amount);
        }
        
        public Builder withMoon(int amount) {
            return this.with(Source.MOON, amount);
        }
        
        public Builder withBlood(int amount) {
            return this.with(Source.BLOOD, amount);
        }
        
        public Builder withInfernal(int amount) {
            return this.with(Source.INFERNAL, amount);
        }
        
        public Builder withVoid(int amount) {
            return this.with(Source.VOID, amount);
        }
        
        public Builder withHallowed(int amount) {
            return this.with(Source.HALLOWED, amount);
        }
        
        public SourceList build() {
            return new SourceList(this.sources);
        }
    }
}
