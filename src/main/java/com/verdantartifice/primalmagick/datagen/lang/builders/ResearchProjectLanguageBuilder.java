package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.resources.ResourceLocation;

/**
 * Helper for specifying theorycrafting project-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class ResearchProjectLanguageBuilder extends AbstractLanguageBuilder<String, ResearchProjectLanguageBuilder> {
    public ResearchProjectLanguageBuilder(String id, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(id, () -> String.join(".", "research_project", PrimalMagick.MODID, id.toLowerCase()), untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return this.getBaseRegistryKey().withPrefix("research_projects/").toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(String base) {
        return PrimalMagick.resource(base.toLowerCase());
    }

    @Override
    public ResearchProjectLanguageBuilder name(String value) {
        this.add(this.getKey("name"), value);
        return this;
    }

    public ResearchProjectLanguageBuilder text(String value) {
        this.add(this.getKey("text"), value);
        return this;
    }
}
