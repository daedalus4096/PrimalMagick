package com.verdantartifice.primalmagic.proxy;

import com.verdantartifice.primalmagic.client.config.KeyBindings;
import com.verdantartifice.primalmagic.client.gui.AnalysisTableScreen;
import com.verdantartifice.primalmagic.client.gui.ArcaneWorkbenchScreen;
import com.verdantartifice.primalmagic.client.gui.CalcinatorScreen;
import com.verdantartifice.primalmagic.client.gui.WandAssemblyTableScreen;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.client.renderers.tile.AncientManaFontTER;
import com.verdantartifice.primalmagic.common.containers.ContainersPM;
import com.verdantartifice.primalmagic.common.tiles.mana.AncientManaFontTileEntity;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void clientSetup(FMLClientSetupEvent event) {
        super.clientSetup(event);
        this.registerKeybinds();
        this.registerScreens();
        this.registerTERs();
    }
    
    private void registerKeybinds() {
        KeyBindings.init();
    }
    
    private void registerScreens() {
        ScreenManager.registerFactory(ContainersPM.GRIMOIRE, GrimoireScreen::new);
        ScreenManager.registerFactory(ContainersPM.ARCANE_WORKBENCH, ArcaneWorkbenchScreen::new);
        ScreenManager.registerFactory(ContainersPM.WAND_ASSEMBLY_TABLE, WandAssemblyTableScreen::new);
        ScreenManager.registerFactory(ContainersPM.ANALYSIS_TABLE, AnalysisTableScreen::new);
        ScreenManager.registerFactory(ContainersPM.CALCINATOR, CalcinatorScreen::new);
    }
    
    private void registerTERs() {
        ClientRegistry.bindTileEntitySpecialRenderer(AncientManaFontTileEntity.class, new AncientManaFontTER());
    }
    
    @Override
    public boolean isShiftDown() {
        return Screen.hasShiftDown();
    }
}
