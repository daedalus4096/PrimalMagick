package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.armortrim.TrimMaterialsPM;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.armortrim.TrimMaterial;

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
        super(builderKey, bases, key -> String.join(".", "trim_material", PrimalMagick.MODID, key.location().getPath()), TrimMaterialsPM::getSource, sourceNameMapper, untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return PrimalMagick.resource(this.builderKey).withPrefix("trim_material/").toString();
    }
}
