package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.concoctions.BombCasingItem;
import com.verdantartifice.primalmagick.common.items.concoctions.ConcoctionItem;
import com.verdantartifice.primalmagick.common.items.concoctions.SkyglassFlaskItem;
import com.verdantartifice.primalmagick.common.items.entities.FlyingCarpetItem;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.apache.commons.lang3.function.TriConsumer;

/**
 * Handlers for cauldron related events.
 *
 * @author Daedalus4096
 */
public class CauldronEvents {
    private static final Identifier EMPTY_DISPATCHER = Identifier.withDefaultNamespace("empty");
    private static final Identifier WATER_DISPATCHER = Identifier.withDefaultNamespace("water");

    public static void onRegisterInteractions(TriConsumer<Identifier, Item, CauldronInteraction> registrar) {
        registrar.accept(EMPTY_DISPATCHER, ItemsPM.CONCOCTION.get(), ConcoctionItem.FILL_EMPTY_CAULDRON);
        registrar.accept(WATER_DISPATCHER, ItemsPM.FLYING_CARPET.get(), FlyingCarpetItem.DYED_CARPET);
        registrar.accept(WATER_DISPATCHER, ItemsPM.SKYGLASS_FLASK.get(), SkyglassFlaskItem.FILL_CONCOCTION);
        registrar.accept(WATER_DISPATCHER, ItemsPM.BOMB_CASING.get(), BombCasingItem.FILL_BOMB);
        registrar.accept(WATER_DISPATCHER, ItemsPM.CONCOCTION.get(), ConcoctionItem.FILL_WATER_CAULDRON);
    }
}
