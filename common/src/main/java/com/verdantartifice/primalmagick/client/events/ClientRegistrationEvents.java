package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.Constants;
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
import com.verdantartifice.primalmagick.client.tips.TipLoader;
import com.verdantartifice.primalmagick.client.tooltips.ClientAffinityTooltipComponent;
import com.verdantartifice.primalmagick.common.affinities.AffinityTooltipComponent;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.armor.WardingModuleItem;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.IItemDecorator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

/**
 * Respond to client-only Forge registration events.
 * 
 * @author Daedalus4096
 */
public class ClientRegistrationEvents {
    public static void onRegisterParticleProviders(SpriteParticleProviderRegistrar sprite,
            SpriteSetParticleProviderRegistrar spriteSet, SpecialParticleProviderRegistrar special) {
        spriteSet.register(ParticleTypesPM.WAND_POOF.get(), WandPoofParticle.Factory::new);
        spriteSet.register(ParticleTypesPM.MANA_SPARKLE.get(), ManaSparkleParticle.Factory::new);
        spriteSet.register(ParticleTypesPM.SPELL_SPARKLE.get(), SpellSparkleParticle.Factory::new);
        spriteSet.register(ParticleTypesPM.SPELL_BOLT.get(), SpellBoltParticle.Factory::new);
        spriteSet.register(ParticleTypesPM.OFFERING.get(), OfferingParticle.Factory::new);
        spriteSet.register(ParticleTypesPM.PROP_MARKER.get(), PropMarkerParticle.Factory::new);
        special.register(ParticleTypesPM.POTION_EXPLOSION.get(), new PotionExplosionParticle.Factory());
        special.register(ParticleTypesPM.NOTE_EMITTER.get(), new NoteEmitterParticle.Factory());
        spriteSet.register(ParticleTypesPM.SPELLCRAFTING_RUNE_U.get(), SpellcraftingRuneParticle.Factory::new);
        spriteSet.register(ParticleTypesPM.SPELLCRAFTING_RUNE_V.get(), SpellcraftingRuneParticle.Factory::new);
        spriteSet.register(ParticleTypesPM.SPELLCRAFTING_RUNE_T.get(), SpellcraftingRuneParticle.Factory::new);
        spriteSet.register(ParticleTypesPM.SPELLCRAFTING_RUNE_D.get(), SpellcraftingRuneParticle.Factory::new);
        spriteSet.register(ParticleTypesPM.INFERNAL_FLAME.get(), InfernalFlameParticle.Factory::new);
        spriteSet.register(ParticleTypesPM.AIR_CURRENT.get(), AirCurrentParticle.Factory::new);
        spriteSet.register(ParticleTypesPM.VOID_SMOKE.get(), AirCurrentParticle.Factory::new);
        sprite.register(ParticleTypesPM.DRIPPING_BLOOD_DROP.get(), DripParticlePM::createBloodDropHangParticle);
        sprite.register(ParticleTypesPM.FALLING_BLOOD_DROP.get(), DripParticlePM::createBloodDropFallParticle);
        sprite.register(ParticleTypesPM.LANDING_BLOOD_DROP.get(), DripParticlePM::createBloodDropLandParticle);
    }

    public interface SpriteParticleProviderRegistrar {
        <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider.Sprite<T> sprite);
    }

    public interface SpriteSetParticleProviderRegistrar {
        <T extends ParticleOptions> void register(ParticleType<T> type, ParticleEngine.SpriteParticleRegistration<T> registration);
    }

    public interface SpecialParticleProviderRegistrar {
        <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> provider);
    }
    
    /**
     * Register special model resource locations that must be loaded even if not tied to a block state.
     */
    public static void onModelRegister(Consumer<ModelResourceLocation> modelConsumer) {
        modelConsumer.accept(new ModelResourceLocation(ResourceUtils.loc("mundane_wand_core"), ""));
        for (WandCore core : WandCore.getAllWandCores()) {
            modelConsumer.accept(new ModelResourceLocation(core.getWandModelResourceLocationNamespace(), ""));
            modelConsumer.accept(new ModelResourceLocation(core.getStaffModelResourceLocationNamespace(), ""));
        }
        for (WandCap cap : WandCap.getAllWandCaps()) {
            modelConsumer.accept(new ModelResourceLocation(cap.getWandModelResourceLocationNamespace(), ""));
            modelConsumer.accept(new ModelResourceLocation(cap.getStaffModelResourceLocationNamespace(), ""));
        }
        for (WandGem gem : WandGem.getAllWandGems()) {
            modelConsumer.accept(new ModelResourceLocation(gem.getModelResourceLocationNamespace(), ""));
        }
        for (int index = 0; index <= 4; index++) {
            modelConsumer.accept(new ModelResourceLocation(ResourceUtils.loc("arcanometer_" + index), ""));
        }
    }
    
    public static void onClientReloadListenerRegister(Consumer<PreparableReloadListener> reloadListenerConsumer) {
        reloadListenerConsumer.accept(ItemsPM.PRIMALITE_TRIDENT.get().getRenderProperties().getCustomRenderer());
        reloadListenerConsumer.accept(ItemsPM.HEXIUM_TRIDENT.get().getRenderProperties().getCustomRenderer());
        reloadListenerConsumer.accept(ItemsPM.HALLOWSTEEL_TRIDENT.get().getRenderProperties().getCustomRenderer());
        reloadListenerConsumer.accept(ItemsPM.FORBIDDEN_TRIDENT.get().getRenderProperties().getCustomRenderer());
        reloadListenerConsumer.accept(ItemsPM.PRIMALITE_SHIELD.get().getRenderProperties().getCustomRenderer());
        reloadListenerConsumer.accept(ItemsPM.HEXIUM_SHIELD.get().getRenderProperties().getCustomRenderer());
        reloadListenerConsumer.accept(ItemsPM.HALLOWSTEEL_SHIELD.get().getRenderProperties().getCustomRenderer());
        reloadListenerConsumer.accept(ItemsPM.SPELLCRAFTING_ALTAR.get().getRenderProperties().getCustomRenderer());
        reloadListenerConsumer.accept(LexiconLoader.getOrCreateInstance());
        reloadListenerConsumer.accept(StyleGuideLoader.getOrCreateInstance());
        reloadListenerConsumer.accept(TipLoader.getOrCreateInstance());
    }
    
    @SubscribeEvent
    public static void onRegisterClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(AffinityTooltipComponent.class, ClientAffinityTooltipComponent::new);
    }
    
    @SubscribeEvent
    public static void onRegisterItemDecorations(RegisterItemDecorationsEvent event) {
        // FIXME Use the WARDABLE_ARMOR tag as the source of truth if/when the RegisterItemDecorationsEvent is made to fire *after* tag data loads
        IItemDecorator wardDecorator = new ManaStorageItemDecorator(Sources.EARTH);
        WardingModuleItem.getApplicableItems().forEach(itemSupplier -> event.register(itemSupplier.get(), wardDecorator));
    }
}
