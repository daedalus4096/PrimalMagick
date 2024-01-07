package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet to sync ward capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncWardPacket implements IMessageToClient {
    protected CompoundTag data;

    public SyncWardPacket() {
        this.data = null;
    }
    
    public SyncWardPacket(Player player) {
        PrimalMagickCapabilities.getWard(player).ifPresent(ward -> {
            this.data = ward.serializeNBT();
        });
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(SyncWardPacket message, FriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncWardPacket decode(FriendlyByteBuf buf) {
        SyncWardPacket message = new SyncWardPacket();
        message.data = buf.readNbt();
        return message;
    }
    
    public static void onMessage(SyncWardPacket message, CustomPayloadEvent.Context ctx) {
        Player player = FMLEnvironment.dist.isClient() ? ClientUtils.getCurrentPlayer() : null;
        if (player != null) {
            PrimalMagickCapabilities.getWard(player).ifPresent(ward -> {
                ward.deserializeNBT(message.data);
            });
        }
    }
}
