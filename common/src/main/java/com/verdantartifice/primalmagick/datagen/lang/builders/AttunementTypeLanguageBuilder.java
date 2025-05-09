package com.verdantartifice.primalmagick.datagen.lang.builders;

import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying attunement type-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class AttunementTypeLanguageBuilder extends AbstractLanguageBuilder<AttunementType, AttunementTypeLanguageBuilder> {
    public AttunementTypeLanguageBuilder(AttunementType type, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(type, type::getNameTranslationKey, untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return this.getBaseRegistryKey().withPrefix("attunement_type/").toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(AttunementType base) {
        return ResourceUtils.loc(base.getSerializedName());
    }
}
