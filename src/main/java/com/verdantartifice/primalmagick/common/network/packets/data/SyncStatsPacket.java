package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet to sync statistics capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncStatsPacket implements IMessageToClient {
    protected CompoundTag data;

    public SyncStatsPacket() {
        this.data = null;
    }
    
    public SyncStatsPacket(Player player) {
        IPlayerStats stats = PrimalMagickCapabilities.getStats(player);
        this.data = (stats != null) ? stats.serializeNBT() : null;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(SyncStatsPacket message, FriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncStatsPacket decode(FriendlyByteBuf buf) {
        SyncStatsPacket message = new SyncStatsPacket();
        message.data = buf.readNbt();
        return message;
    }
    
    public static void onMessage(SyncStatsPacket message, CustomPayloadEvent.Context ctx) {
        Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
        IPlayerStats stats = PrimalMagickCapabilities.getStats(player);
        if (stats != null) {
            stats.deserializeNBT(message.data);
        }
    }
}
