package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Packet to sync a menu data slot value from the server to the client as a varint, to allow for
 * larger data values than vanilla.
 * 
 * @author Daedalus4096
 */
public class ContainerSetVarintDataPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("container_set_varint_data");
    public static final StreamCodec<RegistryFriendlyByteBuf, ContainerSetVarintDataPacket> STREAM_CODEC = StreamCodec.ofMember(ContainerSetVarintDataPacket::encode, ContainerSetVarintDataPacket::decode);

    private final int containerId;
    private final int slotId;
    private final int dataValue;

    public ContainerSetVarintDataPacket(int containerId, int slotId, int dataValue) {
        this.containerId = containerId;
        this.slotId = slotId;
        this.dataValue = dataValue;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(ContainerSetVarintDataPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(message.containerId);
        buf.writeVarInt(message.slotId);
        buf.writeVarInt(message.dataValue);
    }
    
    public static ContainerSetVarintDataPacket decode(RegistryFriendlyByteBuf buf) {
        return new ContainerSetVarintDataPacket(buf.readVarInt(), buf.readVarInt(), buf.readVarInt());
    }
    
    public static void onMessage(PacketContext<ContainerSetVarintDataPacket> ctx) {
        ContainerSetVarintDataPacket message = ctx.message();
        Player player = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentPlayer() : null;
        if (player != null && player.containerMenu != null && player.containerMenu.containerId == message.containerId) {
            player.containerMenu.setData(message.slotId, message.dataValue);
        }
    }
}
