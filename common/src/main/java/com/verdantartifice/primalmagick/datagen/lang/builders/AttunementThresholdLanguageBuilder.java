package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.sources.Source;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.resources.ResourceLocation;

/**
 * Helper for specifying attunement threshold-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class AttunementThresholdLanguageBuilder extends AbstractLanguageBuilder<AttunementThreshold, AttunementThresholdLanguageBuilder> {
    public AttunementThresholdLanguageBuilder(AttunementThreshold threshold, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(threshold, threshold::getNameTranslationKey, untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return this.getBaseRegistryKey().withPrefix("attunement_threshold/").toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(AttunementThreshold base) {
        return ResourceUtils.loc(base.getSerializedName());
    }
    
    public AttunementThresholdLanguageBuilder effect(Source source, String value) {
        this.add(this.getKey(source.getId().getPath()), value);
        return this;
    }
}
