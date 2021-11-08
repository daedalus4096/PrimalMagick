package com.verdantartifice.primalmagick.common.blocks.trees;

import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

/**
 * Block definition for sunwood pillars.  They are decorative blocks that fade out of existence and become indestructable at night.
 * 
 * @author Daedalus4096
 */
public class SunwoodPillarBlock extends AbstractPhasingPillarBlock {
    public SunwoodPillarBlock() {
        super(Block.Properties.of(Material.WOOD, MaterialColor.GOLD).strength(2.0F).randomTicks().noOcclusion().sound(SoundType.WOOD));
    }

    @Override
    protected TimePhase getCurrentPhase(LevelAccessor world) {
        return TimePhase.getSunPhase(world);
    }
}
