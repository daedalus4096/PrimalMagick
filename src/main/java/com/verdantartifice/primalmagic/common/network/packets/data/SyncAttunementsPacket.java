package com.verdantartifice.primalmagic.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerAttunements;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToClient;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet to sync attunements capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncAttunementsPacket implements IMessageToClient {
    protected CompoundNBT data;

    public SyncAttunementsPacket() {
        this.data = null;
    }
    
    public SyncAttunementsPacket(PlayerEntity player) {
        IPlayerAttunements attunements = PrimalMagicCapabilities.getAttunements(player);
        this.data = (attunements != null) ? attunements.serializeNBT() : null;
    }
    
    public static void encode(SyncAttunementsPacket message, PacketBuffer buf) {
        buf.writeCompoundTag(message.data);
    }
    
    public static SyncAttunementsPacket decode(PacketBuffer buf) {
        SyncAttunementsPacket message = new SyncAttunementsPacket();
        message.data = buf.readCompoundTag();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SyncAttunementsPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
            	Minecraft mc = Minecraft.getInstance();
                PlayerEntity player = mc.player;
                IPlayerAttunements attunements = PrimalMagicCapabilities.getAttunements(player);
                if (attunements != null) {
                    attunements.deserializeNBT(message.data);
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
