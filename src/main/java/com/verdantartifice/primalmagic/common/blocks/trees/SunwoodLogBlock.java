package com.verdantartifice.primalmagic.common.blocks.trees;

import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.block.Block;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.Direction;
import net.minecraft.world.IWorld;

/**
 * Block definition for sunwood logs.  They are decorative blocks that fade out of existence and become indestructable at night.
 * 
 * @author Daedalus4096
 */
public class SunwoodLogBlock extends AbstractPhasingLogBlock {
    public SunwoodLogBlock(Block stripped) {
        super(stripped, Block.Properties.create(Material.WOOD, (state) -> {
            // TODO Use different color for bark vs top?
            return state.get(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MaterialColor.GOLD : MaterialColor.GOLD;
        }).hardnessAndResistance(2.0F).tickRandomly().notSolid().sound(SoundType.WOOD));
    }

    @Override
    public TimePhase getCurrentPhase(IWorld world) {
        return TimePhase.getSunPhase(world);
    }
}
