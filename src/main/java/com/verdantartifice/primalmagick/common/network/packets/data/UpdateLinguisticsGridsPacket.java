package com.verdantartifice.primalmagick.common.network.packets.data;

import java.util.HashMap;
import java.util.Map;

import com.verdantartifice.primalmagick.common.books.grids.GridDefinition;
import com.verdantartifice.primalmagick.common.books.grids.GridDefinitionLoader;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet to update linguistics comprehension grid JSON data on the client from the server.
 * 
 * @author Daedalus4096
 */
public class UpdateLinguisticsGridsPacket implements IMessageToClient {
    protected final Map<ResourceLocation, GridDefinition> gridDefs;
    
    public UpdateLinguisticsGridsPacket(Map<ResourceLocation, GridDefinition> gridDefs) {
        this.gridDefs = new HashMap<>(gridDefs);
    }
    
    protected UpdateLinguisticsGridsPacket(FriendlyByteBuf buf) {
        this.gridDefs = new HashMap<>();
        int mapSize = buf.readVarInt();
        for (int index = 0; index < mapSize; index++) {
            ResourceLocation loc = buf.readResourceLocation();
            GridDefinition gridDef = GridDefinition.SERIALIZER.fromNetwork(buf);
            this.gridDefs.put(loc, gridDef);
        }
    }
    
    public Map<ResourceLocation, GridDefinition> getGridDefinitions() {
        return this.gridDefs;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(UpdateLinguisticsGridsPacket message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.gridDefs.size());
        for (Map.Entry<ResourceLocation, GridDefinition> entry : message.gridDefs.entrySet()) {
            buf.writeResourceLocation(entry.getKey());
            GridDefinition.SERIALIZER.toNetwork(buf, entry.getValue());
        }
    }
    
    public static UpdateLinguisticsGridsPacket decode(FriendlyByteBuf buf) {
        return new UpdateLinguisticsGridsPacket(buf);
    }
    
    public static void onMessage(UpdateLinguisticsGridsPacket message, CustomPayloadEvent.Context ctx) {
        GridDefinitionLoader.createInstance().replaceGridDefinitions(message.getGridDefinitions());
    }
}
