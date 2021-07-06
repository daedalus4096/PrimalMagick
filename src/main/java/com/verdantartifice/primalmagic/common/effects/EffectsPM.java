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
    private static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, PrimalMagic.MODID);
    
    public static void init() {
        EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<Effect> FLYING = EFFECTS.register("flying", () -> new FlyingEffect(EffectType.BENEFICIAL, Source.SKY.getColor()));
    public static final RegistryObject<Effect> POLYMORPH = EFFECTS.register("polymorph", () -> new EffectPM(EffectType.HARMFUL, Source.MOON.getColor()));
    public static final RegistryObject<Effect> BLEEDING = EFFECTS.register("bleeding", () -> new BleedingEffect(EffectType.HARMFUL, Source.BLOOD.getColor()));
    public static final RegistryObject<Effect> WEAKENED_SOUL = EFFECTS.register("weakened_soul", () -> new EffectPM(EffectType.HARMFUL, Source.HALLOWED.getColor()));
    public static final RegistryObject<Effect> MANAFRUIT = EFFECTS.register("manafruit", () -> new EffectPM(EffectType.BENEFICIAL, 0x27E1C7));
    public static final RegistryObject<Effect> DRAIN_SOUL = EFFECTS.register("drain_soul", () -> new EffectPM(EffectType.HARMFUL, Source.INFERNAL.getColor()));
    public static final RegistryObject<Effect> MANA_IMPEDANCE = EFFECTS.register("mana_impedance", () -> new EffectPM(EffectType.HARMFUL, 0x808080));
    public static final RegistryObject<Effect> ENDERLOCK = EFFECTS.register("enderlock", () -> new EffectPM(EffectType.HARMFUL, Source.VOID.getColor()));
    public static final RegistryObject<Effect> SOULPIERCED = EFFECTS.register("soulpierced", () -> new EffectPM(EffectType.NEUTRAL, 0x808080));
}
