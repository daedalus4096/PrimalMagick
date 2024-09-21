package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

/**
 * Helper for specifying key mapping-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class KeyMappingLanguageBuilder extends AbstractLanguageBuilder<KeyMapping, KeyMappingLanguageBuilder> {
    protected final String regName;
    
    public KeyMappingLanguageBuilder(KeyMapping mapping, String regName, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(mapping, mapping::getName, untracker, adder);
        this.regName = regName;
    }

    @Override
    public String getBuilderKey() {
        return this.getBaseRegistryKey().withPrefix("key_mapping/").toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(KeyMapping base) {
        return PrimalMagick.resource(this.regName.toLowerCase());
    }
}
