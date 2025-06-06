package com.verdantartifice.primalmagick.datagen.lang.builders;

import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying entity type-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class EntityTypeLanguageBuilder extends AbstractLanguageBuilder<EntityType<?>, EntityTypeLanguageBuilder> {
    public EntityTypeLanguageBuilder(EntityType<?> entityType, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(entityType, entityType::getDescriptionId, untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return ResourceKey.create(Registries.ENTITY_TYPE, this.getBaseRegistryKey()).toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(EntityType<?> base) {
        return Objects.requireNonNull(Services.ENTITY_TYPES_REGISTRY.getKey(base));
    }
}
