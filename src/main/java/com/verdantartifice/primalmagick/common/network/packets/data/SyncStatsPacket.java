package com.verdantartifice.primalmagick.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

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
    
    public static void encode(SyncStatsPacket message, FriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncStatsPacket decode(FriendlyByteBuf buf) {
        SyncStatsPacket message = new SyncStatsPacket();
        message.data = buf.readNbt();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SyncStatsPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
                IPlayerStats stats = PrimalMagickCapabilities.getStats(player);
                if (stats != null) {
                    stats.deserializeNBT(message.data);
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
