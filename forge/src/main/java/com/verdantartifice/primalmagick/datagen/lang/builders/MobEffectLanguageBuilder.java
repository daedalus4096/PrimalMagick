package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Helper for specifying status effect-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class MobEffectLanguageBuilder extends AbstractLanguageBuilder<MobEffect, MobEffectLanguageBuilder> {
    public MobEffectLanguageBuilder(MobEffect effect, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(effect, effect::getDescriptionId, untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return ResourceKey.create(Registries.MOB_EFFECT, this.getBaseRegistryKey()).toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(MobEffect effect) {
        return Objects.requireNonNull(ForgeRegistries.MOB_EFFECTS.getKey(effect));
    }
}
