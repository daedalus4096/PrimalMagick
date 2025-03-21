package com.verdantartifice.primalmagick.datagen.lang.builders;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying spell vehicle-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractSpellComponentLanguageBuilder extends AbstractLanguageBuilder<String, AbstractSpellComponentLanguageBuilder> {
    protected final String componentType;
    
    public AbstractSpellComponentLanguageBuilder(String componentName, String componentType, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(componentName, () -> String.join(".", "spells", Constants.MOD_ID, componentType, componentName), untracker, adder);
        this.componentType = componentType;
    }

    @Override
    public String getBuilderKey() {
        return this.getBaseRegistryKey().withPrefix("spell_" + this.componentType + "/").toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(String base) {
        return ResourceUtils.loc(base.toLowerCase());
    }

    @Override
    public AbstractSpellComponentLanguageBuilder name(String value) {
        this.add(this.getKey("type"), value);
        return this;
    }
    
    public AbstractSpellComponentLanguageBuilder defaultName(String value) {
        this.add(this.getKey("default_name"), value);
        return this;
    }
    
    public AbstractSpellComponentLanguageBuilder detailTooltip(String value) {
        this.add(this.getKey("detail_tooltip"), value);
        return this;
    }
}
