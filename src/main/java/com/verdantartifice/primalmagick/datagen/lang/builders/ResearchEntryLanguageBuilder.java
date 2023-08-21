package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.resources.ResourceLocation;

/**
 * Helper for specifying research entry-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class ResearchEntryLanguageBuilder extends AbstractLanguageBuilder<SimpleResearchKey, ResearchEntryLanguageBuilder> {
    public ResearchEntryLanguageBuilder(SimpleResearchKey entry, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(entry, () -> String.join(".", "research", PrimalMagick.MODID, entry.getRootKey().toLowerCase()), untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return this.getBaseRegistryKey().withPrefix("research_entry/").toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(SimpleResearchKey base) {
        return PrimalMagick.resource(base.getRootKey().toLowerCase());
    }

    @Override
    public ResearchEntryLanguageBuilder name(String value) {
        this.add(this.getKey("title"), value);
        return this;
    }
    
    public ContentsBuilder stages() {
        return new ContentsBuilder("stage");
    }
    
    public ContentsBuilder addenda() {
        return new ContentsBuilder("addenda");
    }
    
    public class ContentsBuilder {
        private final String type;
        private int index = 1;
        
        protected ContentsBuilder(String type) {
            this.type = type;
        }
        
        public ContentsBuilder add(String value) {
            ResearchEntryLanguageBuilder.this.add(ResearchEntryLanguageBuilder.this.getKey("text", this.type, Integer.toString(this.index)), value);
            this.index++;
            return this;
        }
        
        public ResearchEntryLanguageBuilder end() {
            return ResearchEntryLanguageBuilder.this;
        }
    }
}
