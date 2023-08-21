package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.resources.ResourceLocation;

/**
 * Helper for specifying written book localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class WrittenBookLanguageBuilder extends AbstractLanguageBuilder<String, WrittenBookLanguageBuilder> {
    private int index = 1;
    
    public WrittenBookLanguageBuilder(String title, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(title, () -> String.join(".", "written_book", PrimalMagick.MODID, title.toLowerCase()), untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return this.getBaseRegistryKey().withPrefix("written_book/").toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(String base) {
        return PrimalMagick.resource(base.toLowerCase());
    }

    @Override
    public WrittenBookLanguageBuilder name(String value) {
        this.add(this.getKey("title"), value);
        return this;
    }
    
    public WrittenBookLanguageBuilder text(String value) {
        this.add(this.getKey("text", Integer.toString(this.index)), value);
        this.index++;
        return this;
    }
}
