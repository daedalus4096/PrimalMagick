package com.verdantartifice.primalmagick.common.capabilities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

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
        this(capacity, capacity, capacity, SourceList.EMPTY, allowedSources);
    }
    
    public ManaStorage(int capacity, int maxTransfer, Source... allowedSources) {
        this(capacity, maxTransfer, maxTransfer, SourceList.EMPTY, allowedSources);
    }
    
    public ManaStorage(int capacity, int maxReceive, int maxExtract, Source... allowedSources) {
        this(capacity, maxReceive, maxExtract, SourceList.EMPTY, allowedSources);
    }
    
    public ManaStorage(int capacity, int maxReceive, int maxExtract, SourceList mana, Source... allowedSources) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.allowedSources = new HashSet<>(Arrays.asList(allowedSources));
        this.mana = SourceList.EMPTY;
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
        this.setMana(SourceList.deserializeNBT(nbt.getCompound("Mana")));
    }
    
    public void setMana(SourceList mana) {
        this.mana = SourceList.EMPTY;
        for (Source source : mana.getSources()) {
            this.setMana(source, mana.getAmount(source));
        }
    }
    
    public void setMana(Source source, int amount) {
        if (this.allowedSources.contains(source) && this.capacity != INFINITE) {
            this.mana = this.mana.set(source, Mth.clamp(amount, 0, this.capacity));
        }
    }

    @Override
    public int receiveMana(Source source, int maxReceive, boolean simulate) {
        if (!this.canReceive(source) || this.capacity == INFINITE) {
            return 0;
        }
        int manaReceived = Math.min(this.capacity - this.mana.getAmount(source), Math.min(this.maxReceive, maxReceive));
        if (!simulate) {
            this.mana = this.mana.add(source, manaReceived);
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
            this.mana = this.mana.reduce(source, manaExtracted);
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
    public boolean canStore(Source source) {
        return this.allowedSources.contains(source);
    }

    @Override
    public boolean canExtract(Source source) {
        return this.canStore(source) && this.maxExtract > 0;
    }

    @Override
    public boolean canReceive(Source source) {
        return this.canStore(source) && this.maxReceive > 0;
    }
    
    protected void onManaChanged() {
        // Do nothing by default
    }
    
    /**
     * Capability provider for the mana storage capability.  Used to attach capability data to the owner.
     * 
     * @author Daedalus4096
     * @see {@link com.verdantartifice.primalmagick.common.events.CapabilityEvents}
     */
    public static class Provider implements ICapabilitySerializable<CompoundTag> {
        public static final ResourceLocation NAME = PrimalMagick.resource("capability_mana_storage");
        
        private final IManaStorage instance;
        private final LazyOptional<IManaStorage> holder;
        
        public Provider(int capacity, int maxReceive, int maxExtract, Source... allowedSources) {
            this.instance = new ManaStorage(capacity, maxReceive, maxExtract, SourceList.EMPTY, allowedSources);
            this.holder = LazyOptional.of(() -> this.instance);  // Cache a lazy optional of the capability instance
        }

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if (cap == PrimalMagickCapabilities.MANA_STORAGE) {
                return this.holder.cast();
            } else {
                return LazyOptional.empty();
            }
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.instance.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.instance.deserializeNBT(nbt);
        }
    }
}
