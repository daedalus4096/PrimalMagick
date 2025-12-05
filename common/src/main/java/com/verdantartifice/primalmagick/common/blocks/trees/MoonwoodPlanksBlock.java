package com.verdantartifice.primalmagick.common.blocks.trees;

import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import net.minecraft.world.level.LevelTimeAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

/**
 * Block definition for moonwood planks.  They are decorative blocks that fade out of existence during the day.
 * 
 * @author Daedalus4096
 */
public class MoonwoodPlanksBlock extends AbstractPhasingBlock {
    public MoonwoodPlanksBlock() {
        super(Block.Properties.of().mapColor(MapColor.METAL).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(2.0F, 3.0F).randomTicks().noOcclusion().sound(SoundType.WOOD));
    }

    @Override
    protected TimePhase getCurrentPhase(LevelTimeAccess world) {
        return TimePhase.getMoonPhase(world);
    }
}
