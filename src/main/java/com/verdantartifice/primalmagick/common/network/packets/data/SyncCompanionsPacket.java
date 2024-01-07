package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCompanions;
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
 * Packet to sync companion capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncCompanionsPacket implements IMessageToClient {
    protected CompoundTag data;

    public SyncCompanionsPacket() {
        this.data = null;
    }
    
    public SyncCompanionsPacket(Player player) {
        IPlayerCompanions companions = PrimalMagickCapabilities.getCompanions(player);
        this.data = (companions != null) ? companions.serializeNBT() : null;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(SyncCompanionsPacket message, FriendlyByteBuf buf) {
        buf.writeNbt(message.data);
    }
    
    public static SyncCompanionsPacket decode(FriendlyByteBuf buf) {
        SyncCompanionsPacket message = new SyncCompanionsPacket();
        message.data = buf.readNbt();
        return message;
    }
    
    public static void onMessage(SyncCompanionsPacket message, CustomPayloadEvent.Context ctx) {
        Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
        IPlayerCompanions companions = PrimalMagickCapabilities.getCompanions(player);
        if (companions != null) {
            companions.deserializeNBT(message.data);
        }
    }
}
