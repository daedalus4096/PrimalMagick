package com.verdantartifice.primalmagick.common.capabilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.misc.EntitySwapper;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Default implementation of the entity swappers capability.
 * 
 * @author Daedalus4096
 */
public class EntitySwappers implements IEntitySwappers {
    public static final Codec<EntitySwappers> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EntitySwapper.CODEC.listOf().fieldOf("swappers").<Queue<EntitySwapper>>xmap(LinkedBlockingQueue::new, ArrayList::new).forGetter(o -> o.swappers)
        ).apply(instance, EntitySwappers::new));

    private final Queue<EntitySwapper> swappers;  // Queue of active entity swappers for the world

    public EntitySwappers() {
        this.swappers = new LinkedBlockingQueue<>();
    }

    protected EntitySwappers(Collection<EntitySwapper> swappers) {
        this.swappers = new LinkedBlockingQueue<>(swappers);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider registries) {
        CompoundTag rootTag = new CompoundTag();
        ListTag swapperList = new ListTag();
        for (EntitySwapper swapper : this.swappers) {
            if (swapper != null) {
                swapperList.add(swapper.serializeNBT(registries));
            }
        }
        rootTag.put("Swappers", swapperList);
        return rootTag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider registries, CompoundTag nbt) {
        if (nbt == null) {
            return;
        }
        this.swappers.clear();
        ListTag swapperList = nbt.getListOrEmpty("Swappers");
        for (int index = 0; index < swapperList.size(); index++) {
            CompoundTag swapperTag = swapperList.getCompoundOrEmpty(index);
            EntitySwapper swapper = new EntitySwapper(registries, swapperTag);
            if (swapper.isValid()) {
                // Only accept valid swappers
                this.swappers.offer(swapper);
            }
        }
    }

    @Override
    public void serialize(@NotNull ValueOutput output) {
        output.store(Constants.MOD_ID + "swappers", CODEC, this);
    }

    @Override
    public void deserialize(@NotNull ValueInput input) {
        input.read(Constants.MOD_ID + "swappers", CODEC).ifPresent(this::copyFrom);
    }

    public void copyFrom(@Nullable EntitySwappers other) {
        if (other == null) {
            return;
        }
        this.swappers.clear();
        this.swappers.addAll(other.swappers);
    }

    @Override
    public boolean enqueue(EntitySwapper swapper) {
        if (swapper == null) {
            return false;
        } else {
            return this.swappers.offer(swapper);
        }
    }

    @Override
    @NotNull
    public Queue<EntitySwapper> getQueue() {
        return this.swappers;
    }

    @Override
    public boolean setQueue(Queue<EntitySwapper> queue) {
        if (queue == null) {
            return false;
        } else {
            // Make a shallow copy of the given queue
            this.swappers.clear();
            for (EntitySwapper swapper : queue) {
                this.enqueue(swapper);
            }
            return true;
        }
    }
}
