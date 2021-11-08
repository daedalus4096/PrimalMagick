package com.verdantartifice.primalmagick.common.blocks.misc;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Block;

/**
 * Block definition for a stained skyglass pane.  Like normal stained glass panes, except that it
 * connects to adjacent stained skyglass panes of the same color, reducing edge used.  It also is
 * not destroyed upon harvesting.
 * 
 * @author Daedalus4096
 */
public class StainedSkyglassPaneBlock extends SkyglassPaneBlock implements BeaconBeamBlock {
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
