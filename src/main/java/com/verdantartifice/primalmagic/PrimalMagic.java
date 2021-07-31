package com.verdantartifice.primalmagic;

import com.verdantartifice.primalmagic.common.config.Config;
import com.verdantartifice.primalmagic.common.init.InitRegistries;
import com.verdantartifice.primalmagic.common.misc.ItemGroupPM;
import com.verdantartifice.primalmagic.proxy.ClientProxy;
import com.verdantartifice.primalmagic.proxy.IProxyPM;
import com.verdantartifice.primalmagic.proxy.ServerProxy;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

/**
 * Main class of the Primal Magic mod.  Most initialization doesn't happen here,
 * but rather in response to Forge events.
 * 
 * @see {@link com.verdantartifice.primalmagic.common.events.ModLifecycleEvents}
 * @see {@link com.verdantartifice.primalmagic.common.events.ServerLifecycleEvents}
 * @see {@link com.verdantartifice.primalmagic.client.events.ClientModLifecycleEvents}
 * 
 * @author Daedalus4096
 */
@Mod(PrimalMagic.MODID)
public class PrimalMagic {
    public static final String MODID = "primalmagic";
    public static final CreativeModeTab ITEM_GROUP = new ItemGroupPM();
    
    public static IProxyPM proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    
    public PrimalMagic() {
        Config.register();
        InitRegistries.initDeferredRegistries();
    }
}
