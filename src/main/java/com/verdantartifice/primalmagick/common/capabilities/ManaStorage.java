package com.verdantartifice.primalmagick.common.capabilities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

/**
 * Default implementation of the mana storage capability.
 * 
 * @author Daedalus4096
 */
public class ManaStorage implements IManaStorage {
    protected static final int INFINITE = -1;
    
    protected Set<Source> allowedSources;
    protected SourceList mana;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;
    
    public ManaStorage(int capacity, Source... allowedSources) {
        this(capacity, capacity, capacity, new SourceList(), allowedSources);
    }
    
    public ManaStorage(int capacity, int maxTransfer, Source... allowedSources) {
        this(capacity, maxTransfer, maxTransfer, new SourceList(), allowedSources);
    }
    
    public ManaStorage(int capacity, int maxReceive, int maxExtract, Source... allowedSources) {
        this(capacity, maxReceive, maxExtract, new SourceList(), allowedSources);
    }
    
    public ManaStorage(int capacity, int maxReceive, int maxExtract, SourceList mana, Source... allowedSources) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.allowedSources = new HashSet<>(Arrays.asList(allowedSources));
        this.mana = new SourceList();
        this.setMana(mana);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("Mana", this.mana.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        SourceList nbtMana = new SourceList();
        nbtMana.deserializeNBT(nbt.getCompound("Mana"));
        this.setMana(nbtMana);
    }
    
    public void setMana(SourceList mana) {
        this.mana.clear();
        for (Source source : mana.getSources()) {
            this.setMana(source, mana.getAmount(source));
        }
    }
    
    public void setMana(Source source, int amount) {
        if (this.allowedSources.contains(source) && this.capacity != INFINITE) {
            this.mana.set(source, Mth.clamp(amount, 0, this.capacity));
        }
    }

    @Override
    public int receiveMana(Source source, int maxReceive, boolean simulate) {
        if (!this.canReceive(source) || this.capacity == INFINITE) {
            return 0;
        }
        int manaReceived = Math.min(this.capacity - this.mana.getAmount(source), Math.min(this.maxReceive, maxReceive));
        if (!simulate) {
            this.mana.add(source, manaReceived);
            this.onManaChanged();
        }
        return manaReceived;
    }

    @Override
    public int extractMana(Source source, int maxExtract, boolean simulate) {
        if (!this.canExtract(source)) {
            return 0;
        }
        if (this.capacity == INFINITE) {
            return maxExtract;
        }
        int manaExtracted = Math.min(this.mana.getAmount(source), Math.min(this.maxExtract, maxExtract));
        if (!simulate) {
            this.mana.reduce(source, manaExtracted);
            this.onManaChanged();
        }
        return manaExtracted;
    }

    @Override
    public int getManaStored(Source source) {
        return this.capacity == INFINITE ? Integer.MAX_VALUE : this.mana.getAmount(source);
    }

    @Override
    public int getMaxManaStored(Source source) {
        return this.allowedSources.contains(source) ? this.capacity : 0;
    }

    @Override
    public boolean canExtract(Source source) {
        return this.allowedSources.contains(source) && this.maxExtract > 0;
    }

    @Override
    public boolean canReceive(Source source) {
        return this.allowedSources.contains(source) && this.maxReceive > 0;
    }
    
    protected void onManaChanged() {
        // Do nothing by default
    }
}
