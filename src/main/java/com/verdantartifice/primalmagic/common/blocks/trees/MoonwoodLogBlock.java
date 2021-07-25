package com.verdantartifice.primalmagic.common.blocks.trees;

import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;

/**
 * Block definition for moonwood logs.  They are decorative blocks that fade out of existence and become indestructable during the day.
 * 
 * @author Daedalus4096
 */
public class MoonwoodLogBlock extends AbstractPhasingLogBlock {
    public MoonwoodLogBlock(Block stripped) {
        super(stripped, Block.Properties.of(Material.WOOD, (state) -> {
            // TODO Use different color for bark vs top?
            return state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MaterialColor.METAL : MaterialColor.METAL;
        }).strength(2.0F).randomTicks().noOcclusion().sound(SoundType.WOOD));
    }

    @Override
    public TimePhase getCurrentPhase(LevelAccessor world) {
        return TimePhase.getMoonPhase(world);
    }
}
