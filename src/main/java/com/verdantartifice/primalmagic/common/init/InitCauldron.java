package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.concoctions.BombCasingItem;
import com.verdantartifice.primalmagic.common.items.concoctions.ConcoctionItem;
import com.verdantartifice.primalmagic.common.items.concoctions.SkyglassFlaskItem;
import com.verdantartifice.primalmagic.common.items.entities.FlyingCarpetItem;

import net.minecraft.core.cauldron.CauldronInteraction;

/**
 * Point of registration for cauldron interactions.
 * 
 * @author Daedalus4096
 */
public class InitCauldron {
    public static void initCauldronInteractions() {
        CauldronInteraction.EMPTY.put(ItemsPM.CONCOCTION.get(), ConcoctionItem.FILL_EMPTY_CAULDRON);
        CauldronInteraction.WATER.put(ItemsPM.FLYING_CARPET.get(), FlyingCarpetItem.DYED_CARPET);
        CauldronInteraction.WATER.put(ItemsPM.SKYGLASS_FLASK.get(), SkyglassFlaskItem.FILL_CONCOCTION);
        CauldronInteraction.WATER.put(ItemsPM.BOMB_CASING.get(), BombCasingItem.FILL_BOMB);
        CauldronInteraction.WATER.put(ItemsPM.CONCOCTION.get(), ConcoctionItem.FILL_WATER_CAULDRON);
    }
}
