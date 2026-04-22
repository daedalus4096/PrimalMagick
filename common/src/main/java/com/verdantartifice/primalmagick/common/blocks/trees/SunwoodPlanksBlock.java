package com.verdantartifice.primalmagick.common.blocks.trees;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

/**
 * Block definition for sunwood planks.  They are decorative blocks that fade out of existence at night.
 * 
 * @author Daedalus4096
 */
public class SunwoodPlanksBlock extends AbstractPhasingBlock implements ISunwoodBlock {
    public SunwoodPlanksBlock() {
        super(Block.Properties.of().mapColor(MapColor.GOLD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).randomTicks().noOcclusion().sound(SoundType.WOOD));
    }
}
