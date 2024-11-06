package com.verdantartifice.primalmagick.common.items.misc;

import com.verdantartifice.primalmagick.client.renderers.itemstack.ManaFontISTER;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class ManaFontBlockItemForge extends ManaFontBlockItem {
    public ManaFontBlockItemForge(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            final BlockEntityWithoutLevelRenderer renderer = new ManaFontISTER();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }
}
