package com.verdantartifice.primalmagic.proxy;

import com.verdantartifice.primalmagic.client.config.KeyBindings;
import com.verdantartifice.primalmagic.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagic.client.gui.AnalysisTableScreen;
import com.verdantartifice.primalmagic.client.gui.ArcaneWorkbenchScreen;
import com.verdantartifice.primalmagic.client.gui.CalcinatorScreen;
import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.ResearchTableScreen;
import com.verdantartifice.primalmagic.client.gui.RunecarvingTableScreen;
import com.verdantartifice.primalmagic.client.gui.RunescribingAltarBasicScreen;
import com.verdantartifice.primalmagic.client.gui.RunescribingAltarEnchantedScreen;
import com.verdantartifice.primalmagic.client.gui.RunescribingAltarForbiddenScreen;
import com.verdantartifice.primalmagic.client.gui.RunescribingAltarHeavenlyScreen;
import com.verdantartifice.primalmagic.client.gui.SpellcraftingAltarScreen;
import com.verdantartifice.primalmagic.client.gui.WandAssemblyTableScreen;
import com.verdantartifice.primalmagic.client.gui.WandChargerScreen;
import com.verdantartifice.primalmagic.client.gui.WandInscriptionTableScreen;
import com.verdantartifice.primalmagic.client.renderers.entity.SpellMineRenderer;
import com.verdantartifice.primalmagic.client.renderers.entity.SpellProjectileRenderer;
import com.verdantartifice.primalmagic.client.renderers.tile.AncientManaFontTER;
import com.verdantartifice.primalmagic.client.renderers.tile.OfferingPedestalTER;
import com.verdantartifice.primalmagic.client.renderers.tile.RitualAltarTER;
import com.verdantartifice.primalmagic.client.renderers.tile.RitualBellTER;
import com.verdantartifice.primalmagic.client.renderers.tile.RitualLecternTER;
import com.verdantartifice.primalmagic.client.renderers.tile.RunescribingAltarTER;
import com.verdantartifice.primalmagic.client.renderers.tile.WandChargerTER;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.containers.ContainersPM;
import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemModelsProperties;
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
        this.registerItemProperties(event);
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
        ScreenManager.registerFactory(ContainersPM.RESEARCH_TABLE.get(), ResearchTableScreen::new);
        ScreenManager.registerFactory(ContainersPM.RUNESCRIBING_ALTAR_BASIC.get(), RunescribingAltarBasicScreen::new);
        ScreenManager.registerFactory(ContainersPM.RUNESCRIBING_ALTAR_ENCHANTED.get(), RunescribingAltarEnchantedScreen::new);
        ScreenManager.registerFactory(ContainersPM.RUNESCRIBING_ALTAR_FORBIDDEN.get(), RunescribingAltarForbiddenScreen::new);
        ScreenManager.registerFactory(ContainersPM.RUNESCRIBING_ALTAR_HEAVENLY.get(), RunescribingAltarHeavenlyScreen::new);
        ScreenManager.registerFactory(ContainersPM.RUNECARVING_TABLE.get(), RunecarvingTableScreen::new);
    }
    
    private void registerTERs() {
        // Register tile entity renderers for those tile entities that need them
        ClientRegistry.bindTileEntityRenderer(TileEntityTypesPM.ANCIENT_MANA_FONT.get(), dispatcher -> new AncientManaFontTER(dispatcher));
        ClientRegistry.bindTileEntityRenderer(TileEntityTypesPM.WAND_CHARGER.get(), dispatcher -> new WandChargerTER(dispatcher));
        ClientRegistry.bindTileEntityRenderer(TileEntityTypesPM.RITUAL_ALTAR.get(), dispatcher -> new RitualAltarTER(dispatcher));
        ClientRegistry.bindTileEntityRenderer(TileEntityTypesPM.OFFERING_PEDESTAL.get(), dispatcher -> new OfferingPedestalTER(dispatcher));
        ClientRegistry.bindTileEntityRenderer(TileEntityTypesPM.RITUAL_LECTERN.get(), dispatcher -> new RitualLecternTER(dispatcher));
        ClientRegistry.bindTileEntityRenderer(TileEntityTypesPM.RITUAL_BELL.get(), dispatcher -> new RitualBellTER(dispatcher));
        ClientRegistry.bindTileEntityRenderer(TileEntityTypesPM.RUNESCRIBING_ALTAR.get(), dispatcher -> new RunescribingAltarTER(dispatcher));
    }
    
    private void registerEntityRenderers() {
        // Register renderers for each entity type
        RenderingRegistry.registerEntityRenderingHandler(EntityTypesPM.SPELL_PROJECTILE.get(), SpellProjectileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypesPM.SPELL_MINE.get(), SpellMineRenderer::new);
    }
    
    private void registerItemProperties(FMLClientSetupEvent event) {
    	// Register properties for items on the main thread in a thread-safe fashion
    	event.enqueueWork(() -> {
    		ItemModelsProperties.registerProperty(ItemsPM.ARCANOMETER.get(), ArcanometerItem.SCAN_STATE_PROPERTY, ArcanometerItem.getScanStateProperty());
    	});
    }
    
    private void setRenderLayers() {
        // Set the render layers for any blocks that don't use the default
        RenderTypeLookup.setRenderLayer(BlocksPM.MOONWOOD_SAPLING.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlocksPM.MOONWOOD_LEAVES.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.MOONWOOD_LOG.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.MOONWOOD_PILLAR.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.MOONWOOD_PLANKS.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.MOONWOOD_SLAB.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.MOONWOOD_STAIRS.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.MOONWOOD_WOOD.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STRIPPED_MOONWOOD_LOG.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STRIPPED_MOONWOOD_WOOD.get(), RenderType.getTranslucent());

        RenderTypeLookup.setRenderLayer(BlocksPM.SUNWOOD_SAPLING.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlocksPM.SUNWOOD_LEAVES.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.SUNWOOD_LOG.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.SUNWOOD_PILLAR.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.SUNWOOD_PLANKS.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.SUNWOOD_SLAB.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.SUNWOOD_STAIRS.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.SUNWOOD_WOOD.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STRIPPED_SUNWOOD_LOG.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STRIPPED_SUNWOOD_WOOD.get(), RenderType.getTranslucent());
        
        RenderTypeLookup.setRenderLayer(BlocksPM.SALT_TRAIL.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlocksPM.SUNLAMP.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlocksPM.BLOODLETTER.get(), RenderType.getCutout());

        RenderTypeLookup.setRenderLayer(BlocksPM.SKYGLASS.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_BLACK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_BLUE.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_BROWN.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_CYAN.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_GRAY.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_GREEN.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_LIGHT_BLUE.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_LIGHT_GRAY.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_LIME.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_MAGENTA.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_ORANGE.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PINK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PURPLE.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_RED.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_WHITE.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_YELLOW.get(), RenderType.getTranslucent());
        
        RenderTypeLookup.setRenderLayer(BlocksPM.SKYGLASS_PANE.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_BLACK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_BLUE.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_BROWN.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_CYAN.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_GRAY.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_GREEN.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_LIME.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_MAGENTA.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_ORANGE.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_PINK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_PURPLE.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_RED.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_WHITE.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_YELLOW.get(), RenderType.getTranslucent());
    }
    
    @Override
    public boolean isShiftDown() {
        return Screen.hasShiftDown();
    }
}
