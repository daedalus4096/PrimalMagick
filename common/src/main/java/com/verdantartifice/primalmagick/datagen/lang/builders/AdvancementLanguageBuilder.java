package com.verdantartifice.primalmagick.datagen.lang.builders;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying advancement-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class AdvancementLanguageBuilder extends AbstractLanguageBuilder<String, AdvancementLanguageBuilder> {
    public AdvancementLanguageBuilder(String id, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(id, () -> String.join(".", "advancements", Constants.MOD_ID, id.toLowerCase()), untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return this.getBaseRegistryKey().withPrefix("advancements/").toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(String base) {
        return ResourceUtils.loc(base.toLowerCase());
    }

    @Override
    public AdvancementLanguageBuilder name(String value) {
        this.add(this.getKey("title"), value);
        return this;
    }

    public AdvancementLanguageBuilder description(String value) {
        this.add(this.getKey("description"), value);
        return this;
    }
}
