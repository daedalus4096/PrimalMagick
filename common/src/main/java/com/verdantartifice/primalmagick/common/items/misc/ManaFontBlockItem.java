package com.verdantartifice.primalmagick.common.items.misc;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Definition of a block item for a mana font.
 * 
 * @author Daedalus4096
 */
public abstract class ManaFontBlockItem extends BlockItem {
    protected static final List<ManaFontBlockItem> FONTS = new ArrayList<>();

    public ManaFontBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
        FONTS.add(this);
    }

    public static Collection<ManaFontBlockItem> getAllFonts() {
        return Collections.unmodifiableList(FONTS);
    }
}
