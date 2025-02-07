package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.misc.EntitySwapper;
import com.verdantartifice.primalmagick.common.util.INBTSerializablePM;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Queue;

/**
 * Capability interface for storing active entity swappers.  Attached to worlds.
 * 
 * @author Daedalus4096
 */
public interface IEntitySwappers extends INBTSerializablePM<CompoundTag> {
    /**
     * Add the given entity swapper to the world's queue.
     * 
     * @param swapper the entity swapper to be added
     * @return true if the addition was successful, false otherwise
     */
    public boolean enqueue(@Nullable EntitySwapper swapper);
    
    /**
     * Get the entity swapper queue for the world.
     * 
     * @return the entity swapper queue
     */
    @Nonnull
    public Queue<EntitySwapper> getQueue();
    
    /**
     * Replace the entity swapper queue for the world with a copy of the given one.
     * 
     * @param queue the new entity swapper queue
     * @return true if the replacement was successful, false otherwise
     */
    public boolean setQueue(@Nullable Queue<EntitySwapper> queue);
}
