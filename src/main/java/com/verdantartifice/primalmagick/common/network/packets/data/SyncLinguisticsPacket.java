package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerLinguistics;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkDirection;

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
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(SyncLinguisticsPacket message, FriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncLinguisticsPacket decode(FriendlyByteBuf buf) {
        SyncLinguisticsPacket message = new SyncLinguisticsPacket();
        message.data = buf.readNbt();
        return message;
    }
    
    public static void onMessage(SyncLinguisticsPacket message, CustomPayloadEvent.Context ctx) {
        Player player = FMLEnvironment.dist.isClient() ? ClientUtils.getCurrentPlayer() : null;
        PrimalMagickCapabilities.getLinguistics(player).ifPresent(linguistics -> {
            linguistics.deserializeNBT(message.data);
        });
    }
}
