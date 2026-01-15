package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IPlatformService;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;

public class PlatformServiceNeoforge implements IPlatformService {
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
        FMLLoader loader = FMLLoader.getCurrentOrNull();
        return loader != null && !loader.isProduction();
    }

    @Override
    public boolean isClientDist() {
        return FMLEnvironment.getDist().isClient();
    }
}
