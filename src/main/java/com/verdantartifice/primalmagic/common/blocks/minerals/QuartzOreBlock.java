package com.verdantartifice.primalmagic.common.blocks.minerals;

import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.util.Mth;

/**
 * Block definition for quartz ore.  Like nether quartz ore, except it spawns in the overworld in
 * single block veins.
 * 
 * @author Daedalus4096
 */
public class QuartzOreBlock extends OreBlock {
    public QuartzOreBlock(Block.Properties properties) {
        super(properties);
    }
    
    @Override
    protected int xpOnDrop(Random rng) {
        return Mth.nextInt(rng, 2, 5);
    }
}
