package com.verdantartifice.primalmagick.common.mana.network;

import com.verdantartifice.primalmagick.common.sources.Source;

/**
 * Interface identifying a network node which can supply mana to other network nodes and, optionally, connect directly
 * to devices to extract mana into the network.
 *
 * @author Daedalus4096
 */
public interface IManaSupplier extends IManaNetworkNode {
    default boolean isOrigin() {
        return true;
    }

    int getTransmissionRange();
    boolean canSupply(Source source);
    int getManaThroughput();
}
