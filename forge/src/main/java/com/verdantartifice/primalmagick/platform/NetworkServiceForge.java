package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.network.ConfigPacketHandlerForge;
import com.verdantartifice.primalmagick.platform.services.INetworkService;

public class NetworkServiceForge implements INetworkService {
    @Override
    public void registerConfigMessages() {
        ConfigPacketHandlerForge.registerMessages();
    }
}
