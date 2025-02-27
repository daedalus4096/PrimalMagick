package com.verdantartifice.primalmagick.common.capabilities;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Default implementation of the mana storage capability.
 * 
 * @author Daedalus4096
 */
public class ManaStorage implements IManaStorage<ManaStorage> {
    public static final Codec<ManaStorage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("capacity").forGetter(cap -> cap.capacity),
            Codec.INT.fieldOf("maxReceive").forGetter(cap -> cap.maxReceive),
            Codec.INT.fieldOf("maxExtract").forGetter(cap -> cap.maxExtract),
            SourceList.CODEC.fieldOf("mana").forGetter(cap -> cap.mana),
            Source.CODEC.listOf().fieldOf("allowedSources").<Set<Source>>xmap(l -> new HashSet<>(l), s -> new ArrayList<>(s)).forGetter(cap -> cap.allowedSources)
        ).apply(instance, ManaStorage::new));
    
    public static final StreamCodec<ByteBuf, ManaStorage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, cap -> cap.capacity,
            ByteBufCodecs.VAR_INT, cap -> cap.maxReceive,
            ByteBufCodecs.VAR_INT, cap -> cap.maxExtract,
            SourceList.STREAM_CODEC, cap -> cap.mana,
            Source.STREAM_CODEC.apply(ByteBufCodecs.collection(HashSet::new)), cap -> cap.allowedSources,
            ManaStorage::new);
    
    public static final ManaStorage EMPTY = new ManaStorage(0, 0, 0, SourceList.EMPTY, ImmutableList.of());
    protected static final int INFINITE = -1;
    
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;
    protected SourceList mana;
    protected Set<Source> allowedSources;
    
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
        this(capacity, maxReceive, maxExtract, mana, Arrays.asList(allowedSources));
    }
    
    public ManaStorage(int capacity, int maxReceive, int maxExtract, SourceList mana, Collection<Source> allowedSources) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.allowedSources = new HashSet<>(allowedSources);
        this.mana = SourceList.EMPTY;
        this.setMana(mana);
    }

    @Override
    public Codec<ManaStorage> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ManaStorage> streamCodec() {
        return STREAM_CODEC;
    }
    
    public void copyInto(ManaStorage other) {
        // Only copy current mana levels to avoid blowing away unchanging values such as capacity
        other.setMana(this.mana);
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
    
    @Override
    public int hashCode() {
        return Objects.hash(allowedSources, capacity, mana, maxExtract, maxReceive);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ManaStorage other = (ManaStorage) obj;
        return Objects.equals(allowedSources, other.allowedSources) && capacity == other.capacity
                && Objects.equals(mana, other.mana) && maxExtract == other.maxExtract && maxReceive == other.maxReceive;
    }
}
