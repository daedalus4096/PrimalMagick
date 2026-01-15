package com.verdantartifice.primalmagick.datagen.tags;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.tags.MobEffectTagsPM;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MobEffectTagsProviderPMNeoforge extends IntrinsicHolderTagsProvider<MobEffect> {
    public MobEffectTagsProviderPMNeoforge(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.MOB_EFFECT, future, mobEffect -> BuiltInRegistries.MOB_EFFECT.getResourceKey(mobEffect).get(), Constants.MOD_ID);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider pProvider) {
        this.tag(MobEffectTagsPM.IMMUNITY_PRIMALITE_GOLEM).add(MobEffects.POISON.value(), EffectsPM.BLEEDING.get());
        this.tag(MobEffectTagsPM.IMMUNITY_HEXIUM_GOLEM).add(MobEffects.POISON.value(), EffectsPM.BLEEDING.get());
        this.tag(MobEffectTagsPM.IMMUNITY_HALLOWSTEEL_GOLEM).add(MobEffects.POISON.value(), EffectsPM.BLEEDING.get(), MobEffects.WITHER.value());
    }
}
