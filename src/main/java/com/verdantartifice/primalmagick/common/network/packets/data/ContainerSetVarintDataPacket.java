package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Packet to sync a menu data slot value from the server to the client as a varint, to allow for
 * larger data values than vanilla.
 * 
 * @author Daedalus4096
 */
public class ContainerSetVarintDataPacket implements IMessageToClient {
    public static final StreamCodec<RegistryFriendlyByteBuf, ContainerSetVarintDataPacket> STREAM_CODEC = StreamCodec.ofMember(ContainerSetVarintDataPacket::encode, ContainerSetVarintDataPacket::decode);

    private final int containerId;
    private final int slotId;
    private final int dataValue;

    public ContainerSetVarintDataPacket(int containerId, int slotId, int dataValue) {
        this.containerId = containerId;
        this.slotId = slotId;
        this.dataValue = dataValue;
    }
    
    public static void encode(ContainerSetVarintDataPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(message.containerId);
        buf.writeVarInt(message.slotId);
        buf.writeVarInt(message.dataValue);
    }
    
    public static ContainerSetVarintDataPacket decode(RegistryFriendlyByteBuf buf) {
        return new ContainerSetVarintDataPacket(buf.readVarInt(), buf.readVarInt(), buf.readVarInt());
    }
    
    public static void onMessage(ContainerSetVarintDataPacket message, CustomPayloadEvent.Context ctx) {
        Player player = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentPlayer() : null;
        if (player != null && player.containerMenu != null && player.containerMenu.containerId == message.containerId) {
            player.containerMenu.setData(message.slotId, message.dataValue);
        }
    }
}
