package com.verdantartifice.primalmagick;

import com.verdantartifice.primalmagick.common.config.Config;
import com.verdantartifice.primalmagick.common.init.InitRegistries;
import com.verdantartifice.primalmagick.common.misc.ItemGroupPM;

import net.minecraft.world.item.CreativeModeTab;
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
    public static final CreativeModeTab ITEM_GROUP = new ItemGroupPM();
    
    public PrimalMagick() {
        Config.register();
        InitRegistries.initDeferredRegistries();
    }
}
