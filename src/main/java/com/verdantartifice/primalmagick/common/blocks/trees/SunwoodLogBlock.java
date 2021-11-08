package com.verdantartifice.primalmagick.common.blocks.trees;

import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

/**
 * Block definition for sunwood logs.  They are decorative blocks that fade out of existence and become indestructable at night.
 * 
 * @author Daedalus4096
 */
public class SunwoodLogBlock extends AbstractPhasingLogBlock {
    public SunwoodLogBlock(Block stripped) {
        super(stripped, Block.Properties.of(Material.WOOD, MaterialColor.GOLD).strength(2.0F).randomTicks().noOcclusion().sound(SoundType.WOOD));
    }

    @Override
    public TimePhase getCurrentPhase(LevelAccessor world) {
        return TimePhase.getSunPhase(world);
    }

    @Override
    public int getPulseColor() {
        return Source.SUN.getColor();
    }
}
