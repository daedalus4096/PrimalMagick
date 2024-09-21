package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

/**
 * Helper for specifying damage type-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class DamageTypeLanguageBuilder extends AbstractLanguageBuilder<ResourceKey<DamageType>, DamageTypeLanguageBuilder> {
    public DamageTypeLanguageBuilder(ResourceKey<DamageType> patternKey, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(patternKey, () -> String.join(".", "death", "attack", PrimalMagick.MODID, patternKey.location().getPath()), untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return ResourceKey.create(Registries.DAMAGE_TYPE, this.getBaseRegistryKey()).toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(ResourceKey<DamageType> base) {
        return base.location();
    }
    
    public DamageTypeLanguageBuilder player(String value) {
        this.add(this.getKey("player"), value);
        return this;
    }
    
    public DamageTypeLanguageBuilder item(String value) {
        this.add(this.getKey("item"), value);
        return this;
    }
}
