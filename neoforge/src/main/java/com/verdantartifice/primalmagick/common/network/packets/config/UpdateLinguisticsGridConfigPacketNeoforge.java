package com.verdantartifice.primalmagick.common.network.packets.config;

import com.verdantartifice.primalmagick.common.books.grids.GridDefinition;
import com.verdantartifice.primalmagick.common.books.grids.GridDefinitionLoader;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class UpdateLinguisticsGridConfigPacketNeoforge implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UpdateLinguisticsGridConfigPacketNeoforge> TYPE = new CustomPacketPayload.Type<>(ResourceUtils.loc("update_linguistics_grid_neoforge"));
    public static final StreamCodec<FriendlyByteBuf, UpdateLinguisticsGridConfigPacketNeoforge> STREAM_CODEC = StreamCodec.ofMember(UpdateLinguisticsGridConfigPacketNeoforge::encode, UpdateLinguisticsGridConfigPacketNeoforge::decode);
    private static final Logger LOGGER = LogManager.getLogger();

    protected final Map<ResourceLocation, GridDefinition> gridDefs;

    public UpdateLinguisticsGridConfigPacketNeoforge(Map<ResourceLocation, GridDefinition> gridDefs) {
        this.gridDefs = new HashMap<>(gridDefs);
    }

    protected UpdateLinguisticsGridConfigPacketNeoforge(FriendlyByteBuf buf) {
        this.gridDefs = new HashMap<>();
        int mapSize = buf.readVarInt();
        for (int index = 0; index < mapSize; index++) {
            ResourceLocation loc = buf.readResourceLocation();
            GridDefinition gridDef = GridDefinition.streamCodec().decode(buf);
            this.gridDefs.put(loc, gridDef);
        }
    }

    public static void encode(UpdateLinguisticsGridConfigPacketNeoforge message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.gridDefs.size());
        for (Map.Entry<ResourceLocation, GridDefinition> entry : message.gridDefs.entrySet()) {
            buf.writeResourceLocation(entry.getKey());
            GridDefinition.streamCodec().encode(buf, entry.getValue());
        }
    }

    public static UpdateLinguisticsGridConfigPacketNeoforge decode(FriendlyByteBuf buf) {
        return new UpdateLinguisticsGridConfigPacketNeoforge(buf);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void onMessage(final UpdateLinguisticsGridConfigPacketNeoforge message, final IPayloadContext context) {
        context.enqueueWork(() -> {
            GridDefinitionLoader.getOrCreateInstance().replaceGridDefinitions(message.gridDefs);
        }).exceptionally(e -> {
            LOGGER.error("Config task failed to replace linguistics grid data");
            context.disconnect(Component.literal("Config task failed to replace linguistics grid data"));   // TODO Localize
            return null;
        }).thenAccept($ -> {
            // Reply with acknowledgement
            context.reply(new AcknowledgeLinguisticsGridConfigPacketNeoforge());
        });
    }
}
