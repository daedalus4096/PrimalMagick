package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;

import net.minecraft.resources.ResourceLocation;

/**
 * Helper for specifying knowledge type-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class KnowledgeTypeLanguageBuilder extends AbstractLanguageBuilder<KnowledgeType, KnowledgeTypeLanguageBuilder> {
    public KnowledgeTypeLanguageBuilder(KnowledgeType type, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(type, type::getNameTranslationKey, untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return this.getBaseRegistryKey().withPrefix("knowledge_type/").toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(KnowledgeType base) {
        return PrimalMagick.resource(base.getSerializedName());
    }
}
