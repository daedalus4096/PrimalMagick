package com.verdantartifice.primalmagic.common.capabilities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Default implementation of the mana storage capability.
 * 
 * @author Daedalus4096
 */
public class ManaStorage implements IManaStorage {
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
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.put("Mana", this.mana.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        SourceList nbtMana = new SourceList();
        nbtMana.deserializeNBT(nbt.getCompound("Mana"));
        this.setMana(nbtMana);
    }
    
    protected void setMana(SourceList mana) {
        this.mana.clear();
        for (Source source : mana.getSources()) {
            if (this.allowedSources.contains(source)) {
                this.mana.set(source, MathHelper.clamp(mana.getAmount(source), 0, this.capacity));
            }
        }
    }

    @Override
    public int receiveMana(Source source, int maxReceive, boolean simulate) {
        if (!this.canReceive(source)) {
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
        int manaExtracted = Math.min(this.mana.getAmount(source), Math.min(this.maxExtract, maxExtract));
        if (!simulate) {
            this.mana.add(source, -manaExtracted);
            this.onManaChanged();
        }
        return manaExtracted;
    }

    @Override
    public int getManaStored(Source source) {
        return this.mana.getAmount(source);
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
    
    /**
     * Storage manager for the tile mana storage capability.  Used to register the capability.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagic.common.init.InitCapabilities}
     */
    public static class Storage implements Capability.IStorage<IManaStorage> {
        @Override
        public INBT writeNBT(Capability<IManaStorage> capability, IManaStorage instance, Direction side) {
            // Use the instance's pre-defined serialization
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<IManaStorage> capability, IManaStorage instance, Direction side, INBT nbt) {
            // Use the instance's pre-defined deserialization
            instance.deserializeNBT((CompoundNBT)nbt);
        }
    }
    
    /**
     * Factory for the tile mana storage capability.  Returns a stub default implementation.  Used
     * to register the capability.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagic.common.init.InitCapabilities}
     */
    public static class Factory implements Callable<IManaStorage> {
        @Override
        public IManaStorage call() throws Exception {
            return new ManaStorage(0, new Source[0]);
        }
    }
}
