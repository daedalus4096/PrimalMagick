package com.verdantartifice.primalmagick.common.tiles.base;

import javax.annotation.Nullable;

import net.minecraft.world.entity.player.Player;

/**
 * Interface marking a tile entity as one that can have an owner.  This may involved specific
 * privileges being granted to the owner, or the tile entity may simply need to know who its owner
 * is for some calculation.
 * 
 * @author Daedalus4096
 */
public interface IOwnedTileEntity {
    /**
     * Set the owner of the tile entity.
     * 
     * @param owner the new owning player
     */
    public void setTileOwner(@Nullable Player owner);
    
    /**
     * Get the owning player of the tile entity.
     * 
     * @return the owning player of the tile entity
     */
    @Nullable
    public Player getTileOwner();
}
