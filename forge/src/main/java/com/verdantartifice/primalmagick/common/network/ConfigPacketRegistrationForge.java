package com.verdantartifice.primalmagick.common.network;

import com.verdantartifice.primalmagick.common.network.packets.config.UpdateAffinitiesConfigPacket;
import com.verdantartifice.primalmagick.common.network.packets.config.UpdateAffinitiesConfigPacketForge;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigPacketRegistrationForge {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation CHANNEL_NAME = ResourceUtils.loc("config_channel");
    private static final int PROTOCOL_VERSION = 1;

    private static final SimpleChannel CHANNEL = ChannelBuilder
            .named(CHANNEL_NAME)
            .clientAcceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION))
            .serverAcceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION))
            .networkProtocolVersion(PROTOCOL_VERSION)
            .simpleChannel()
                .configuration()
                    .clientbound()
                        // TODO Register clientbound config packets
                        .add(UpdateAffinitiesConfigPacketForge.class, UpdateAffinitiesConfigPacketForge.STREAM_CODEC, UpdateAffinitiesConfigPacketForge::onMessage)
                    .serverbound()
                        // TODO Register serverbound config packets
            .build();

    public static void registerMessages() {
        // The class just needs to be externally referenced by a loaded class in order to be class-loaded itself and have its SimpleChannel initialized statically
        LOGGER.debug("Registering network {} v{}", CHANNEL.getName(), CHANNEL.getProtocolVersion());
    }
}
