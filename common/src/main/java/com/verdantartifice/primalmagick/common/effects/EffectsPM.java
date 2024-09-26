package com.verdantartifice.primalmagick.common.effects;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.sources.Sources;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod potion effect types.
 * 
 * @author Daedalus4096
 */
public class EffectsPM {
    private static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Constants.MOD_ID);
    
    public static void init() {
        EFFECTS.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
    
    public static final RegistryObject<MobEffect> FLYING = EFFECTS.register("flying", () -> new FlyingEffect(MobEffectCategory.BENEFICIAL, Sources.SKY.getColor()));
    public static final RegistryObject<MobEffect> POLYMORPH = EFFECTS.register("polymorph", () -> new EffectPM(MobEffectCategory.HARMFUL, Sources.MOON.getColor()));
    public static final RegistryObject<MobEffect> BLEEDING = EFFECTS.register("bleeding", () -> new BleedingEffect(MobEffectCategory.HARMFUL, Sources.BLOOD.getColor()));
    public static final RegistryObject<MobEffect> WEAKENED_SOUL = EFFECTS.register("weakened_soul", () -> new EffectPM(MobEffectCategory.HARMFUL, Sources.HALLOWED.getColor()));
    public static final RegistryObject<MobEffect> MANAFRUIT = EFFECTS.register("manafruit", () -> new EffectPM(MobEffectCategory.BENEFICIAL, 0x27E1C7));
    public static final RegistryObject<MobEffect> DRAIN_SOUL = EFFECTS.register("drain_soul", () -> new EffectPM(MobEffectCategory.HARMFUL, Sources.INFERNAL.getColor()));
    public static final RegistryObject<MobEffect> MANA_IMPEDANCE = EFFECTS.register("mana_impedance", () -> new EffectPM(MobEffectCategory.HARMFUL, 0x808080));
    public static final RegistryObject<MobEffect> ENDERLOCK = EFFECTS.register("enderlock", () -> new EffectPM(MobEffectCategory.HARMFUL, Sources.VOID.getColor()));
    public static final RegistryObject<MobEffect> SOULPIERCED = EFFECTS.register("soulpierced", () -> new EffectPM(MobEffectCategory.NEUTRAL, 0x808080));
}
