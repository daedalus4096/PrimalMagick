package com.verdantartifice.primalmagic.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerCompanions;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToClient;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet to sync companion capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncCompanionsPacket implements IMessageToClient {
    protected CompoundNBT data;

    public SyncCompanionsPacket() {
        this.data = null;
    }
    
    public SyncCompanionsPacket(PlayerEntity player) {
        IPlayerCompanions companions = PrimalMagicCapabilities.getCompanions(player);
        this.data = (companions != null) ? companions.serializeNBT() : null;
    }
    
    public static void encode(SyncCompanionsPacket message, PacketBuffer buf) {
        buf.writeCompoundTag(message.data);
    }
    
    public static SyncCompanionsPacket decode(PacketBuffer buf) {
        SyncCompanionsPacket message = new SyncCompanionsPacket();
        message.data = buf.readCompoundTag();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SyncCompanionsPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                Minecraft mc = Minecraft.getInstance();
                PlayerEntity player = mc.player;
                IPlayerCompanions companions = PrimalMagicCapabilities.getCompanions(player);
                if (companions != null) {
                    companions.deserializeNBT(message.data);
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
