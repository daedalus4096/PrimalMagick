package com.verdantartifice.primalmagic.proxy;

import com.verdantartifice.primalmagic.client.config.KeyBindings;
import com.verdantartifice.primalmagic.client.fx.particles.ParticleTypesPM;
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
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.containers.ContainersPM;
import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
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
    public void initDeferredRegistries() {
        super.initDeferredRegistries();
        ParticleTypesPM.init();
    }
    
    @Override
    public void clientSetup(FMLClientSetupEvent event) {
        super.clientSetup(event);
        this.registerKeybinds();
        this.registerScreens();
        this.registerTERs();
        this.registerEntityRenderers();
        this.setRenderLayers();
    }
    
    private void registerKeybinds() {
        KeyBindings.init();
    }
    
    private void registerScreens() {
        // Register screen factories for each container
        ScreenManager.registerFactory(ContainersPM.GRIMOIRE.get(), GrimoireScreen::new);
        ScreenManager.registerFactory(ContainersPM.ARCANE_WORKBENCH.get(), ArcaneWorkbenchScreen::new);
        ScreenManager.registerFactory(ContainersPM.WAND_ASSEMBLY_TABLE.get(), WandAssemblyTableScreen::new);
        ScreenManager.registerFactory(ContainersPM.ANALYSIS_TABLE.get(), AnalysisTableScreen::new);
        ScreenManager.registerFactory(ContainersPM.CALCINATOR.get(), CalcinatorScreen::new);
        ScreenManager.registerFactory(ContainersPM.WAND_INSCRIPTION_TABLE.get(), WandInscriptionTableScreen::new);
        ScreenManager.registerFactory(ContainersPM.SPELLCRAFTING_ALTAR.get(), SpellcraftingAltarScreen::new);
        ScreenManager.registerFactory(ContainersPM.WAND_CHARGER.get(), WandChargerScreen::new);
    }
    
    private void registerTERs() {
        // Register tile entity renderers for those tile entities that need them
        ClientRegistry.bindTileEntityRenderer(TileEntityTypesPM.ANCIENT_MANA_FONT.get(), dispatcher -> new AncientManaFontTER(dispatcher));
        ClientRegistry.bindTileEntityRenderer(TileEntityTypesPM.WAND_CHARGER.get(), dispatcher -> new WandChargerTER(dispatcher));
    }
    
    private void registerEntityRenderers() {
        // Register renderers for each entity type
        RenderingRegistry.registerEntityRenderingHandler(EntityTypesPM.SPELL_PROJECTILE.get(), SpellProjectileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypesPM.SPELL_MINE.get(), SpellMineRenderer::new);
    }
    
    private void setRenderLayers() {
        // Set the render layers for any blocks that don't use the default
        RenderTypeLookup.setRenderLayer(BlocksPM.MOONWOOD_SAPLING, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlocksPM.MOONWOOD_LEAVES, RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.MOONWOOD_LOG, RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.MOONWOOD_PILLAR, RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.MOONWOOD_PLANKS, RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.MOONWOOD_SLAB, RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.MOONWOOD_STAIRS, RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.MOONWOOD_WOOD, RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STRIPPED_MOONWOOD_LOG, RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STRIPPED_MOONWOOD_WOOD, RenderType.translucent());

        RenderTypeLookup.setRenderLayer(BlocksPM.SUNWOOD_SAPLING, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlocksPM.SUNWOOD_LEAVES, RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.SUNWOOD_LOG, RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.SUNWOOD_PILLAR, RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.SUNWOOD_PLANKS, RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.SUNWOOD_SLAB, RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.SUNWOOD_STAIRS, RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.SUNWOOD_WOOD, RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STRIPPED_SUNWOOD_LOG, RenderType.translucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STRIPPED_SUNWOOD_WOOD, RenderType.translucent());
    }
    
    @Override
    public boolean isShiftDown() {
        return Screen.hasShiftDown();
    }
}
