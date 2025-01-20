package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.network.ConfigPacketRegistrationForge;
import com.verdantartifice.primalmagick.platform.services.INetworkService;

public class NetworkServiceForge implements INetworkService {
    @Override
    public void registerConfigMessages() {
        ConfigPacketRegistrationForge.registerMessages();
    }
}
