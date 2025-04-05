package com.verdantartifice.primalmagick.common.mana.network;

import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.world.level.Level;

/**
 * Interface identifying a relay in a mana network, a device which can both transmit and receive mana but cannot
 * directly connect to devices to store it.
 *
 * @author Daedalus4096
 */
public interface IManaRelay extends IManaSupplier, IManaConsumer {
    @Override
    default boolean isTerminus() {
        return false;
    }

    @Override
    default boolean isOrigin() {
        return false;
    }

    @Override
    default boolean canSupply(Source source) {
        return this.canRelay(source);
    }

    @Override
    default boolean canConsume(Source source) {
        return this.canRelay(source);
    }

    boolean canRelay(Source source);

    @Override
    default void onPlaced(Level leve) {
        // TODO Stub
    }
}
