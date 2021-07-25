package com.verdantartifice.primalmagic.common.blocks.trees;

import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.LevelAccessor;

/**
 * Block definition for sunwood leaves.  They are decorative blocks that fade out of existence and become indestructable at night.
 * 
 * @author Daedalus4096
 */
public class SunwoodLeavesBlock extends AbstractPhasingLeavesBlock {
    public SunwoodLeavesBlock() {
        super(Block.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.GRASS).lightLevel((state) -> {
            return state.getValue(PHASE).getLightLevel();
        }).isSuffocating((state, blockReader, pos) -> {
        	return false;
        }).isViewBlocking((state, blockReader, pos) -> {
        	return false;
        }).isValidSpawn(AbstractPhasingLeavesBlock::allowsSpawnOnLeaves));
    }

    @Override
    public TimePhase getCurrentPhase(LevelAccessor world) {
        return TimePhase.getSunPhase(world);
    }
}
