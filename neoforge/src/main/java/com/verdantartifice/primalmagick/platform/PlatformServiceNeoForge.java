package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IPlatformService;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

public class PlatformServiceNeoForge implements IPlatformService {
    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }
}
