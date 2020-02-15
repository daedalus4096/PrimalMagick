package com.verdantartifice.primalmagic.common.effects;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Deferred registry for mod potion effect types.
 * 
 * @author Daedalus4096
 */
public class EffectsPM {
    private static final DeferredRegister<Effect> EFFECTS = new DeferredRegister<>(ForgeRegistries.POTIONS, PrimalMagic.MODID);
    
    public static void init() {
        EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<Effect> FLYING = EFFECTS.register("flying", () -> new FlyingEffect(EffectType.BENEFICIAL, Source.SKY.getColor()));
    public static final RegistryObject<Effect> POLYMORPH = EFFECTS.register("polymorph", () -> new EffectPM(EffectType.HARMFUL, Source.MOON.getColor()));
}
