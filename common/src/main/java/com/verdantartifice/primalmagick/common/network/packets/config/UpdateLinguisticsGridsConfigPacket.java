package com.verdantartifice.primalmagick.common.network.packets.config;

import com.verdantartifice.primalmagick.common.books.grids.GridDefinition;
import com.verdantartifice.primalmagick.common.books.grids.GridDefinitionLoader;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.FriendlyByteBuf;
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
public class UpdateLinguisticsGridsConfigPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("update_linguistics_grids_config");
    public static final StreamCodec<FriendlyByteBuf, UpdateLinguisticsGridsConfigPacket> STREAM_CODEC = StreamCodec.ofMember(UpdateLinguisticsGridsConfigPacket::encode, UpdateLinguisticsGridsConfigPacket::decode);
    private static final int NO_REPLY = -1;

    protected final int token;
    protected final Map<ResourceLocation, GridDefinition> gridDefs;
    
    public UpdateLinguisticsGridsConfigPacket(Map<ResourceLocation, GridDefinition> gridDefs) {
        this(NO_REPLY, gridDefs);
    }
    
    public UpdateLinguisticsGridsConfigPacket(int token, Map<ResourceLocation, GridDefinition> gridDefs) {
        this.token = token;
        this.gridDefs = new HashMap<>(gridDefs);
    }
    
    protected UpdateLinguisticsGridsConfigPacket(FriendlyByteBuf buf) {
        this.token = buf.readVarInt();
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

    public static void encode(UpdateLinguisticsGridsConfigPacket message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.token);
        buf.writeVarInt(message.gridDefs.size());
        for (Map.Entry<ResourceLocation, GridDefinition> entry : message.gridDefs.entrySet()) {
            buf.writeResourceLocation(entry.getKey());
            GridDefinition.streamCodec().encode(buf, entry.getValue());
        }
    }
    
    public static UpdateLinguisticsGridsConfigPacket decode(FriendlyByteBuf buf) {
        return new UpdateLinguisticsGridsConfigPacket(buf);
    }
    
    public static void onMessage(PacketContext<UpdateLinguisticsGridsConfigPacket> ctx) {
        UpdateLinguisticsGridsConfigPacket message = ctx.message();
        GridDefinitionLoader.createInstance().replaceGridDefinitions(message.gridDefs);
        if (message.token != NO_REPLY) {
            PacketHandler.sendToServer(new AcknowledgementPacket(message.token));
        }
    }
}
