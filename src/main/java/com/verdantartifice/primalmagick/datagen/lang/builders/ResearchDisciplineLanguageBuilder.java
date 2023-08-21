package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;

import net.minecraft.resources.ResourceLocation;

/**
 * Helper for specifying research discipline-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class ResearchDisciplineLanguageBuilder extends AbstractLanguageBuilder<ResearchDiscipline, ResearchDisciplineLanguageBuilder> {
    public ResearchDisciplineLanguageBuilder(ResearchDiscipline disc, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(disc, disc::getNameTranslationKey, untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return this.getBaseRegistryKey().withPrefix("research_discipline/").toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(ResearchDiscipline base) {
        return PrimalMagick.resource(base.getKey().toLowerCase());
    }
}
