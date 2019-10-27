package com.verdantartifice.primalmagic.common.sources;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public class SourceList {
    protected Map<Source, Integer> sources = new HashMap<>();
    
    public SourceList() {}
    
    public int getAmount(@Nullable Source source) {
        return this.sources.getOrDefault(source, Integer.valueOf(0)).intValue();
    }
    
    @Nonnull
    public SourceList add(@Nullable Source source, int amount) {
        if (source != null && amount > 0) {
            this.sources.put(source, amount + this.getAmount(source));
        }
        return this;
    }
    
    @Nonnull
    public SourceList add(@Nullable SourceList list) {
        if (list != null) {
            for (Source source : list.getSources()) {
                this.add(source, list.getAmount(source));
            }
        }
        return this;
    }
    
    public boolean reduce(@Nullable Source source, int amount) {
        if (source != null) {
            int newAmount = this.getAmount(source) - amount;
            if (newAmount == 0) {
                this.remove(source);
                return true;
            } else if (newAmount > 0) {
                this.sources.put(source, Integer.valueOf(newAmount));
                return true;
            }
        }
        return false;
    }
    
    @Nonnull
    public SourceList remove(@Nullable Source source) {
        this.sources.remove(source);
        return this;
    }
    
    @Nonnull
    public SourceList remove(@Nullable Source source, int amount) {
        if (source != null) {
            int newAmount = this.getAmount(source) - amount;
            if (newAmount <= 0) {
                this.remove(source);
            } else {
                this.sources.put(source, Integer.valueOf(newAmount));
            }
        }
        return this;
    }
    
    @Nonnull
    public SourceList remove(@Nullable SourceList list) {
        if (list != null) {
            for (Source source : list.getSources()) {
                this.remove(source, list.getAmount(source));
            }
        }
        return this;
    }
    
    @Nonnull
    public SourceList merge(@Nullable Source source, int amount) {
        if (source != null) {
            this.sources.put(source, Integer.valueOf(Math.max(amount, this.getAmount(source))));
        }
        return this;
    }
    
    @Nonnull
    public SourceList merge(@Nullable SourceList list) {
        if (list != null) {
            for (Source source : list.getSources()) {
                this.merge(source, list.getAmount(source));
            }
        }
        return this;
    }
    
    @Nonnull
    public Set<Source> getSources() {
        return Collections.unmodifiableSet(this.sources.keySet());
    }
    
    @Nonnull
    public List<Source> getSourcesSorted() {
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
    
    public void readFromNBT(@Nonnull CompoundNBT tag) {
        this.readFromNBT(tag, "Sources");
    }
    
    public void readFromNBT(@Nonnull CompoundNBT tag, @Nonnull String label) {
        this.sources.clear();
        ListNBT tagList = tag.getList(label, 10);
        for (int index = 0; index < tagList.size(); index++) {
            CompoundNBT singleTag = tagList.getCompound(index);
            if (singleTag.contains("key")) {
                this.add(Source.getSource(singleTag.getString("key")), singleTag.getInt("amount"));
            }
        }
    }
    
    public void writeToNBT(@Nonnull CompoundNBT tag) {
        this.writeToNBT(tag, "Sources");
    }
    
    public void writeToNBT(@Nonnull CompoundNBT tag, @Nonnull String label) {
        ListNBT tagList = new ListNBT();
        for (Source source : this.getSources()) {
            if (source != null) {
                CompoundNBT singleTag = new CompoundNBT();
                singleTag.putString("key", source.getTag());
                singleTag.putInt("amount", this.getAmount(source));
                tagList.add(singleTag);
            }
        }
        tag.put(label, tagList);
    }
}
