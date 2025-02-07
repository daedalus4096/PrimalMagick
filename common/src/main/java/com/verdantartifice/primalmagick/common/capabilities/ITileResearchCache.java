package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.util.INBTSerializablePM;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

/**
 * Capability interface for storing a subset of the research completed by a tile's owner.  Owned by
 * block entities.
 * 
 * @author Daedalus4096
 */
public interface ITileResearchCache extends INBTSerializablePM<CompoundTag> {
    /**
     * Remove all research from the cache.
     */
    public void clear();
    
    /**
     * Determine if the given research has been completed, as known by the cache.
     * 
     * @param key a key for the desired research entry
     * @return true if the given research is complete, false otherwise
     */
    public boolean isResearchComplete(@Nullable AbstractResearchKey<?> key);
    
    /**
     * Determine if the given research has been completed, as known by the cache.
     * 
     * @param keys a key for the desired research entry
     * @return true if the given research is complete, false otherwise
     */
    public boolean isResearchComplete(List<AbstractResearchKey<?>> keys);
    
    /**
     * Updates the cache to contain a subset of the given player's research.  The subset to copy over
     * is defined as the set of all research keys for which the given filter predicate returns true.
     * Only completed research entries will be considered.
     * 
     * @param player the player whose research to cache
     * @param researchFilter the predicate defining which research keys to cache
     */
    public void update(@Nullable Player player, @Nullable Predicate<AbstractResearchKey<?>> researchFilter);
}
