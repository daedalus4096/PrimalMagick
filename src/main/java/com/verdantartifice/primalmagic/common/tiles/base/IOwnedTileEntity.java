package com.verdantartifice.primalmagic.common.tiles.base;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;

public interface IOwnedTileEntity {
    void setTileOwner(@Nullable PlayerEntity owner);
    
    @Nullable
    PlayerEntity getTileOwner();
}
