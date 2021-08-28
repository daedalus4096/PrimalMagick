package com.verdantartifice.primalmagic.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagic.common.util.EntityUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

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
        IPlayerStats stats = PrimalMagicCapabilities.getStats(player);
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
                Player player = (FMLEnvironment.dist == Dist.CLIENT) ? EntityUtils.getCurrentPlayer() : null;
                IPlayerStats stats = PrimalMagicCapabilities.getStats(player);
                if (stats != null) {
                    stats.deserializeNBT(message.data);
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
