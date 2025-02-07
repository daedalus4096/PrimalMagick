package com.verdantartifice.primalmagick.common.network.tasks;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.network.ConfigPacketHandlerForge;
import com.verdantartifice.primalmagick.common.network.packets.config.AcknowledgeAffinitiesConfigPacketForge;
import com.verdantartifice.primalmagick.common.network.packets.config.UpdateAffinitiesConfigPacketForge;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.config.ConfigurationTaskContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

/**
 * Forge configuration task that syncs affinity datapack contents from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncAffinityDataTaskForge implements ConfigurationTask {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final Type TYPE = new Type(Constants.MOD_ID + ":sync_affinity_data");

    private ConfigurationTaskContext taskCtx;

    @Override
    public void start(ConfigurationTaskContext ctx) {
        this.taskCtx = ctx;

        // If we're in memory, no syncing necessary
        if (this.taskCtx.getConnection().isMemoryConnection()) {
            this.taskCtx.finish(type());
            return;
        }

        // Set up callback expectation, then send the sync packet
        AcknowledgeAffinitiesConfigPacketForge.expect(this::finish);
        LOGGER.debug("Pushing affinity data to client");
        ConfigPacketHandlerForge.sendOverConnection(new UpdateAffinitiesConfigPacketForge(AffinityManager.getInstance().getAllAffinities()), this.taskCtx.getConnection());
    }

    private void finish(AcknowledgeAffinitiesConfigPacketForge message, CustomPayloadEvent.Context ctx) {
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
