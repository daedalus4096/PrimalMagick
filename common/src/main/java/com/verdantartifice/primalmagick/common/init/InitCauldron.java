package com.verdantartifice.primalmagick.common.init;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.items.concoctions.BombCasingItem;
import com.verdantartifice.primalmagick.common.items.concoctions.ConcoctionItem;
import com.verdantartifice.primalmagick.common.items.concoctions.SkyglassFlaskItem;
import com.verdantartifice.primalmagick.common.items.entities.FlyingCarpetItem;
import net.minecraft.core.cauldron.CauldronInteraction;

/**
 * Point of registration for cauldron interactions.
 * 
 * @author Daedalus4096
 */
public class InitCauldron {
    public static void initCauldronInteractions() {
        CauldronInteraction.EMPTY.map().put(ItemsPM.CONCOCTION.get(), ConcoctionItem.FILL_EMPTY_CAULDRON);
        CauldronInteraction.WATER.map().put(ItemsPM.FLYING_CARPET.get(), FlyingCarpetItem.DYED_CARPET);
        CauldronInteraction.WATER.map().put(ItemsPM.SKYGLASS_FLASK.get(), SkyglassFlaskItem.FILL_CONCOCTION);
        CauldronInteraction.WATER.map().put(ItemsPM.BOMB_CASING.get(), BombCasingItem.FILL_BOMB);
        CauldronInteraction.WATER.map().put(ItemsPM.CONCOCTION.get(), ConcoctionItem.FILL_WATER_CAULDRON);
    }
}
