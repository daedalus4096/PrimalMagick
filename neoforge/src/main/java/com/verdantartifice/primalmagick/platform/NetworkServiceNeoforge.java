package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.INetworkService;
import org.apache.commons.lang3.NotImplementedException;

public class NetworkServiceNeoforge implements INetworkService {
    @Override
    public void registerConfigMessages() {
        // Neoforge network payload registration is done in response to a dedicated event, rather than during common
        // setup, so nothing needs to be done here.
    }
}
