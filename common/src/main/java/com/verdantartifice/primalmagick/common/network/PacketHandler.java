package com.verdantartifice.primalmagick.common.network;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import commonnetwork.api.Dispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

/**
 * Handler class for processing packets.  Responsible for all custom communication between the client and the server.
 * 
 * @author Daedalus4096
 */
public class PacketHandler {
    public static void sendToServer(IMessageToServer message) {
        // Send a packet from a client to the server
        Dispatcher.sendToServer(message);
    }
    
    public static void sendToPlayer(IMessageToClient message, ServerPlayer player) {
        // Send a message from the server to a specific player's client
        Dispatcher.sendToClient(message, player);
    }
    
    public static void sendToAllAround(IMessageToClient message, ServerLevel level, BlockPos center, double radius) {
        // Send a message to the clients of all players within a given distance of the given world position
        Dispatcher.sendToClientsInRange(message, level, center, radius);
    }
}
