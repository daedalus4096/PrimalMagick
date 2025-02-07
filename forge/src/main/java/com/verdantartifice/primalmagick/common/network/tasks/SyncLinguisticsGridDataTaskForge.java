package com.verdantartifice.primalmagick.common.network.tasks;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.network.ConfigPacketHandlerForge;
import com.verdantartifice.primalmagick.common.network.packets.config.AcknowledgeLinguisticsGridConfigPacketForge;
import com.verdantartifice.primalmagick.common.network.packets.config.UpdateLinguisticsGridConfigPacketForge;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.config.ConfigurationTaskContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class SyncLinguisticsGridDataTaskForge implements ConfigurationTask {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final Type TYPE = new Type(Constants.MOD_ID + ":sync_linguistics_grid_data");

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
        AcknowledgeLinguisticsGridConfigPacketForge.expect(this::finish);
        LOGGER.debug("Pushing linguistics grid data to client");
        ConfigPacketHandlerForge.sendOverConnection(new UpdateLinguisticsGridConfigPacketForge(LinguisticsManager.getAllGridDefinitions()), this.taskCtx.getConnection());
    }

    private void finish(AcknowledgeLinguisticsGridConfigPacketForge message, CustomPayloadEvent.Context ctx) {
        this.taskCtx.finish(type());
    }

    @Override
    public void start(Consumer<Packet<?>> consumer) {
        throw new IllegalStateException("This should never be called");
    }

    @Override
    public Type type() {
        return TYPE;
    }
}
