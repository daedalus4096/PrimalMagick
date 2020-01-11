package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.effects.EffectPM;
import com.verdantartifice.primalmagic.common.effects.FlyingEffect;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.registries.IForgeRegistry;

public class InitEffects {
    public static void initEffects(IForgeRegistry<Effect> registry) {
        registry.register(new FlyingEffect(EffectType.BENEFICIAL, Source.SKY.getColor()).setRegistryName(PrimalMagic.MODID, "flying"));
        registry.register(new EffectPM(EffectType.HARMFUL, Source.MOON.getColor()).setRegistryName(PrimalMagic.MODID, "polymorph"));
    }
}
