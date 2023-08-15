package com.verdantartifice.primalmagick;

import com.verdantartifice.primalmagick.common.config.Config;
import com.verdantartifice.primalmagick.common.init.InitRegistries;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

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
@Mod(PrimalMagick.MODID)
public class PrimalMagick {
    public static final String MODID = "primalmagick";
    
    public PrimalMagick() {
        Config.register();
        InitRegistries.initDeferredRegistries();
    }
    
    public static ResourceLocation resource(String name) {
        return new ResourceLocation(MODID, name);
    }
}
