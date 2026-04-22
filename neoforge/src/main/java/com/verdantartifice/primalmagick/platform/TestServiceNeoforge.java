package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.ITestService;
import net.minecraft.network.Connection;
import net.neoforged.neoforge.network.registration.NetworkRegistry;

public class TestServiceNeoforge implements ITestService {
    @Override
    public void configureMockConnection(Connection connection) {
        NetworkRegistry.configureMockConnection(connection);
    }
}
