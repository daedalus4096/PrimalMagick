package com.verdantartifice.primalmagick.common.mana.network;

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
}
