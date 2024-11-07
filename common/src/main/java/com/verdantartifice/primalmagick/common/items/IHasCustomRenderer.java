package com.verdantartifice.primalmagick.common.items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

import java.util.function.Supplier;

public interface IHasCustomRenderer {
    Supplier<BlockEntityWithoutLevelRenderer> getCustomRendererSupplier();
}
