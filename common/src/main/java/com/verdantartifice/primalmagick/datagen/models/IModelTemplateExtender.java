package com.verdantartifice.primalmagick.datagen.models;

import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface IModelTemplateExtender {
    IModelTemplateExtender withRenderType(@NotNull Identifier renderType);

    Identifier create(@NotNull Block pBlock, @NotNull TextureMapping pTextureMapping, @NotNull BiConsumer<Identifier, ModelInstance> pOutput);
    Identifier createWithSuffix(@NotNull Block pBlock, @NotNull String pSuffix, @NotNull TextureMapping pTextureMapping, @NotNull BiConsumer<Identifier, ModelInstance> pOutput);
    Identifier createWithOverride(@NotNull Block pBlock, @NotNull String pSuffix, @NotNull TextureMapping pTextureMapping, @NotNull BiConsumer<Identifier, ModelInstance> pOutput);

    Identifier create(@NotNull Block pBlock, @NotNull Function<Block, TextureMapping> pTextureMappingGetter, @NotNull BiConsumer<Identifier, ModelInstance> pOutput);
    Identifier createWithSuffix(@NotNull Block pBlock, @NotNull String pSuffix, @NotNull Function<Block, TextureMapping> pTextureMappingGetter, @NotNull BiConsumer<Identifier, ModelInstance> pOutput);
    Identifier createWithOverride(@NotNull Block pBlock, @NotNull String pSuffix, @NotNull Function<Block, TextureMapping> pTextureMappingGetter, @NotNull BiConsumer<Identifier, ModelInstance> pOutput);

    Identifier create(@NotNull Item pItem, @NotNull TextureMapping pTextureMapping, @NotNull BiConsumer<Identifier, ModelInstance> pOutput);
}
