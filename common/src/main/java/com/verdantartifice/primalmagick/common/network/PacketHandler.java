package com.verdantartifice.primalmagick.common.network;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import commonnetwork.api.Dispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.PacketDistributor;

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
    
    public static void sendToAllAround(IMessageToClient message, ResourceKey<Level> dimension, BlockPos center, double radius) {
        // Send a message to the clients of all players within a given distance of the given world position
        CHANNEL.send(message, PacketDistributor.NEAR.with(new PacketDistributor.TargetPoint(center.getX() + 0.5D, center.getY() + 0.5D, center.getZ() + 0.5D, radius, dimension)));
    }
    
    public static void sendOverConnection(Object message, Connection conn) {
        // Send a message over the given connection
        CHANNEL.send(message, conn);
    }
    
    public static void reply(Object replyMessage, CustomPayloadEvent.Context ctx) {
        // Send a reply message in response to a previous message
        CHANNEL.reply(replyMessage, ctx);
    }
}
