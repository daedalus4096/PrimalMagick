package com.verdantartifice.primalmagick.common.blocks.trees;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

/**
 * Block definition for sunwood pillars.  They are decorative blocks that fade out of existence at night.
 * 
 * @author Daedalus4096
 */
public class SunwoodPillarBlock extends AbstractPhasingPillarBlock implements ISunwoodBlock {
    public SunwoodPillarBlock() {
        super(Block.Properties.of().mapColor(MapColor.GOLD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).randomTicks().noOcclusion().sound(SoundType.WOOD));
    }
}
