package com.verdantartifice.primalmagic.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToClient;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet to sync cooldown capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncCooldownsPacket implements IMessageToClient {
    protected CompoundNBT data;

    public SyncCooldownsPacket() {
        this.data = null;
    }
    
    public SyncCooldownsPacket(PlayerEntity player) {
        IPlayerCooldowns cooldowns = PrimalMagicCapabilities.getCooldowns(player);
        this.data = (cooldowns != null) ? cooldowns.serializeNBT() : null;
    }
    
    public static void encode(SyncCooldownsPacket message, PacketBuffer buf) {
        buf.writeCompoundTag(message.data);
    }
    
    public static SyncCooldownsPacket decode(PacketBuffer buf) {
        SyncCooldownsPacket message = new SyncCooldownsPacket();
        message.data = buf.readCompoundTag();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SyncCooldownsPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
            	Minecraft mc = Minecraft.getInstance();
                PlayerEntity player = mc.player;
                IPlayerCooldowns cooldowns = PrimalMagicCapabilities.getCooldowns(player);
                if (cooldowns != null) {
                    cooldowns.deserializeNBT(message.data);
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
