package com.verdantartifice.primalmagick.common.network.tasks;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.network.packets.config.UpdateAffinitiesConfigPacketNeoforge;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.configuration.ICustomConfigurationTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

/**
 * Neoforge configuration task that syncs affinity datapack contents from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncAffinityDataTaskNeoforge implements ICustomConfigurationTask {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final Type TYPE = new Type(Constants.MOD_ID + ":sync_affinity_data");

    @Override
    public void run(Consumer<CustomPacketPayload> consumer) {
        LOGGER.debug("Pushing affinity data to client");
        consumer.accept(new UpdateAffinitiesConfigPacketNeoforge(AffinityManager.getInstance().getAllAffinities()));
    }

    @Override
    public Type type() {
        return TYPE;
    }
}
