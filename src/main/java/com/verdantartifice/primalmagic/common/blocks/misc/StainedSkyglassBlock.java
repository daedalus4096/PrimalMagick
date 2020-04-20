package com.verdantartifice.primalmagic.common.blocks.misc;

import net.minecraft.block.Block;
import net.minecraft.block.IBeaconBeamColorProvider;
import net.minecraft.item.DyeColor;

/**
 * Block definition for stained skyglass.  Like normal stained glass, except that it connects to
 * adjacent stained skyglass of the same color, reducing edge used.  It also is not destroyed upon
 * harvesting.
 * 
 * @author Daedalus4096
 */
public class StainedSkyglassBlock extends SkyglassBlock implements IBeaconBeamColorProvider {
    protected final DyeColor color;
    
    public StainedSkyglassBlock(DyeColor colorIn, Block.Properties properties) {
        super(properties);
        this.color = colorIn;
    }
    
    @Override
    public DyeColor getColor() {
        return this.color;
    }
}
