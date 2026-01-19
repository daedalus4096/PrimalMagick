package com.verdantartifice.primalmagick.common.network.packets.config;

import com.verdantartifice.primalmagick.common.books.grids.GridDefinition;
import com.verdantartifice.primalmagick.common.books.grids.GridDefinitionLoader;
import com.verdantartifice.primalmagick.common.network.ConfigPacketHandlerForge;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class UpdateLinguisticsGridConfigPacketForge implements IMessageToClient {
    public static final StreamCodec<FriendlyByteBuf, UpdateLinguisticsGridConfigPacketForge> STREAM_CODEC = StreamCodec.ofMember(UpdateLinguisticsGridConfigPacketForge::encode, UpdateLinguisticsGridConfigPacketForge::decode);
    private static final Logger LOGGER = LogManager.getLogger();

    protected final Map<Identifier, GridDefinition> gridDefs;

    public UpdateLinguisticsGridConfigPacketForge(Map<Identifier, GridDefinition> gridDefs) {
        this.gridDefs = new HashMap<>(gridDefs);
    }

    protected UpdateLinguisticsGridConfigPacketForge(FriendlyByteBuf buf) {
        this.gridDefs = new HashMap<>();
        int mapSize = buf.readVarInt();
        for (int index = 0; index < mapSize; index++) {
            Identifier loc = buf.readIdentifier();
            GridDefinition gridDef = GridDefinition.streamCodec().decode(buf);
            this.gridDefs.put(loc, gridDef);
        }
    }

    public static void encode(UpdateLinguisticsGridConfigPacketForge message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.gridDefs.size());
        message.gridDefs.forEach((id, def) -> {
            buf.writeIdentifier(id);
            GridDefinition.streamCodec().encode(buf, def);
        });
    }

    public static UpdateLinguisticsGridConfigPacketForge decode(FriendlyByteBuf buf) {
        return new UpdateLinguisticsGridConfigPacketForge(buf);
    }

    public static void onMessage(UpdateLinguisticsGridConfigPacketForge message, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            GridDefinitionLoader.getOrCreateInstance().replaceGridDefinitions(message.gridDefs);
        }).exceptionally(e -> {
            LOGGER.error("Config task failed to replace linguistics grid data");
            ctx.getConnection().disconnect(Component.literal("Config task failed to replace linguistics grid data"));   // TODO Localize
            return null;
        }).thenAccept($ -> {
            ConfigPacketHandlerForge.sendOverConnection(AcknowledgeLinguisticsGridConfigPacketForge.INSTANCE, ctx.getConnection());
        });
        ctx.setPacketHandled(true);
    }
}
