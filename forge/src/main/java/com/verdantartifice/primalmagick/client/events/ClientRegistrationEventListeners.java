package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.client.fx.particles.AirCurrentParticle;
import com.verdantartifice.primalmagick.client.fx.particles.InfernalFlameParticle;
import com.verdantartifice.primalmagick.client.fx.particles.ManaSparkleParticle;
import com.verdantartifice.primalmagick.client.fx.particles.OfferingParticle;
import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagick.client.fx.particles.PropMarkerParticle;
import com.verdantartifice.primalmagick.client.fx.particles.SpellBoltParticle;
import com.verdantartifice.primalmagick.client.fx.particles.SpellSparkleParticle;
import com.verdantartifice.primalmagick.client.fx.particles.SpellcraftingRuneParticle;
import com.verdantartifice.primalmagick.client.fx.particles.WandPoofParticle;
import com.verdantartifice.primalmagick.client.gui.hud.ManaStorageItemDecoratorForge;
import com.verdantartifice.primalmagick.common.items.armor.WardingModuleItem;
import com.verdantartifice.primalmagick.common.sources.Sources;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.IItemDecorator;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Forge listeners for client-only Forge registration events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistrationEventListeners {
    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        ClientRegistrationEvents.onRegisterParticleProviders(event::registerSprite, event::registerSpecial);

        // FIXME The common access transformer refuses to recognize ParticleEngine$SpriteParticleRegistration, so do it here instead
        event.registerSpriteSet(ParticleTypesPM.WAND_POOF.get(), WandPoofParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.MANA_SPARKLE.get(), ManaSparkleParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.SPELL_SPARKLE.get(), SpellSparkleParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.SPELL_BOLT.get(), SpellBoltParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.OFFERING.get(), OfferingParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.PROP_MARKER.get(), PropMarkerParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.SPELLCRAFTING_RUNE_U.get(), SpellcraftingRuneParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.SPELLCRAFTING_RUNE_V.get(), SpellcraftingRuneParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.SPELLCRAFTING_RUNE_T.get(), SpellcraftingRuneParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.SPELLCRAFTING_RUNE_D.get(), SpellcraftingRuneParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.INFERNAL_FLAME.get(), InfernalFlameParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.AIR_CURRENT.get(), AirCurrentParticle.Factory::new);
        event.registerSpriteSet(ParticleTypesPM.VOID_SMOKE.get(), AirCurrentParticle.Factory::new);
    }
    
    @SubscribeEvent
    public static void onModelRegister(ModelEvent.RegisterAdditional event) {
        ClientRegistrationEvents.onModelRegister(event::register);
    }
    
    @SubscribeEvent
    public static void onClientReloadListenerRegister(RegisterClientReloadListenersEvent event) {
        ClientRegistrationEvents.onClientReloadListenerRegister(event::registerReloadListener);
    }
    
    @SubscribeEvent
    public static void onRegisterClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
        ClientRegistrationEvents.onRegisterClientTooltipComponentFactories(event::register);
    }
    
    @SubscribeEvent
    public static void onRegisterItemDecorations(RegisterItemDecorationsEvent event) {
        // FIXME Use the WARDABLE_ARMOR tag as the source of truth if/when the RegisterItemDecorationsEvent is made to fire *after* tag data loads
        IItemDecorator wardDecorator = new ManaStorageItemDecoratorForge(Sources.EARTH);
        WardingModuleItem.getApplicableItems().forEach(itemSupplier -> event.register(itemSupplier.get(), wardDecorator));
    }
}
