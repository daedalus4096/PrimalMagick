package com.verdantartifice.primalmagick.common.util;

import java.util.function.Predicate;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;

/**
 * Defines entity selector predicates for use in the mod.
 * 
 * @author Daedalus4096
 */
public final class EntitySelectorsPM {
    public static Predicate<Entity> validHellishChainTarget(Player attacker) {
        return EntitySelector.NO_SPECTATORS.and(EntitySelector.ENTITY_STILL_ALIVE).and((entity) -> {
            return !entity.isAlliedTo(attacker);
        });
    }
}
