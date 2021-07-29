package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.entities.FlyingCarpetItem;

import net.minecraft.core.cauldron.CauldronInteraction;

/**
 * Point of registration for cauldron interactions.
 * 
 * @author Daedalus4096
 */
public class InitCauldron {
    public static void initCauldronInteractions() {
        CauldronInteraction.WATER.put(ItemsPM.FLYING_CARPET.get(), FlyingCarpetItem.DYED_CARPET);
    }
}
