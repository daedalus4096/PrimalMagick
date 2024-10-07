package com.verdantartifice.primalmagick.common.effects;

import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.util.function.Supplier;

/**
 * Deferred registry for mod potion effect types.
 * 
 * @author Daedalus4096
 */
public class EffectsPM {
    public static final IRegistryItem<MobEffect, FlyingEffect> FLYING = register("flying", () -> new FlyingEffect(MobEffectCategory.BENEFICIAL, Sources.SKY.getColor()));
    public static final IRegistryItem<MobEffect, EffectPM> POLYMORPH = register("polymorph", () -> new EffectPM(MobEffectCategory.HARMFUL, Sources.MOON.getColor()));
    public static final IRegistryItem<MobEffect, BleedingEffect> BLEEDING = register("bleeding", () -> new BleedingEffect(MobEffectCategory.HARMFUL, Sources.BLOOD.getColor()));
    public static final IRegistryItem<MobEffect, EffectPM> WEAKENED_SOUL = register("weakened_soul", () -> new EffectPM(MobEffectCategory.HARMFUL, Sources.HALLOWED.getColor()));
    public static final IRegistryItem<MobEffect, EffectPM> MANAFRUIT = register("manafruit", () -> new EffectPM(MobEffectCategory.BENEFICIAL, 0x27E1C7));
    public static final IRegistryItem<MobEffect, EffectPM> DRAIN_SOUL = register("drain_soul", () -> new EffectPM(MobEffectCategory.HARMFUL, Sources.INFERNAL.getColor()));
    public static final IRegistryItem<MobEffect, EffectPM> MANA_IMPEDANCE = register("mana_impedance", () -> new EffectPM(MobEffectCategory.HARMFUL, 0x808080));
    public static final IRegistryItem<MobEffect, EffectPM> ENDERLOCK = register("enderlock", () -> new EffectPM(MobEffectCategory.HARMFUL, Sources.VOID.getColor()));
    public static final IRegistryItem<MobEffect, EffectPM> SOULPIERCED = register("soulpierced", () -> new EffectPM(MobEffectCategory.NEUTRAL, 0x808080));

    private static <T extends MobEffect> IRegistryItem<MobEffect, T> register(String name, Supplier<T> supplier) {
        return Services.MOB_EFFECTS.register(name, supplier);
    }
}
