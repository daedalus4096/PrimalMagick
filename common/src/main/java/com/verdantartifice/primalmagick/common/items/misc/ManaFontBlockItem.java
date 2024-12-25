package com.verdantartifice.primalmagick.common.items.misc;

import com.verdantartifice.primalmagick.client.renderers.itemstack.ManaFontISTER;
import com.verdantartifice.primalmagick.common.items.IHasCustomRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Definition of a block item for a mana font.
 * 
 * @author Daedalus4096
 */
public abstract class ManaFontBlockItem extends BlockItem implements IHasCustomRenderer {
    protected static final List<ManaFontBlockItem> FONTS = new ArrayList<>();

    public ManaFontBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
        FONTS.add(this);
    }

    public static Collection<ManaFontBlockItem> getAllFonts() {
        return Collections.unmodifiableList(FONTS);
    }

    @Override
    public Supplier<BlockEntityWithoutLevelRenderer> getCustomRendererSupplier() {
        return ManaFontISTER::new;
    }
}
