package com.verdantartifice.primalmagic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagic.common.misc.ItemGroupPM;
import com.verdantartifice.primalmagic.proxy.ClientProxy;
import com.verdantartifice.primalmagic.proxy.IProxyPM;
import com.verdantartifice.primalmagic.proxy.ServerProxy;

import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(PrimalMagic.MODID)
public class PrimalMagic {
    public static final String MODID = "primalmagic";
    public static final Logger LOGGER = LogManager.getLogger(PrimalMagic.MODID);
    public static final ItemGroup ITEM_GROUP = new ItemGroupPM();
    
    public static IProxyPM proxy = DistExecutor.runForDist(()->()->new ClientProxy(), ()->()->new ServerProxy());
}
