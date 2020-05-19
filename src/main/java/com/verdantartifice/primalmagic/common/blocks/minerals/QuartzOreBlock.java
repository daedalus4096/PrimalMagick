package com.verdantartifice.primalmagic.common.blocks.minerals;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;

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
    protected int getExperience(Random rng) {
        return MathHelper.nextInt(rng, 2, 5);
    }
}
