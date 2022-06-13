package com.verdantartifice.primalmagick.common.blocks.minerals;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;

/**
 * Block definition for quartz ore.  Like nether quartz ore, except it spawns in the overworld in
 * single block veins.
 * 
 * @author Daedalus4096
 */
public class QuartzOreBlock extends DropExperienceBlock {
    public QuartzOreBlock(Block.Properties properties) {
        super(properties, UniformInt.of(2, 5));
    }
}
