package com.verdantartifice.primalmagick.datagen.lang.builders;

import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
        return ResourceUtils.loc(base.getSerializedName());
    }
}
