package com.verdantartifice.primalmagick.common.items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public interface IHasCustomRendererForge extends IHasCustomRenderer {
    default IClientItemExtensions getRenderPropertiesUncached() {
        return new IClientItemExtensions() {
            final BlockEntityWithoutLevelRenderer renderer = getCustomRendererSupplier().get();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        };
    }

    IClientItemExtensions getRenderProperties();
}
