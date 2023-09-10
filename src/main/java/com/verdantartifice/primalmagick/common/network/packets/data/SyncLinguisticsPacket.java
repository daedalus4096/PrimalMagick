package com.verdantartifice.primalmagick.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerLinguistics;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet to sync linguistics capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncLinguisticsPacket implements IMessageToClient {
    protected CompoundTag data;

    public SyncLinguisticsPacket() {
        this.data = null;
    }
    
    public SyncLinguisticsPacket(Player player) {
        IPlayerLinguistics linguistics = PrimalMagickCapabilities.getLinguistics(player).orElse(null);
        this.data = (linguistics != null) ? linguistics.serializeNBT() : null;
    }
    
    public static void encode(SyncLinguisticsPacket message, FriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncLinguisticsPacket decode(FriendlyByteBuf buf) {
        SyncLinguisticsPacket message = new SyncLinguisticsPacket();
        message.data = buf.readNbt();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SyncLinguisticsPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
                PrimalMagickCapabilities.getLinguistics(player).ifPresent(linguistics -> {
                    linguistics.deserializeNBT(message.data);
                });
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
