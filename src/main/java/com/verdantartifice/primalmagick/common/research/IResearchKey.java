package com.verdantartifice.primalmagick.common.research;

import javax.annotation.Nullable;

import net.minecraft.world.entity.player.Player;

/**
 * Interface for a research key, which can be tested for known-ness for a player.
 * 
 * @author Daedalus4096
 */
public interface IResearchKey {
    boolean isEmpty();
    boolean isKnownBy(@Nullable Player player);
    boolean isKnownByStrict(@Nullable Player player);
}