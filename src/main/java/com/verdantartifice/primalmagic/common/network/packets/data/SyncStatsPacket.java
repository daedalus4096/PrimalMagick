package com.verdantartifice.primalmagic.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToClient;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet to sync statistics capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncStatsPacket implements IMessageToClient {
    protected CompoundNBT data;

    public SyncStatsPacket() {
        this.data = null;
    }
    
    public SyncStatsPacket(PlayerEntity player) {
        IPlayerStats stats = PrimalMagicCapabilities.getStats(player);
        this.data = (stats != null) ? stats.serializeNBT() : null;
    }
    
    public static void encode(SyncStatsPacket message, PacketBuffer buf) {
        buf.writeCompoundTag(message.data);
    }
    
    public static SyncStatsPacket decode(PacketBuffer buf) {
        SyncStatsPacket message = new SyncStatsPacket();
        message.data = buf.readCompoundTag();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SyncStatsPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                PlayerEntity player = Minecraft.getInstance().player;
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
