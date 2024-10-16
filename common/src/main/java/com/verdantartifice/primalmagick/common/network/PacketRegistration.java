package com.verdantartifice.primalmagick.common.network;

import com.verdantartifice.primalmagick.common.network.packets.data.SyncKnowledgePacket;
import commonnetwork.api.Network;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PacketRegistration {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void registerMessages() {
        Network
            // Client-bound play channel packets
            .registerPacket(SyncKnowledgePacket.type(), SyncKnowledgePacket.class, SyncKnowledgePacket.STREAM_CODEC, SyncKnowledgePacket::onMessage)
            ;
    }
}
