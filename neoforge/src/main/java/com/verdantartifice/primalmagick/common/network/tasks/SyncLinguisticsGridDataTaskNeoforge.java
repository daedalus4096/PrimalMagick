package com.verdantartifice.primalmagick.common.network.tasks;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.network.packets.config.UpdateLinguisticsGridConfigPacketNeoforge;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.configuration.ICustomConfigurationTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class SyncLinguisticsGridDataTaskNeoforge implements ICustomConfigurationTask {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final Type TYPE = new Type(Constants.MOD_ID + ":sync_linguistics_grid_data");

    @Override
    public void run(Consumer<CustomPacketPayload> consumer) {
        LOGGER.debug("Pushing linguistics grid data to client");
        consumer.accept(new UpdateLinguisticsGridConfigPacketNeoforge(LinguisticsManager.getAllGridDefinitions()));
    }

    @Override
    public Type type() {
        return TYPE;
    }
}
