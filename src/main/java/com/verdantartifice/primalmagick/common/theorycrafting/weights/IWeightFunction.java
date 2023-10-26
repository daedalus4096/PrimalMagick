package com.verdantartifice.primalmagick.common.theorycrafting.weights;

import net.minecraft.world.entity.player.Player;

/**
 * Abstraction interface intended for use in calculating weights for theorycrafting projects.
 * 
 * @author Daedalus4096
 */
public interface IWeightFunction {
    double getWeight(Player player);
    String getFunctionType();
    <T extends IWeightFunction> IWeightFunctionSerializer<T> getSerializer();
}
