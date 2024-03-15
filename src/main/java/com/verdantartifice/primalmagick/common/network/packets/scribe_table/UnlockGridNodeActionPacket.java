package com.verdantartifice.primalmagick.common.network.packets.scribe_table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2i;

import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet sent to trigger a server-side unlock of a linguistics grid node on a scribe table.
 * 
 * @author Daedalus4096
 */
public class UnlockGridNodeActionPacket implements IMessageToServer {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    protected final ResourceLocation gridDefinitionKey;
    protected final Vector2i nodePos;
    
    public UnlockGridNodeActionPacket(ResourceLocation key, Vector2i pos) {
        this.gridDefinitionKey = key;
        this.nodePos = pos;
    }
    
    protected UnlockGridNodeActionPacket(ResourceLocation key, int x, int y) {
        this(key, new Vector2i(x, y));
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_SERVER;
    }
    
    public static void encode(UnlockGridNodeActionPacket message, FriendlyByteBuf buf) {
        buf.writeResourceLocation(message.gridDefinitionKey);
        buf.writeVarInt(message.nodePos.x());
        buf.writeVarInt(message.nodePos.y());
    }
    
    public static UnlockGridNodeActionPacket decode(FriendlyByteBuf buf) {
        return new UnlockGridNodeActionPacket(buf.readResourceLocation(), buf.readVarInt(), buf.readVarInt());
    }
    
    public static void onMessage(UnlockGridNodeActionPacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        if (!LinguisticsManager.getPlayerGrid(player, message.gridDefinitionKey).unlock(message.nodePos)) {
            LOGGER.warn("Failed to unlock server side node ({}, {}) for linguistics grid {}", message.nodePos.x(), message.nodePos.y(), message.gridDefinitionKey);
        }
    }
}
