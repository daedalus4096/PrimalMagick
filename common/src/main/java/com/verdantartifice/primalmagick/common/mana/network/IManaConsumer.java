package com.verdantartifice.primalmagick.common.mana.network;

import com.verdantartifice.primalmagick.common.sources.Source;

/**
 * Interface identifying a network node which can receive mana from other network nodes and, optionally, connect
 * directly to devices to sink mana out of the network.
 *
 * @author Daedalus4096
 */
public interface IManaConsumer extends IManaNetworkNode {
    default boolean isTerminus() {
        return true;
    }

    boolean canConsume(Source source);
}
