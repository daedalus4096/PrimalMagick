package com.verdantartifice.primalmagic.common.blocks.ores;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ToolType;

/**
 * Block definition for rock salt ore.  Drops rock salt when mined, which can be smelted into
 * refined salt for use in cooking or rituals.
 * 
 * @author Daedalus4096
 */
public class RockSaltOreBlock extends OreBlock {
    public RockSaltOreBlock() {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1));
    }
    
    @Override
    protected int getExperience(Random rand) {
        return MathHelper.nextInt(rand, 1, 4);
    }
}
