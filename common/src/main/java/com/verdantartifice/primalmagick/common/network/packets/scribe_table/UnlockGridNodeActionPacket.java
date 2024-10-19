package com.verdantartifice.primalmagick.common.network.packets.scribe_table;

import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.books.grids.PlayerGrid;
import com.verdantartifice.primalmagick.common.menus.ScribeGainComprehensionMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2i;

/**
 * Packet sent to trigger a server-side unlock of a linguistics grid node on a scribe table.
 * 
 * @author Daedalus4096
 */
public class UnlockGridNodeActionPacket implements IMessageToServer {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("unlock_grid_node_action");
    public static final StreamCodec<RegistryFriendlyByteBuf, UnlockGridNodeActionPacket> STREAM_CODEC = StreamCodec.ofMember(UnlockGridNodeActionPacket::encode, UnlockGridNodeActionPacket::decode);

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

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(UnlockGridNodeActionPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeResourceLocation(message.gridDefinitionKey);
        buf.writeVarInt(message.nodePos.x());
        buf.writeVarInt(message.nodePos.y());
    }
    
    public static UnlockGridNodeActionPacket decode(RegistryFriendlyByteBuf buf) {
        return new UnlockGridNodeActionPacket(buf.readResourceLocation(), buf.readVarInt(), buf.readVarInt());
    }
    
    public static void onMessage(PacketContext<UnlockGridNodeActionPacket> ctx) {
        UnlockGridNodeActionPacket message = ctx.message();
        ServerPlayer player = ctx.sender();
        PlayerGrid grid = LinguisticsManager.getPlayerGrid(player, message.gridDefinitionKey);
        if (grid == null || !grid.unlock(message.nodePos, player.level().registryAccess())) {
            LOGGER.warn("Failed to unlock server side node ({}, {}) for linguistics grid {}", message.nodePos.x(), message.nodePos.y(), message.gridDefinitionKey);
        } else if (player.containerMenu instanceof ScribeGainComprehensionMenu menu) {
            // If the unlock action was successful, have the menu re-sync its data to the client to update vocabulary count
            menu.refreshBookData();
        }
    }
}
