package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.armortrim.TrimPattern;

/**
 * Helper for specifying armor trim pattern-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class TrimPatternLanguageBuilder extends AbstractLanguageBuilder<ResourceKey<TrimPattern>, TrimPatternLanguageBuilder> {
    public TrimPatternLanguageBuilder(ResourceKey<TrimPattern> patternKey, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(patternKey, () -> String.join(".", "trim_pattern", PrimalMagick.MODID, patternKey.location().getPath()), untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return ResourceKey.create(Registries.TRIM_PATTERN, this.getBaseRegistryKey()).toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(ResourceKey<TrimPattern> base) {
        return base.location();
    }
}
