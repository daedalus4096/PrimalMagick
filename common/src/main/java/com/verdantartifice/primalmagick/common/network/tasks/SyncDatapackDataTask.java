package com.verdantartifice.primalmagick.common.network.tasks;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.config.AcknowledgementPacket;
import com.verdantartifice.primalmagick.common.network.packets.config.UpdateAffinitiesConfigPacket;
import com.verdantartifice.primalmagick.common.network.packets.config.UpdateLinguisticsGridsConfigPacket;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.config.ConfigurationTaskContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

/**
 * Configuration task that syncs datapack contents from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncDatapackDataTask implements ConfigurationTask {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final Type TYPE = new Type(Constants.MOD_ID + ":sync_datapack_data");

    private ConfigurationTaskContext taskCtx;
    private int expectedToken;

    @Override
    public void start(ConfigurationTaskContext ctx) {
        this.taskCtx = ctx;
        Connection conn = ctx.getConnection();
        
        // If we're in memory, no syncing necessary
        if (this.taskCtx.getConnection().isMemoryConnection()) {
            this.taskCtx.finish(type());
            return;
        }
        
        // First update affinity data, then move on to the next step
        this.expectedToken = AcknowledgementPacket.expect(this::updateLinguisticsGrids);
        LOGGER.debug("Pushing affinity data to client");
        PacketHandler.sendOverConnection(new UpdateAffinitiesConfigPacket(this.expectedToken, AffinityManager.getInstance().getAllAffinities()), conn);
    }
    
    private void updateLinguisticsGrids(AcknowledgementPacket msg, CustomPayloadEvent.Context ctx) {
        if (msg.token() != this.expectedToken) {
            LOGGER.error("Received unknown acknowledgement: received {}, expected {}", msg.token(), this.expectedToken);
            ctx.getConnection().disconnect(Component.literal("Unexpected AcknowledgementPacket received, unknown token: " + msg.token()));
            return;
        }

        // Next update linguistics grid data, then finish
        this.expectedToken = AcknowledgementPacket.expect(this::finish);
        LOGGER.debug("Pushing linguistics grid data to client");
        PacketHandler.reply(new UpdateLinguisticsGridsConfigPacket(this.expectedToken, LinguisticsManager.getAllGridDefinitions()), ctx);
    }

    private void finish(AcknowledgementPacket msg, CustomPayloadEvent.Context ctx) {
        this.taskCtx.finish(type());
    }

    @Override
    public void start(Consumer<Packet<?>> pTask) {
        throw new IllegalStateException("This should never be called");
    }

    @Override
    public Type type() {
        return TYPE;
    }
}
