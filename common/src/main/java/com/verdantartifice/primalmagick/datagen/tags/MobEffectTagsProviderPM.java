package com.verdantartifice.primalmagick.datagen.tags;

import java.util.concurrent.CompletableFuture;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.tags.MobEffectTagsPM;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MobEffectTagsProviderPM extends TagsProvider<MobEffect> {
    public MobEffectTagsProviderPM(PackOutput output, CompletableFuture<HolderLookup.Provider> future, ExistingFileHelper helper) {
        super(output, Registries.MOB_EFFECT, future, Constants.MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(MobEffectTagsPM.IMMUNITY_PRIMALITE_GOLEM).add(MobEffects.POISON.unwrapKey().get(), EffectsPM.BLEEDING.getKey());
        this.tag(MobEffectTagsPM.IMMUNITY_HEXIUM_GOLEM).add(MobEffects.POISON.unwrapKey().get(), EffectsPM.BLEEDING.getKey());
        this.tag(MobEffectTagsPM.IMMUNITY_HALLOWSTEEL_GOLEM).add(MobEffects.POISON.unwrapKey().get(), EffectsPM.BLEEDING.getKey(), MobEffects.WITHER.unwrapKey().get());
    }
}
