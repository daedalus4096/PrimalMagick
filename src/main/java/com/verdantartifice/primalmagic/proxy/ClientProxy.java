package com.verdantartifice.primalmagic.proxy;

import com.verdantartifice.primalmagic.client.config.KeyBindings;
import com.verdantartifice.primalmagic.client.gui.AnalysisTableScreen;
import com.verdantartifice.primalmagic.client.gui.ArcaneWorkbenchScreen;
import com.verdantartifice.primalmagic.client.gui.CalcinatorScreen;
import com.verdantartifice.primalmagic.client.gui.SpellcraftingAltarScreen;
import com.verdantartifice.primalmagic.client.gui.WandAssemblyTableScreen;
import com.verdantartifice.primalmagic.client.gui.WandChargerScreen;
import com.verdantartifice.primalmagic.client.gui.WandInscriptionTableScreen;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.client.renderers.entity.SpellMineRenderer;
import com.verdantartifice.primalmagic.client.renderers.entity.SpellProjectileRenderer;
import com.verdantartifice.primalmagic.client.renderers.tile.AncientManaFontTER;
import com.verdantartifice.primalmagic.client.renderers.tile.WandChargerTER;
import com.verdantartifice.primalmagic.common.containers.ContainersPM;
import com.verdantartifice.primalmagic.common.entities.projectiles.SpellMineEntity;
import com.verdantartifice.primalmagic.common.entities.projectiles.SpellProjectileEntity;
import com.verdantartifice.primalmagic.common.tiles.mana.AncientManaFontTileEntity;
import com.verdantartifice.primalmagic.common.tiles.mana.WandChargerTileEntity;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client sided proxy.  Handles client setup issues and provides side-dependent utility methods.
 * 
 * @author Daedalus4096
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void clientSetup(FMLClientSetupEvent event) {
        super.clientSetup(event);
        this.registerKeybinds();
        this.registerScreens();
        this.registerTERs();
        this.registerEntityRenderers();
    }
    
    private void registerKeybinds() {
        KeyBindings.init();
    }
    
    private void registerScreens() {
        // Register screen factories for each container
        ScreenManager.registerFactory(ContainersPM.GRIMOIRE, GrimoireScreen::new);
        ScreenManager.registerFactory(ContainersPM.ARCANE_WORKBENCH, ArcaneWorkbenchScreen::new);
        ScreenManager.registerFactory(ContainersPM.WAND_ASSEMBLY_TABLE, WandAssemblyTableScreen::new);
        ScreenManager.registerFactory(ContainersPM.ANALYSIS_TABLE, AnalysisTableScreen::new);
        ScreenManager.registerFactory(ContainersPM.CALCINATOR, CalcinatorScreen::new);
        ScreenManager.registerFactory(ContainersPM.WAND_INSCRIPTION_TABLE, WandInscriptionTableScreen::new);
        ScreenManager.registerFactory(ContainersPM.SPELLCRAFTING_ALTAR, SpellcraftingAltarScreen::new);
        ScreenManager.registerFactory(ContainersPM.WAND_CHARGER, WandChargerScreen::new);
    }
    
    private void registerTERs() {
        // Register tile entity renderers for those tile entities that need them
        ClientRegistry.bindTileEntitySpecialRenderer(AncientManaFontTileEntity.class, new AncientManaFontTER());
        ClientRegistry.bindTileEntitySpecialRenderer(WandChargerTileEntity.class, new WandChargerTER());
    }
    
    private void registerEntityRenderers() {
        // Register renderers for each entity type
        RenderingRegistry.registerEntityRenderingHandler(SpellProjectileEntity.class, SpellProjectileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(SpellMineEntity.class, SpellMineRenderer::new);
    }
    
    @Override
    public boolean isShiftDown() {
        return Screen.hasShiftDown();
    }
}
