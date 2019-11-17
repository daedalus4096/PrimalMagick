package com.verdantartifice.primalmagic.common.blocks.trees;

import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.world.IWorld;

public class MoonwoodLogBlock extends AbstractPhasingLogBlock {
    public MoonwoodLogBlock(Block stripped) {
        super(stripped, MaterialColor.IRON, Block.Properties.create(Material.WOOD, MaterialColor.IRON).hardnessAndResistance(2.0F).tickRandomly().sound(SoundType.WOOD));
    }

    @Override
    protected TimePhase getCurrentPhase(IWorld world) {
        return TimePhase.getMoonPhase(world);
    }
}
