package com.verdantartifice.primalmagick.datagen.lang.builders;

import com.verdantartifice.primalmagick.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.armortrim.TrimMaterial;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying armor trim material-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class TrimMaterialLanguageBuilder extends AbstractLanguageBuilder<ResourceKey<TrimMaterial>, TrimMaterialLanguageBuilder> {
    public TrimMaterialLanguageBuilder(ResourceKey<TrimMaterial> materialKey, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(materialKey, () -> String.join(".", "trim_material", Constants.MOD_ID, materialKey.location().getPath()), untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return ResourceKey.create(Registries.TRIM_MATERIAL, this.getBaseRegistryKey()).toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(ResourceKey<TrimMaterial> base) {
        return base.location();
    }
}
