package com.verdantartifice.primalmagic.proxy;

import com.verdantartifice.primalmagic.client.config.KeyBindings;
import com.verdantartifice.primalmagic.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagic.client.gui.AnalysisTableScreen;
import com.verdantartifice.primalmagic.client.gui.ArcaneWorkbenchScreen;
import com.verdantartifice.primalmagic.client.gui.CalcinatorScreen;
import com.verdantartifice.primalmagic.client.gui.ConcocterScreen;
import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.HoneyExtractorScreen;
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
import com.verdantartifice.primalmagic.client.renderers.tile.AncientManaFontTER;
import com.verdantartifice.primalmagic.client.renderers.tile.OfferingPedestalTER;
import com.verdantartifice.primalmagic.client.renderers.tile.RitualAltarTER;
import com.verdantartifice.primalmagic.client.renderers.tile.RitualBellTER;
import com.verdantartifice.primalmagic.client.renderers.tile.RitualLecternTER;
import com.verdantartifice.primalmagic.client.renderers.tile.RunescribingAltarTER;
import com.verdantartifice.primalmagic.client.renderers.tile.SanguineCrucibleTER;
import com.verdantartifice.primalmagic.client.renderers.tile.WandChargerTER;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.containers.ContainersPM;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.entities.FlyingCarpetItem;
import com.verdantartifice.primalmagic.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client sided proxy.  Handles client setup issues and provides side-dependent utility methods.
 * 
 * @author Daedalus4096
 */
@SuppressWarnings("deprecation")
public class ClientProxy implements IProxyPM {
    @Override
    public void initDeferredRegistries() {
        ParticleTypesPM.init();
    }
    
    @Override
    public void clientSetup(FMLClientSetupEvent event) {
        this.registerKeybinds();
        this.registerScreens();
        this.registerTERs();
        this.registerItemProperties(event);
        this.setRenderLayers();
    }
    
    private void registerKeybinds() {
        KeyBindings.init();
    }
    
    private void registerScreens() {
        // Register screen factories for each container
        MenuScreens.register(ContainersPM.GRIMOIRE.get(), GrimoireScreen::new);
        MenuScreens.register(ContainersPM.ARCANE_WORKBENCH.get(), ArcaneWorkbenchScreen::new);
        MenuScreens.register(ContainersPM.WAND_ASSEMBLY_TABLE.get(), WandAssemblyTableScreen::new);
        MenuScreens.register(ContainersPM.ANALYSIS_TABLE.get(), AnalysisTableScreen::new);
        MenuScreens.register(ContainersPM.CALCINATOR.get(), CalcinatorScreen::new);
        MenuScreens.register(ContainersPM.WAND_INSCRIPTION_TABLE.get(), WandInscriptionTableScreen::new);
        MenuScreens.register(ContainersPM.SPELLCRAFTING_ALTAR.get(), SpellcraftingAltarScreen::new);
        MenuScreens.register(ContainersPM.WAND_CHARGER.get(), WandChargerScreen::new);
        MenuScreens.register(ContainersPM.RESEARCH_TABLE.get(), ResearchTableScreen::new);
        MenuScreens.register(ContainersPM.RUNESCRIBING_ALTAR_BASIC.get(), RunescribingAltarBasicScreen::new);
        MenuScreens.register(ContainersPM.RUNESCRIBING_ALTAR_ENCHANTED.get(), RunescribingAltarEnchantedScreen::new);
        MenuScreens.register(ContainersPM.RUNESCRIBING_ALTAR_FORBIDDEN.get(), RunescribingAltarForbiddenScreen::new);
        MenuScreens.register(ContainersPM.RUNESCRIBING_ALTAR_HEAVENLY.get(), RunescribingAltarHeavenlyScreen::new);
        MenuScreens.register(ContainersPM.RUNECARVING_TABLE.get(), RunecarvingTableScreen::new);
        MenuScreens.register(ContainersPM.HONEY_EXTRACTOR.get(), HoneyExtractorScreen::new);
        MenuScreens.register(ContainersPM.CONCOCTER.get(), ConcocterScreen::new);
    }
    
    private void registerTERs() {
        // Register tile entity renderers for those tile entities that need them
        BlockEntityRenderers.register(TileEntityTypesPM.ANCIENT_MANA_FONT.get(), AncientManaFontTER::new);
        BlockEntityRenderers.register(TileEntityTypesPM.WAND_CHARGER.get(), WandChargerTER::new);
        BlockEntityRenderers.register(TileEntityTypesPM.RITUAL_ALTAR.get(), RitualAltarTER::new);
        BlockEntityRenderers.register(TileEntityTypesPM.OFFERING_PEDESTAL.get(), OfferingPedestalTER::new);
        BlockEntityRenderers.register(TileEntityTypesPM.RITUAL_LECTERN.get(), RitualLecternTER::new);
        BlockEntityRenderers.register(TileEntityTypesPM.RITUAL_BELL.get(), RitualBellTER::new);
        BlockEntityRenderers.register(TileEntityTypesPM.RUNESCRIBING_ALTAR.get(), RunescribingAltarTER::new);
        BlockEntityRenderers.register(TileEntityTypesPM.SANGUINE_CRUCIBLE.get(), SanguineCrucibleTER::new);
    }
    
    private void registerItemProperties(FMLClientSetupEvent event) {
        // Register properties for items on the main thread in a thread-safe fashion
        event.enqueueWork(() -> {
            ItemProperties.register(ItemsPM.ARCANOMETER.get(), ArcanometerItem.SCAN_STATE_PROPERTY, ArcanometerItem.getScanStateProperty());
            ItemProperties.register(ItemsPM.FLYING_CARPET.get(), FlyingCarpetItem.COLOR_PROPERTY, FlyingCarpetItem.getColorProperty());
            
            ItemPropertyFunction castProperty = (ItemStack stack, ClientLevel world, LivingEntity entity, int seed) -> {
                if (entity == null) {
                    return 0.0F;
                } else {
                    boolean inMain = entity.getMainHandItem() == stack;
                    boolean inOff = entity.getOffhandItem() == stack;
                    if (entity.getMainHandItem().getItem() instanceof FishingRodItem) {
                        inOff = false;
                    }
                    return (inMain || inOff) && entity instanceof Player && ((Player)entity).fishing != null ? 1.0F : 0.0F;
                }
            };
            ItemProperties.register(ItemsPM.PRIMALITE_FISHING_ROD.get(), new ResourceLocation("cast"), castProperty);
            ItemProperties.register(ItemsPM.HEXIUM_FISHING_ROD.get(), new ResourceLocation("cast"), castProperty);
            ItemProperties.register(ItemsPM.HALLOWSTEEL_FISHING_ROD.get(), new ResourceLocation("cast"), castProperty);
            ItemProperties.register(ItemsPM.PRIMAL_FISHING_ROD.get(), new ResourceLocation("cast"), castProperty);
            
            ItemPropertyFunction handActiveProperty = (ItemStack stack, ClientLevel world, LivingEntity entity, int seed) -> {
                return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
            };
            ItemProperties.register(ItemsPM.PRIMALITE_TRIDENT.get(), new ResourceLocation("throwing"), handActiveProperty);
            ItemProperties.register(ItemsPM.HEXIUM_TRIDENT.get(), new ResourceLocation("throwing"), handActiveProperty);
            ItemProperties.register(ItemsPM.HALLOWSTEEL_TRIDENT.get(), new ResourceLocation("throwing"), handActiveProperty);
            ItemProperties.register(ItemsPM.FORBIDDEN_TRIDENT.get(), new ResourceLocation("throwing"), handActiveProperty);
            ItemProperties.register(ItemsPM.PRIMALITE_SHIELD.get(), new ResourceLocation("blocking"), handActiveProperty);
            ItemProperties.register(ItemsPM.HEXIUM_SHIELD.get(), new ResourceLocation("blocking"), handActiveProperty);
            ItemProperties.register(ItemsPM.HALLOWSTEEL_SHIELD.get(), new ResourceLocation("blocking"), handActiveProperty);
            
            ItemPropertyFunction pullProperty = (ItemStack stack, ClientLevel world, LivingEntity entity, int seed) -> {
                if (entity == null) {
                    return 0.0F;
                } else {
                    return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
                }
            };
            ItemProperties.register(ItemsPM.PRIMALITE_BOW.get(), new ResourceLocation("pull"), pullProperty);
            ItemProperties.register(ItemsPM.PRIMALITE_BOW.get(), new ResourceLocation("pulling"), handActiveProperty);
            ItemProperties.register(ItemsPM.HEXIUM_BOW.get(), new ResourceLocation("pull"), pullProperty);
            ItemProperties.register(ItemsPM.HEXIUM_BOW.get(), new ResourceLocation("pulling"), handActiveProperty);
            ItemProperties.register(ItemsPM.HALLOWSTEEL_BOW.get(), new ResourceLocation("pull"), pullProperty);
            ItemProperties.register(ItemsPM.HALLOWSTEEL_BOW.get(), new ResourceLocation("pulling"), handActiveProperty);
            ItemProperties.register(ItemsPM.FORBIDDEN_BOW.get(), new ResourceLocation("pull"), pullProperty);
            ItemProperties.register(ItemsPM.FORBIDDEN_BOW.get(), new ResourceLocation("pulling"), handActiveProperty);
        });
    }
    
    private void setRenderLayers() {
        // Set the render layers for any blocks that don't use the default
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.MOONWOOD_SAPLING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.MOONWOOD_LEAVES.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.MOONWOOD_LOG.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.MOONWOOD_PILLAR.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.MOONWOOD_PLANKS.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.MOONWOOD_SLAB.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.MOONWOOD_STAIRS.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.MOONWOOD_WOOD.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STRIPPED_MOONWOOD_LOG.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STRIPPED_MOONWOOD_WOOD.get(), RenderType.translucent());

        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SUNWOOD_SAPLING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SUNWOOD_LEAVES.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SUNWOOD_LOG.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SUNWOOD_PILLAR.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SUNWOOD_PLANKS.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SUNWOOD_SLAB.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SUNWOOD_STAIRS.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SUNWOOD_WOOD.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STRIPPED_SUNWOOD_LOG.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STRIPPED_SUNWOOD_WOOD.get(), RenderType.translucent());
        
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.HALLOWOOD_SAPLING.get(), RenderType.cutout());
        
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SALT_TRAIL.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SUNLAMP.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SPIRIT_LANTERN.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.BLOODLETTER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.CONCOCTER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.CELESTIAL_HARP.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.HONEY_EXTRACTOR.get(), RenderType.translucent());

        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SKYGLASS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_BLACK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_BLUE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_BROWN.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_CYAN.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_GRAY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_GREEN.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_LIGHT_BLUE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_LIGHT_GRAY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_LIME.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_MAGENTA.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_ORANGE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PINK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PURPLE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_RED.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_WHITE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_YELLOW.get(), RenderType.translucent());
        
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.SKYGLASS_PANE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_BLACK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_BLUE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_BROWN.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_CYAN.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_GRAY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_GREEN.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_LIME.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_MAGENTA.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_ORANGE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_PINK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_PURPLE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_RED.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_WHITE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlocksPM.STAINED_SKYGLASS_PANE_YELLOW.get(), RenderType.translucent());
    }
    
    @Override
    public boolean isShiftDown() {
        return Screen.hasShiftDown();
    }
}
