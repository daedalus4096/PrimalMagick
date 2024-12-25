package com.verdantartifice.primalmagick.datagen.lang.builders;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.wands.IWandComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying wand component-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class WandComponentLanguageBuilder extends AbstractLanguageBuilder<IWandComponent, WandComponentLanguageBuilder> {
    public WandComponentLanguageBuilder(IWandComponent component, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(component, component::getNameTranslationKey, untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return this.getBaseRegistryKey().withPrefix(this.base.getComponentType().getSerializedName() + "/").toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(IWandComponent base) {
        return ResourceUtils.loc(base.getTag());
    }
}
