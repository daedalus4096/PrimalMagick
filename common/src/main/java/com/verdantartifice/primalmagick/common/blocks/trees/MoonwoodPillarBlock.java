package com.verdantartifice.primalmagick.common.blocks.trees;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

/**
 * Block definition for moonwood pillars.  They are decorative blocks that fade out of existence during the day.
 * 
 * @author Daedalus4096
 */
public class MoonwoodPillarBlock extends AbstractPhasingPillarBlock implements IMoonwoodBlock {
    public MoonwoodPillarBlock() {
        super(Block.Properties.of().mapColor(MapColor.METAL).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F).randomTicks().noOcclusion().sound(SoundType.WOOD));
    }
}
