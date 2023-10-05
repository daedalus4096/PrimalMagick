package com.verdantartifice.primalmagick.client.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.books.LexiconLoader;
import com.verdantartifice.primalmagick.client.books.StyleGuideLoader;
import com.verdantartifice.primalmagick.client.fx.particles.AirCurrentParticle;
import com.verdantartifice.primalmagick.client.fx.particles.DripParticlePM;
import com.verdantartifice.primalmagick.client.fx.particles.InfernalFlameParticle;
import com.verdantartifice.primalmagick.client.fx.particles.ManaSparkleParticle;
import com.verdantartifice.primalmagick.client.fx.particles.NoteEmitterParticle;
import com.verdantartifice.primalmagick.client.fx.particles.OfferingParticle;
import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagick.client.fx.particles.PotionExplosionParticle;
import com.verdantartifice.primalmagick.client.fx.particles.PropMarkerParticle;
import com.verdantartifice.primalmagick.client.fx.particles.SpellBoltParticle;
import com.verdantartifice.primalmagick.client.fx.particles.SpellSparkleParticle;
import com.verdantartifice.primalmagick.client.fx.particles.SpellcraftingRuneParticle;
import com.verdantartifice.primalmagick.client.fx.particles.WandPoofParticle;
import com.verdantartifice.primalmagick.client.gui.hud.ManaStorageItemDecorator;
import com.verdantartifice.primalmagick.client.gui.hud.WandHudOverlay;
import com.verdantartifice.primalmagick.client.gui.hud.WardingHudOverlay;
import com.verdantartifice.primalmagick.client.tips.TipLoader;
import com.verdantartifice.primalmagick.client.tooltips.ClientAffinityTooltipComponent;
import com.verdantartifice.primalmagick.common.affinities.AffinityTooltipComponent;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.armor.WardingModuleItem;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.IItemDecorator;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Respond to client-only Forge registration events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistrationEvents {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleTypesPM.WAND_POOF.get(), WandPoofParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.MANA_SPARKLE.get(), ManaSparkleParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.SPELL_SPARKLE.get(), SpellSparkleParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.SPELL_BOLT.get(), SpellBoltParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.OFFERING.get(), OfferingParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.PROP_MARKER.get(), PropMarkerParticle.Factory::new);
        event.registerSpecial(ParticleTypesPM.POTION_EXPLOSION.get(), new PotionExplosionParticle.Factory());
        event.registerSpecial(ParticleTypesPM.NOTE_EMITTER.get(), new NoteEmitterParticle.Factory());
        event.registerSpriteSet(ParticleTypesPM.SPELLCRAFTING_RUNE_U.get(), SpellcraftingRuneParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.SPELLCRAFTING_RUNE_V.get(), SpellcraftingRuneParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.SPELLCRAFTING_RUNE_T.get(), SpellcraftingRuneParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.SPELLCRAFTING_RUNE_D.get(), SpellcraftingRuneParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.INFERNAL_FLAME.get(), InfernalFlameParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.AIR_CURRENT.get(), AirCurrentParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.VOID_SMOKE.get(), AirCurrentParticle.Factory::new);
        event.registerSprite(ParticleTypesPM.DRIPPING_BLOOD_DROP.get(), DripParticlePM::createBloodDropHangParticle);
        event.registerSprite(ParticleTypesPM.FALLING_BLOOD_DROP.get(), DripParticlePM::createBloodDropFallParticle);
        event.registerSprite(ParticleTypesPM.LANDING_BLOOD_DROP.get(), DripParticlePM::createBloodDropLandParticle);
    }
    
    /**
     * Register special model resource locations that must be loaded even if not tied to a block state.
     * 
     * @param event
     */
    @SubscribeEvent
    public static void onModelRegister(ModelEvent.RegisterAdditional event) {
        event.register(new ModelResourceLocation(PrimalMagick.resource("mundane_wand_core"), ""));
        for (WandCore core : WandCore.getAllWandCores()) {
            event.register(new ModelResourceLocation(core.getWandModelResourceLocationNamespace(), ""));
            event.register(new ModelResourceLocation(core.getStaffModelResourceLocationNamespace(), ""));
        }
        for (WandCap cap : WandCap.getAllWandCaps()) {
            event.register(new ModelResourceLocation(cap.getWandModelResourceLocationNamespace(), ""));
            event.register(new ModelResourceLocation(cap.getStaffModelResourceLocationNamespace(), ""));
        }
        for (WandGem gem : WandGem.getAllWandGems()) {
            event.register(new ModelResourceLocation(gem.getModelResourceLocationNamespace(), ""));
        }
        for (int index = 0; index <= 4; index++) {
            event.register(new ModelResourceLocation(PrimalMagick.resource("arcanometer_" + index), ""));
        }
    }
    
    @SubscribeEvent
    public static void onClientReloadListenerRegister(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(ItemsPM.PRIMALITE_TRIDENT.get().getRenderProperties().getCustomRenderer());
        event.registerReloadListener(ItemsPM.HEXIUM_TRIDENT.get().getRenderProperties().getCustomRenderer());
        event.registerReloadListener(ItemsPM.HALLOWSTEEL_TRIDENT.get().getRenderProperties().getCustomRenderer());
        event.registerReloadListener(ItemsPM.FORBIDDEN_TRIDENT.get().getRenderProperties().getCustomRenderer());
        event.registerReloadListener(ItemsPM.PRIMALITE_SHIELD.get().getRenderProperties().getCustomRenderer());
        event.registerReloadListener(ItemsPM.HEXIUM_SHIELD.get().getRenderProperties().getCustomRenderer());
        event.registerReloadListener(ItemsPM.HALLOWSTEEL_SHIELD.get().getRenderProperties().getCustomRenderer());
        event.registerReloadListener(ItemsPM.SPELLCRAFTING_ALTAR.get().getRenderProperties().getCustomRenderer());
        event.registerReloadListener(LexiconLoader.getOrCreateInstance());
        event.registerReloadListener(StyleGuideLoader.getOrCreateInstance());
        event.registerReloadListener(TipLoader.getOrCreateInstance());
    }
    
    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "wand_hud", new WandHudOverlay());
        event.registerAbove(VanillaGuiOverlay.PLAYER_HEALTH.id(), "warding_hud", new WardingHudOverlay());
    }
    
    @SubscribeEvent
    public static void onRegisterClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(AffinityTooltipComponent.class, ClientAffinityTooltipComponent::new);
    }
    
    @SubscribeEvent
    public static void onRegisterItemDecorations(RegisterItemDecorationsEvent event) {
        // FIXME Use the WARDABLE_ARMOR tag as the source of truth if/when the RegisterItemDecorationsEvent is made to fire *after* tag data loads
        IItemDecorator wardDecorator = new ManaStorageItemDecorator(Source.EARTH);
        WardingModuleItem.getApplicableItems().forEach(itemSupplier -> event.register(itemSupplier.get(), wardDecorator));
    }
}
