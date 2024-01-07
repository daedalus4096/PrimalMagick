package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerAttunements;
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
 * Packet to sync attunements capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncAttunementsPacket implements IMessageToClient {
    protected CompoundTag data;

    public SyncAttunementsPacket() {
        this.data = null;
    }
    
    public SyncAttunementsPacket(Player player) {
        IPlayerAttunements attunements = PrimalMagickCapabilities.getAttunements(player);
        this.data = (attunements != null) ? attunements.serializeNBT() : null;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(SyncAttunementsPacket message, FriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncAttunementsPacket decode(FriendlyByteBuf buf) {
        SyncAttunementsPacket message = new SyncAttunementsPacket();
        message.data = buf.readNbt();
        return message;
    }
    
    public static void onMessage(SyncAttunementsPacket message, CustomPayloadEvent.Context ctx) {
        Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
        IPlayerAttunements attunements = PrimalMagickCapabilities.getAttunements(player);
        if (attunements != null) {
            attunements.deserializeNBT(message.data);
        }
    }
}
