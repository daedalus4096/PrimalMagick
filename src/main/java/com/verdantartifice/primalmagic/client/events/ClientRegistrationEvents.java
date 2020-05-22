package com.verdantartifice.primalmagic.client.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.fx.particles.ManaSparkleParticle;
import com.verdantartifice.primalmagic.client.fx.particles.OfferingParticle;
import com.verdantartifice.primalmagic.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagic.client.fx.particles.PropMarkerParticle;
import com.verdantartifice.primalmagic.client.fx.particles.SpellBoltParticle;
import com.verdantartifice.primalmagic.client.fx.particles.SpellSparkleParticle;
import com.verdantartifice.primalmagic.client.fx.particles.WandPoofParticle;
import com.verdantartifice.primalmagic.common.wands.WandCap;
import com.verdantartifice.primalmagic.common.wands.WandCore;
import com.verdantartifice.primalmagic.common.wands.WandGem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Respond to client-only Forge registration events.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistrationEvents {
    @SubscribeEvent
    public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
        Minecraft mc = Minecraft.getInstance();
        mc.particles.registerFactory(ParticleTypesPM.WAND_POOF.get(), WandPoofParticle.Factory::new);
        mc.particles.registerFactory(ParticleTypesPM.MANA_SPARKLE.get(), ManaSparkleParticle.Factory::new);
        mc.particles.registerFactory(ParticleTypesPM.SPELL_SPARKLE.get(), SpellSparkleParticle.Factory::new);
        mc.particles.registerFactory(ParticleTypesPM.SPELL_BOLT.get(), SpellBoltParticle.Factory::new);
        mc.particles.registerFactory(ParticleTypesPM.OFFERING.get(), OfferingParticle.Factory::new);
        mc.particles.registerFactory(ParticleTypesPM.PROP_MARKER.get(), PropMarkerParticle.Factory::new);
    }
    
    /**
     * Register special model resource locations that must be loaded even if not tied to a block state.
     * 
     * @param event
     */
    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(new ModelResourceLocation(new ResourceLocation(PrimalMagic.MODID, "mundane_wand_core"), ""));
        for (WandCore core : WandCore.getAllWandCores()) {
            ModelLoader.addSpecialModel(core.getModelResourceLocation());
        }
        for (WandCap cap : WandCap.getAllWandCaps()) {
            ModelLoader.addSpecialModel(cap.getModelResourceLocation());
        }
        for (WandGem gem : WandGem.getAllWandGems()) {
            ModelLoader.addSpecialModel(gem.getModelResourceLocation());
        }
        for (int index = 0; index <= 4; index++) {
            ModelLoader.addSpecialModel(new ModelResourceLocation(new ResourceLocation(PrimalMagic.MODID, "arcanometer_" + index), ""));
        }
    }
}
