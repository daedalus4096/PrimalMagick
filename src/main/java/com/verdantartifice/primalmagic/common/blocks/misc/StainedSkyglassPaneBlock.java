package com.verdantartifice.primalmagic.common.blocks.misc;

import net.minecraft.block.Block;
import net.minecraft.block.IBeaconBeamColorProvider;
import net.minecraft.item.DyeColor;

/**
 * Block definition for a stained skyglass pane.  Like normal stained glass panes, except that it
 * connects to adjacent stained skyglass panes of the same color, reducing edge used.  It also is
 * not destroyed upon harvesting.
 * 
 * @author Daedalus4096
 */
public class StainedSkyglassPaneBlock extends SkyglassPaneBlock implements IBeaconBeamColorProvider {
    protected final DyeColor color;

    public StainedSkyglassPaneBlock(DyeColor colorIn, Block.Properties properties) {
        super(properties);
        this.color = colorIn;
    }
    
    @Override
    public DyeColor getColor() {
        return this.color;
    }
}
