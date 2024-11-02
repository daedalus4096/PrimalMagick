package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.common.books.grids.GridDefinition;
import com.verdantartifice.primalmagick.common.books.grids.GridDefinitionLoader;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Packet to update linguistics comprehension grid JSON data on the client from the server.
 * 
 * @author Daedalus4096
 */
public class UpdateLinguisticsGridsPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("update_linguistics_grids");
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateLinguisticsGridsPacket> STREAM_CODEC = StreamCodec.ofMember(UpdateLinguisticsGridsPacket::encode, UpdateLinguisticsGridsPacket::decode);

    protected final Map<ResourceLocation, GridDefinition> gridDefs;
    
    public UpdateLinguisticsGridsPacket(Map<ResourceLocation, GridDefinition> gridDefs) {
        this.gridDefs = new HashMap<>(gridDefs);
    }
    
    protected UpdateLinguisticsGridsPacket(RegistryFriendlyByteBuf buf) {
        this.gridDefs = new HashMap<>();
        int mapSize = buf.readVarInt();
        for (int index = 0; index < mapSize; index++) {
            ResourceLocation loc = buf.readResourceLocation();
            GridDefinition gridDef = GridDefinition.streamCodec().decode(buf);
            this.gridDefs.put(loc, gridDef);
        }
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(UpdateLinguisticsGridsPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(message.gridDefs.size());
        for (Map.Entry<ResourceLocation, GridDefinition> entry : message.gridDefs.entrySet()) {
            buf.writeResourceLocation(entry.getKey());
            GridDefinition.streamCodec().encode(buf, entry.getValue());
        }
    }
    
    public static UpdateLinguisticsGridsPacket decode(RegistryFriendlyByteBuf buf) {
        return new UpdateLinguisticsGridsPacket(buf);
    }
    
    public static void onMessage(PacketContext<UpdateLinguisticsGridsPacket> ctx) {
        UpdateLinguisticsGridsPacket message = ctx.message();
        GridDefinitionLoader.getOrCreateInstance().replaceGridDefinitions(message.gridDefs);
    }
}
