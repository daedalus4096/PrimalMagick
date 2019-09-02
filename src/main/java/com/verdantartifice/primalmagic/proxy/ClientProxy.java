package com.verdantartifice.primalmagic.proxy;

import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.common.containers.ContainersPM;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLCommonSetupEvent event) {
        super.preInit(event);
        this.registerScreens();
    }
    
    private void registerScreens() {
        ScreenManager.registerFactory(ContainersPM.GRIMOIRE, GrimoireScreen::new);
    }
}
