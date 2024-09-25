package com.verdantartifice.primalmagick;

import com.verdantartifice.primalmagick.common.config.Config;
import com.verdantartifice.primalmagick.common.init.InitRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import javax.annotation.Nullable;

/**
 * Main class of the Primal Magick mod.  Most initialization doesn't happen here,
 * but rather in response to Forge events.
 * 
 * @see {@link com.verdantartifice.primalmagick.common.events.ModLifecycleEvents}
 * @see {@link com.verdantartifice.primalmagick.common.events.ServerLifecycleEvents}
 * @see {@link com.verdantartifice.primalmagick.client.events.ClientModLifecycleEvents}
 * 
 * @author Daedalus4096
 */
@Mod(Constants.MOD_ID)
public class PrimalMagick {
    private static FMLJavaModLoadingContext context = null;
    
    public PrimalMagick(FMLJavaModLoadingContext context) {
        PrimalMagick.context = context;
        Config.register();
        InitRegistries.initDeferredRegistries();
    }

    @Nullable
    public static FMLJavaModLoadingContext getModLoadingContext() {
        return PrimalMagick.context;
    }
}
