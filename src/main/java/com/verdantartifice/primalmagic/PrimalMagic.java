package com.verdantartifice.primalmagic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(PrimalMagic.MODID)
@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class PrimalMagic {
    public static final String MODID = "primalmagic";
    
    public static Logger LOGGER = LogManager.getLogger(PrimalMagic.MODID);

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        LOGGER.info("Hello from Primal Magic init!");
    }
}
