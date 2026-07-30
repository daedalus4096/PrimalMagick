package com.verdantartifice.primalmagick.datagen.models;

import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModelTemplateExtenderNeoforge extends AbstractModelTemplateExtender {
    private Optional<Identifier> parentOpt = Optional.empty();
    private final Map<ItemDisplayContext, TransformVecBuilderPMNeoforge> transforms = new HashMap<>();

    public ModelTemplateExtenderNeoforge(ModelTemplate modelTemplate) {
        super(modelTemplate);
    }

    @Override
    protected Identifier createInner(@NotNull Identifier modelLocation, @NotNull TextureMapping textureMapping, @NotNull BiConsumer<Identifier, ModelInstance> output) {
        ExtendedModelTemplateBuilder builder = this.modelTemplate.extend();
        this.parentOpt.ifPresent(builder::parent);
        this.transforms.forEach((type, transformBuilder) -> builder.transform(type, transformBuilder::exportTo));
        // TODO Apply other extensions from the Neoforge-specific model extender
        return builder.build().create(modelLocation, textureMapping, output);
    }

    @Override
    public IModelTemplateExtender parent(@NotNull Identifier parentId) {
        this.parentOpt = Optional.of(parentId);
        return this;
    }

    @Override
    public IModelTemplateExtender transform(@NotNull ItemDisplayContext type, @NotNull Consumer<ITransformVecBuilderPM> action) {
        Objects.requireNonNull(type);
        TransformVecBuilderPMNeoforge builder = this.transforms.computeIfAbsent(type, _ -> new TransformVecBuilderPMNeoforge());
        action.accept(builder);
        return this;
    }
}
