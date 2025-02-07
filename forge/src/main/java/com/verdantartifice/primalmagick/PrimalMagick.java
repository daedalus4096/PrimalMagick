package com.verdantartifice.primalmagick;

import com.verdantartifice.primalmagick.common.config.ConfigForge;
import com.verdantartifice.primalmagick.common.init.InitRegistriesForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import javax.annotation.Nullable;

/**
 * Main class of the Primal Magick mod.  Most initialization doesn't happen here,
 * but rather in response to Forge events.
 *
 * @see com.verdantartifice.primalmagick.common.events.ModLifecycleEventListeners
 * @see com.verdantartifice.primalmagick.common.events.ServerLifecycleEventListeners
 * @see com.verdantartifice.primalmagick.client.events.ClientModLifecycleEventListeners
 * 
 * @author Daedalus4096
 */
@Mod(Constants.MOD_ID)
public class PrimalMagick {
    private static FMLJavaModLoadingContext context = null;
    
    public PrimalMagick(FMLJavaModLoadingContext context) {
        PrimalMagick.context = context;
        ConfigForge.register();
        InitRegistriesForge.initDeferredRegistries();
    }

    @Nullable
    public static FMLJavaModLoadingContext getModLoadingContext() {
        return PrimalMagick.context;
    }
}
