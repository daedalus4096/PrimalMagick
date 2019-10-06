package com.verdantartifice.primalmagic.client.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagic.client.fx.particles.WandPoofParticle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistrationEvents {
    @SubscribeEvent
    public static void registerParticleTypes(RegistryEvent.Register<ParticleType<?>> event) {
        event.getRegistry().register(new BasicParticleType(true).setRegistryName(PrimalMagic.MODID, "wand_poof"));
    }
    
    @SubscribeEvent
    public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
        Minecraft mc = Minecraft.getInstance();
        mc.particles.registerFactory(ParticleTypesPM.WAND_POOF, WandPoofParticle.Factory::new);
    }
    
    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(new ModelResourceLocation(new ResourceLocation(PrimalMagic.MODID, "wand_core"), ""));
    }
}
