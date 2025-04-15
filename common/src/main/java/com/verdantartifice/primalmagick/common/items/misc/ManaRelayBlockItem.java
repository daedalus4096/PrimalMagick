package com.verdantartifice.primalmagick.common.items.misc;

import com.verdantartifice.primalmagick.client.renderers.itemstack.ManaRelayISTER;
import com.verdantartifice.primalmagick.common.items.IHasCustomRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public abstract class ManaRelayBlockItem extends BlockItem implements IHasCustomRenderer {
    private BlockEntityWithoutLevelRenderer customRenderer;

    public ManaRelayBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public Supplier<BlockEntityWithoutLevelRenderer> getCustomRendererSupplier() {
        if (this.customRenderer == null) {
            this.customRenderer = this.getCustomRendererSupplierUncached().get();
        }
        return () -> this.customRenderer;
    }

    @Override
    public Supplier<BlockEntityWithoutLevelRenderer> getCustomRendererSupplierUncached() {
        return ManaRelayISTER::new;
    }
}
