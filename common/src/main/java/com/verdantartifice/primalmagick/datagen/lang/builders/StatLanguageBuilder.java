package com.verdantartifice.primalmagick.datagen.lang.builders;

import com.verdantartifice.primalmagick.common.stats.Stat;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying statistics-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class StatLanguageBuilder extends AbstractLanguageBuilder<Stat, StatLanguageBuilder> {
    public StatLanguageBuilder(Stat stat, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(stat, stat::getTranslationKey, untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return this.getBaseRegistryKey().withPrefix("stats/").toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(Stat base) {
        return base.key();
    }

    public StatLanguageBuilder hint(String value) {
        this.add(this.getKey("hint"), value);
        return this;
    }
}
