package com.verdantartifice.primalmagic.proxy;

import com.verdantartifice.primalmagic.client.gui.ArcaneWorkbenchScreen;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.common.containers.ContainersPM;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void clientSetup(FMLClientSetupEvent event) {
        super.clientSetup(event);
        this.registerScreens();
    }
    
    private void registerScreens() {
        ScreenManager.registerFactory(ContainersPM.GRIMOIRE, GrimoireScreen::new);
        ScreenManager.registerFactory(ContainersPM.ARCANE_WORKBENCH, ArcaneWorkbenchScreen::new);
    }
}
