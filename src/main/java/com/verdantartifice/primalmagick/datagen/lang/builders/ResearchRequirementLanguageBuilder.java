package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.resources.ResourceLocation;

/**
 * Helper for specifying research requirement-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class ResearchRequirementLanguageBuilder extends AbstractLanguageBuilder<SimpleResearchKey, ResearchRequirementLanguageBuilder> {
    public ResearchRequirementLanguageBuilder(SimpleResearchKey entry, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(entry, () -> String.join(".", "research", PrimalMagick.MODID, entry.getRootKey().toLowerCase()), untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return this.getBaseRegistryKey().withPrefix("research_requirement/").toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(SimpleResearchKey base) {
        return PrimalMagick.resource(base.getRootKey().toLowerCase());
    }

    @Override
    public ResearchRequirementLanguageBuilder name(String value) {
        this.add(this.getKey("text"), value);
        return this;
    }

    public ResearchRequirementLanguageBuilder hint(String value) {
        this.add(this.getKey("hint"), value);
        return this;
    }
}
