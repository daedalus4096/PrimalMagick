package com.verdantartifice.primalmagick.datagen.lang.builders;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.armortrim.TrimMaterialsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.armortrim.TrimMaterial;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Helper for specifying localizations for groups of armor trim materials that only differ in their
 * magickal source, in a structured way.
 * 
 * @author Daedalus4096
 */
public class TrimMaterialSourceMultipliedLanguageBuilder extends AbstractSourceMultipliedLanguageBuilder<ResourceKey<TrimMaterial>, TrimMaterialSourceMultipliedLanguageBuilder> {
    public TrimMaterialSourceMultipliedLanguageBuilder(String builderKey, List<ResourceKey<TrimMaterial>> bases,
            Function<Source, String> sourceNameMapper, Consumer<ILanguageBuilder> untracker, 
            BiConsumer<String, String> adder) {
        super(builderKey, bases, key -> String.join(".", "trim_material", Constants.MOD_ID, key.location().getPath()), TrimMaterialsPM::getSource, sourceNameMapper, untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return ResourceUtils.loc(this.builderKey).withPrefix("trim_material/").toString();
    }
}
