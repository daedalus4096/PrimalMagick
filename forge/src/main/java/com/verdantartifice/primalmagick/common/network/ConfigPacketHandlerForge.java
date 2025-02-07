package com.verdantartifice.primalmagick.common.network;

import com.verdantartifice.primalmagick.common.network.packets.config.AcknowledgeAffinitiesConfigPacketForge;
import com.verdantartifice.primalmagick.common.network.packets.config.AcknowledgeLinguisticsGridConfigPacketForge;
import com.verdantartifice.primalmagick.common.network.packets.config.UpdateAffinitiesConfigPacketForge;
import com.verdantartifice.primalmagick.common.network.packets.config.UpdateLinguisticsGridConfigPacketForge;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigPacketHandlerForge {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation CHANNEL_NAME = ResourceUtils.loc("config_channel_forge");
    private static final int PROTOCOL_VERSION = 1;

    private static final SimpleChannel CHANNEL = ChannelBuilder
            .named(CHANNEL_NAME)
            .networkProtocolVersion(PROTOCOL_VERSION)
            .clientAcceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION))
            .serverAcceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION))
            .simpleChannel()
                .configuration()
                    .clientbound()
                        // Register clientbound config packets
                        .add(UpdateAffinitiesConfigPacketForge.class, UpdateAffinitiesConfigPacketForge.STREAM_CODEC, UpdateAffinitiesConfigPacketForge::onMessage)
                        .add(UpdateLinguisticsGridConfigPacketForge.class, UpdateLinguisticsGridConfigPacketForge.STREAM_CODEC, UpdateLinguisticsGridConfigPacketForge::onMessage)
                    .serverbound()
                        // Register serverbound config packets
                        .add(AcknowledgeAffinitiesConfigPacketForge.class, AcknowledgeAffinitiesConfigPacketForge.STREAM_CODEC, AcknowledgeAffinitiesConfigPacketForge::onMessage)
                        .add(AcknowledgeLinguisticsGridConfigPacketForge.class, AcknowledgeLinguisticsGridConfigPacketForge.STREAM_CODEC, AcknowledgeLinguisticsGridConfigPacketForge::onMessage)
            .build();

    public static void registerMessages() {
        // The class just needs to be externally referenced by a loaded class in order to be class-loaded itself and have its SimpleChannel initialized statically
        LOGGER.debug("Registering network {} v{}", CHANNEL.getName(), CHANNEL.getProtocolVersion());
    }

    public static void sendOverConnection(Object message, Connection connection) {
        // Send a config packet over the given connection
        CHANNEL.send(message, connection);
    }
}
