package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.client.gui.AnalysisTableScreen;
import com.verdantartifice.primalmagick.client.gui.ArcaneWorkbenchScreen;
import com.verdantartifice.primalmagick.client.gui.CalcinatorScreen;
import com.verdantartifice.primalmagick.client.gui.ConcocterScreen;
import com.verdantartifice.primalmagick.client.gui.DissolutionChamberScreen;
import com.verdantartifice.primalmagick.client.gui.EssenceCaskScreen;
import com.verdantartifice.primalmagick.client.gui.EssenceTransmuterScreen;
import com.verdantartifice.primalmagick.client.gui.HoneyExtractorScreen;
import com.verdantartifice.primalmagick.client.gui.InfernalFurnaceScreen;
import com.verdantartifice.primalmagick.client.gui.ManaBatteryScreen;
import com.verdantartifice.primalmagick.client.gui.ResearchTableScreen;
import com.verdantartifice.primalmagick.client.gui.RunecarvingTableScreen;
import com.verdantartifice.primalmagick.client.gui.RunescribingAltarBasicScreen;
import com.verdantartifice.primalmagick.client.gui.RunescribingAltarEnchantedScreen;
import com.verdantartifice.primalmagick.client.gui.RunescribingAltarForbiddenScreen;
import com.verdantartifice.primalmagick.client.gui.RunescribingAltarHeavenlyScreen;
import com.verdantartifice.primalmagick.client.gui.RunicGrindstoneScreen;
import com.verdantartifice.primalmagick.client.gui.SpellcraftingAltarScreen;
import com.verdantartifice.primalmagick.client.gui.WandAssemblyTableScreen;
import com.verdantartifice.primalmagick.client.gui.WandChargerScreen;
import com.verdantartifice.primalmagick.client.gui.WandGlamourTableScreen;
import com.verdantartifice.primalmagick.client.gui.WandInscriptionTableScreen;
import com.verdantartifice.primalmagick.client.gui.hud.WandHudOverlay;
import com.verdantartifice.primalmagick.client.gui.hud.WardingHudOverlay;
import com.verdantartifice.primalmagick.client.gui.scribe_table.ScribeGainComprehensionScreen;
import com.verdantartifice.primalmagick.client.gui.scribe_table.ScribeStudyVocabularyScreen;
import com.verdantartifice.primalmagick.client.gui.scribe_table.ScribeTranscribeWorksScreen;
import com.verdantartifice.primalmagick.client.renderers.tile.AutoChargerTER;
import com.verdantartifice.primalmagick.client.renderers.tile.ManaFontTER;
import com.verdantartifice.primalmagick.client.renderers.tile.OfferingPedestalTER;
import com.verdantartifice.primalmagick.client.renderers.tile.RitualAltarTER;
import com.verdantartifice.primalmagick.client.renderers.tile.RitualBellTER;
import com.verdantartifice.primalmagick.client.renderers.tile.RitualLecternTER;
import com.verdantartifice.primalmagick.client.renderers.tile.RunescribingAltarTER;
import com.verdantartifice.primalmagick.client.renderers.tile.SanguineCrucibleTER;
import com.verdantartifice.primalmagick.client.renderers.tile.SpellcraftingAltarTER;
import com.verdantartifice.primalmagick.client.renderers.tile.WandChargerTER;
import com.verdantartifice.primalmagick.client.renderers.tile.WindGeneratorTER;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.entities.FlyingCarpetItem;
import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagick.common.menus.MenuTypesPM;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import com.verdantartifice.primalmagick.common.util.RayTraceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Respond to client-only Forge mod lifecycle events for setup.
 * 
 * @author Daedalus4096
 */
public class ClientModLifecycleEvents {
    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);

    public static void clientSetup(Consumer<Runnable> workConsumer) {
        if (INITIALIZED.compareAndSet(false, true)) {
            registerScreens();
            registerTERs();
            registerItemProperties(workConsumer);
            registerHudOverlays();
        }
    }

    private static void registerScreens() {
        // Register screen factories for each menu
        MenuScreens.register(MenuTypesPM.ARCANE_WORKBENCH.get(), ArcaneWorkbenchScreen::new);
        MenuScreens.register(MenuTypesPM.WAND_ASSEMBLY_TABLE.get(), WandAssemblyTableScreen::new);
        MenuScreens.register(MenuTypesPM.ANALYSIS_TABLE.get(), AnalysisTableScreen::new);
        MenuScreens.register(MenuTypesPM.CALCINATOR.get(), CalcinatorScreen::new);
        MenuScreens.register(MenuTypesPM.WAND_INSCRIPTION_TABLE.get(), WandInscriptionTableScreen::new);
        MenuScreens.register(MenuTypesPM.SPELLCRAFTING_ALTAR.get(), SpellcraftingAltarScreen::new);
        MenuScreens.register(MenuTypesPM.WAND_CHARGER.get(), WandChargerScreen::new);
        MenuScreens.register(MenuTypesPM.RESEARCH_TABLE.get(), ResearchTableScreen::new);
        MenuScreens.register(MenuTypesPM.RUNESCRIBING_ALTAR_BASIC.get(), RunescribingAltarBasicScreen::new);
        MenuScreens.register(MenuTypesPM.RUNESCRIBING_ALTAR_ENCHANTED.get(), RunescribingAltarEnchantedScreen::new);
        MenuScreens.register(MenuTypesPM.RUNESCRIBING_ALTAR_FORBIDDEN.get(), RunescribingAltarForbiddenScreen::new);
        MenuScreens.register(MenuTypesPM.RUNESCRIBING_ALTAR_HEAVENLY.get(), RunescribingAltarHeavenlyScreen::new);
        MenuScreens.register(MenuTypesPM.RUNECARVING_TABLE.get(), RunecarvingTableScreen::new);
        MenuScreens.register(MenuTypesPM.HONEY_EXTRACTOR.get(), HoneyExtractorScreen::new);
        MenuScreens.register(MenuTypesPM.CONCOCTER.get(), ConcocterScreen::new);
        MenuScreens.register(MenuTypesPM.ESSENCE_TRANSMUTER.get(), EssenceTransmuterScreen::new);
        MenuScreens.register(MenuTypesPM.DISSOLUTION_CHAMBER.get(), DissolutionChamberScreen::new);
        MenuScreens.register(MenuTypesPM.ESSENCE_CASK.get(), EssenceCaskScreen::new);
        MenuScreens.register(MenuTypesPM.WAND_GLAMOUR_TABLE.get(), WandGlamourTableScreen::new);
        MenuScreens.register(MenuTypesPM.RUNIC_GRINDSTONE.get(), RunicGrindstoneScreen::new);
        MenuScreens.register(MenuTypesPM.INFERNAL_FURNACE.get(), InfernalFurnaceScreen::new);
        MenuScreens.register(MenuTypesPM.MANA_BATTERY.get(), ManaBatteryScreen::new);
        MenuScreens.register(MenuTypesPM.SCRIBE_STUDY_VOCABULARY.get(), ScribeStudyVocabularyScreen::new);
        MenuScreens.register(MenuTypesPM.SCRIBE_GAIN_COMPREHENSION.get(), ScribeGainComprehensionScreen::new);
        MenuScreens.register(MenuTypesPM.SCRIBE_TRANSCRIBE_WORKS.get(), ScribeTranscribeWorksScreen::new);
    }
    
    private static void registerTERs() {
        // Register tile entity renderers for those tile entities that need them
        BlockEntityRenderers.register(BlockEntityTypesPM.ANCIENT_MANA_FONT.get(), ManaFontTER::new);
        BlockEntityRenderers.register(BlockEntityTypesPM.ARTIFICIAL_MANA_FONT.get(), ManaFontTER::new);
        BlockEntityRenderers.register(BlockEntityTypesPM.WAND_CHARGER.get(), WandChargerTER::new);
        BlockEntityRenderers.register(BlockEntityTypesPM.RITUAL_ALTAR.get(), RitualAltarTER::new);
        BlockEntityRenderers.register(BlockEntityTypesPM.OFFERING_PEDESTAL.get(), OfferingPedestalTER::new);
        BlockEntityRenderers.register(BlockEntityTypesPM.RITUAL_LECTERN.get(), RitualLecternTER::new);
        BlockEntityRenderers.register(BlockEntityTypesPM.RITUAL_BELL.get(), RitualBellTER::new);
        BlockEntityRenderers.register(BlockEntityTypesPM.RUNESCRIBING_ALTAR.get(), RunescribingAltarTER::new);
        BlockEntityRenderers.register(BlockEntityTypesPM.SANGUINE_CRUCIBLE.get(), SanguineCrucibleTER::new);
        BlockEntityRenderers.register(BlockEntityTypesPM.AUTO_CHARGER.get(), AutoChargerTER::new);
        BlockEntityRenderers.register(BlockEntityTypesPM.SPELLCRAFTING_ALTAR.get(), SpellcraftingAltarTER::new);
        BlockEntityRenderers.register(BlockEntityTypesPM.WIND_GENERATOR.get(), WindGeneratorTER::new);
    }
    
    private static void registerItemProperties(Consumer<Runnable> workConsumer) {
        // Register properties for items on the main thread in a thread-safe fashion
        workConsumer.accept(() -> {
            ItemProperties.register(ItemsPM.ARCANOMETER.get(), ArcanometerItem.SCAN_STATE_PROPERTY, new ClampedItemPropertyFunction() {
                protected float scanState = 0;

                @Override
                public float unclampedCall(ItemStack stack, ClientLevel world, LivingEntity entity, int seed) {
                    if (entity instanceof Player player) {
                        // If the currently moused-over block/item has not yet been scanned, raise the antennae
                        if (ArcanometerItem.isMouseOverScannable(RayTraceUtils.getMouseOver(world, player), world, (Player)entity)) {
                            this.incrementScanState();
                        } else {
                            this.decrementScanState();
                        }
                        return scanState;
                    } else {
                        return 0F;
                    }
                }
                
                protected void incrementScanState() {
                    this.scanState = Math.min(4.0F, this.scanState + 0.25F);
                }
                
                protected void decrementScanState() {
                    this.scanState = Math.max(0.0F, this.scanState - 0.25F);
                }
            });
            
            ItemProperties.register(ItemsPM.FLYING_CARPET.get(), FlyingCarpetItem.COLOR_PROPERTY, (ItemStack stack, ClientLevel world, LivingEntity entity, int unknown) -> {
                DyeColor color = null;
                if (stack != null && stack.getItem() instanceof FlyingCarpetItem) {
                    color = ((FlyingCarpetItem)stack.getItem()).getDyeColor(stack);
                }
                if (color == null) {
                    // Default to white if no dye color is applied
                    color = DyeColor.WHITE;
                }
                return ((float)color.getId() / 16.0F);
            });

            ClampedItemPropertyFunction castProperty = (ItemStack stack, ClientLevel world, LivingEntity entity, int seed) -> {
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
            ItemProperties.register(ItemsPM.PRIMALITE_FISHING_ROD.get(), ResourceLocation.withDefaultNamespace("cast"), castProperty);
            ItemProperties.register(ItemsPM.HEXIUM_FISHING_ROD.get(), ResourceLocation.withDefaultNamespace("cast"), castProperty);
            ItemProperties.register(ItemsPM.HALLOWSTEEL_FISHING_ROD.get(), ResourceLocation.withDefaultNamespace("cast"), castProperty);
            ItemProperties.register(ItemsPM.PRIMAL_FISHING_ROD.get(), ResourceLocation.withDefaultNamespace("cast"), castProperty);

            ClampedItemPropertyFunction handActiveProperty = (ItemStack stack, ClientLevel world, LivingEntity entity, int seed) -> {
                return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
            };
            ItemProperties.register(ItemsPM.PRIMALITE_TRIDENT.get(), ResourceLocation.withDefaultNamespace("throwing"), handActiveProperty);
            ItemProperties.register(ItemsPM.HEXIUM_TRIDENT.get(), ResourceLocation.withDefaultNamespace("throwing"), handActiveProperty);
            ItemProperties.register(ItemsPM.HALLOWSTEEL_TRIDENT.get(), ResourceLocation.withDefaultNamespace("throwing"), handActiveProperty);
            ItemProperties.register(ItemsPM.FORBIDDEN_TRIDENT.get(), ResourceLocation.withDefaultNamespace("throwing"), handActiveProperty);
            ItemProperties.register(ItemsPM.PRIMALITE_SHIELD.get(), ResourceLocation.withDefaultNamespace("blocking"), handActiveProperty);
            ItemProperties.register(ItemsPM.HEXIUM_SHIELD.get(), ResourceLocation.withDefaultNamespace("blocking"), handActiveProperty);
            ItemProperties.register(ItemsPM.HALLOWSTEEL_SHIELD.get(), ResourceLocation.withDefaultNamespace("blocking"), handActiveProperty);
            ItemProperties.register(ItemsPM.SACRED_SHIELD.get(), ResourceLocation.withDefaultNamespace("blocking"), handActiveProperty);

            ClampedItemPropertyFunction pullProperty = (ItemStack stack, ClientLevel world, LivingEntity entity, int seed) -> {
                if (entity == null) {
                    return 0.0F;
                } else {
                    return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
                }
            };
            ItemProperties.register(ItemsPM.PRIMALITE_BOW.get(), ResourceLocation.withDefaultNamespace("pull"), pullProperty);
            ItemProperties.register(ItemsPM.PRIMALITE_BOW.get(), ResourceLocation.withDefaultNamespace("pulling"), handActiveProperty);
            ItemProperties.register(ItemsPM.HEXIUM_BOW.get(), ResourceLocation.withDefaultNamespace("pull"), pullProperty);
            ItemProperties.register(ItemsPM.HEXIUM_BOW.get(), ResourceLocation.withDefaultNamespace("pulling"), handActiveProperty);
            ItemProperties.register(ItemsPM.HALLOWSTEEL_BOW.get(), ResourceLocation.withDefaultNamespace("pull"), pullProperty);
            ItemProperties.register(ItemsPM.HALLOWSTEEL_BOW.get(), ResourceLocation.withDefaultNamespace("pulling"), handActiveProperty);
            ItemProperties.register(ItemsPM.FORBIDDEN_BOW.get(), ResourceLocation.withDefaultNamespace("pull"), pullProperty);
            ItemProperties.register(ItemsPM.FORBIDDEN_BOW.get(), ResourceLocation.withDefaultNamespace("pulling"), handActiveProperty);
        });
    }
    
    private static void registerHudOverlays() {
        Minecraft mc = Minecraft.getInstance();
        
        // Register the wand HUD overlay
        // FIXME Register above hotbar layer instead of at the top
        LayeredDraw wandLayer = new LayeredDraw();
        wandLayer.add(WandHudOverlay::render);
        mc.gui.layers.add(wandLayer, WandHudOverlay::shouldRender);
        
        // Register the ward health bar overlay
        // FIXME Register above player health layer instead of at the top
        LayeredDraw wardLayer = new LayeredDraw();
        wardLayer.add(WardingHudOverlay::render);
        mc.gui.layers.add(wardLayer, WardingHudOverlay::shouldRender);
    }
}
