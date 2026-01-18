package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.ITestService;
import net.minecraft.network.Connection;

public class TestServiceForge implements ITestService {
    @Override
    public void configureMockConnection(Connection connection) {
        // Nothing to do
    }
}
