package com.verdantartifice.primalmagick;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.config.Config;
import com.verdantartifice.primalmagick.common.init.InitRegistries;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
    private static FMLJavaModLoadingContext context = null;
    
    public PrimalMagick(FMLJavaModLoadingContext context) {
        PrimalMagick.context = context;
        Config.register();
        InitRegistries.initDeferredRegistries();
    }
    
    public static ResourceLocation resource(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }
    
    @Nullable
    public static FMLJavaModLoadingContext getModLoadingContext() {
        return PrimalMagick.context;
    }
}
