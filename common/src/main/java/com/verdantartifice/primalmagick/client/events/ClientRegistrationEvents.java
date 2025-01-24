package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.client.books.LexiconLoader;
import com.verdantartifice.primalmagick.client.books.StyleGuideLoader;
import com.verdantartifice.primalmagick.client.fx.particles.DripParticlePM;
import com.verdantartifice.primalmagick.client.fx.particles.NoteEmitterParticle;
import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagick.client.fx.particles.PotionExplosionParticle;
import com.verdantartifice.primalmagick.client.tips.TipLoader;
import com.verdantartifice.primalmagick.client.tooltips.ClientAffinityTooltipComponent;
import com.verdantartifice.primalmagick.common.affinities.AffinityTooltipComponent;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Respond to client-only Forge registration events.
 * 
 * @author Daedalus4096
 */
public class ClientRegistrationEvents {
    public static void onRegisterParticleProviders(SpriteParticleProviderRegistrar sprite, SpecialParticleProviderRegistrar special) {
        // FIXME Sprite set particles are registered in platform-specific code due to access transformer weirdness
        special.register(ParticleTypesPM.POTION_EXPLOSION.get(), new PotionExplosionParticle.Factory());
        special.register(ParticleTypesPM.NOTE_EMITTER.get(), new NoteEmitterParticle.Factory());
        sprite.register(ParticleTypesPM.DRIPPING_BLOOD_DROP.get(), DripParticlePM::createBloodDropHangParticle);
        sprite.register(ParticleTypesPM.FALLING_BLOOD_DROP.get(), DripParticlePM::createBloodDropFallParticle);
        sprite.register(ParticleTypesPM.LANDING_BLOOD_DROP.get(), DripParticlePM::createBloodDropLandParticle);
    }

    public interface SpriteParticleProviderRegistrar {
        <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider.Sprite<T> sprite);
    }

    public interface SpecialParticleProviderRegistrar {
        <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> provider);
    }
    
    /**
     * Register special model resource locations that must be loaded even if not tied to a block state.
     */
    public static void onModelRegister(Consumer<ModelResourceLocation> modelConsumer, String variant) {
        modelConsumer.accept(new ModelResourceLocation(ResourceUtils.loc("mundane_wand_core"), variant));
        for (WandCore core : WandCore.getAllWandCores()) {
            modelConsumer.accept(new ModelResourceLocation(core.getWandModelResourceLocationNamespace(), variant));
            modelConsumer.accept(new ModelResourceLocation(core.getStaffModelResourceLocationNamespace(), variant));
        }
        for (WandCap cap : WandCap.getAllWandCaps()) {
            modelConsumer.accept(new ModelResourceLocation(cap.getWandModelResourceLocationNamespace(), variant));
            modelConsumer.accept(new ModelResourceLocation(cap.getStaffModelResourceLocationNamespace(), variant));
        }
        for (WandGem gem : WandGem.getAllWandGems()) {
            modelConsumer.accept(new ModelResourceLocation(gem.getModelResourceLocationNamespace(), variant));
        }
        for (int index = 0; index <= 4; index++) {
            modelConsumer.accept(new ModelResourceLocation(ResourceUtils.loc("arcanometer_" + index), variant));
        }
    }
    
    public static void onClientReloadListenerRegister(Consumer<PreparableReloadListener> reloadListenerConsumer) {
        reloadListenerConsumer.accept(ItemsPM.PRIMALITE_TRIDENT.get().getCustomRendererSupplier().get());
        reloadListenerConsumer.accept(ItemsPM.HEXIUM_TRIDENT.get().getCustomRendererSupplier().get());
        reloadListenerConsumer.accept(ItemsPM.HALLOWSTEEL_TRIDENT.get().getCustomRendererSupplier().get());
        reloadListenerConsumer.accept(ItemsPM.FORBIDDEN_TRIDENT.get().getCustomRendererSupplier().get());
        reloadListenerConsumer.accept(ItemsPM.PRIMALITE_SHIELD.get().getCustomRendererSupplier().get());
        reloadListenerConsumer.accept(ItemsPM.HEXIUM_SHIELD.get().getCustomRendererSupplier().get());
        reloadListenerConsumer.accept(ItemsPM.HALLOWSTEEL_SHIELD.get().getCustomRendererSupplier().get());
        reloadListenerConsumer.accept(ItemsPM.SPELLCRAFTING_ALTAR.get().getCustomRendererSupplier().get());
        reloadListenerConsumer.accept(LexiconLoader.getOrCreateInstance());
        reloadListenerConsumer.accept(StyleGuideLoader.getOrCreateInstance());
        reloadListenerConsumer.accept(TipLoader.getOrCreateInstance());
    }
    
    public static void onRegisterClientTooltipComponentFactories(TooltipComponentRegistrar clientTooltipComponentRegistrar) {
        clientTooltipComponentRegistrar.register(AffinityTooltipComponent.class, ClientAffinityTooltipComponent::new);
    }

    public interface TooltipComponentRegistrar {
        <T extends TooltipComponent> void register(Class<T> type, Function<? super T, ? extends ClientTooltipComponent> factory);
    }
}
