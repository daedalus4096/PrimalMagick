package com.verdantartifice.primalmagick.common.blocks.trees;

import com.verdantartifice.primalmagick.common.sources.Sources;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

/**
 * Block definition for sunwood logs.  They are decorative blocks that fade out of existence and become indestructable at night.
 * 
 * @author Daedalus4096
 */
public class SunwoodLogBlock extends AbstractPhasingLogBlock implements ISunwoodBlock {
    public SunwoodLogBlock(Block stripped, BlockBehaviour.Properties baseProperties) {
        super(stripped, baseProperties);
    }

    @Override
    public int getPulseColor() {
        return Sources.SUN.getColor();
    }
}
