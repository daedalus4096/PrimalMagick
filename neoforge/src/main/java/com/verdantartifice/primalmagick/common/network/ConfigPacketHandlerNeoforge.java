package com.verdantartifice.primalmagick.common.network;

import com.verdantartifice.primalmagick.common.network.packets.config.AcknowledgeAffinitiesConfigPacketNeoforge;
import com.verdantartifice.primalmagick.common.network.packets.config.UpdateAffinitiesConfigPacketNeoforge;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ConfigPacketHandlerNeoforge {
    public static final String REGISTRAR_VERSION = "1";

    public static void registerPayloadHandlers(final PayloadRegistrar registrar) {
        // TODO Register clientbound config packets
        registrar.configurationToClient(UpdateAffinitiesConfigPacketNeoforge.TYPE, UpdateAffinitiesConfigPacketNeoforge.STREAM_CODEC, UpdateAffinitiesConfigPacketNeoforge::onMessage);

        // TODO Register serverbound config packets
        registrar.configurationToServer(AcknowledgeAffinitiesConfigPacketNeoforge.TYPE, AcknowledgeAffinitiesConfigPacketNeoforge.STREAM_CODEC, AcknowledgeAffinitiesConfigPacketNeoforge::onMessage);
    }
}
