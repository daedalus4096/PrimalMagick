package com.verdantartifice.primalmagick.common.blocks.trees;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

/**
 * Block definition for sunwood leaves.  They are decorative blocks that fade out of existence at night.
 * 
 * @author Daedalus4096
 */
public class SunwoodLeavesBlock extends AbstractPhasingLeavesBlock implements ISunwoodBlock {
    public SunwoodLeavesBlock() {
        super(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.GRASS).lightLevel((state) -> {
            return state.getValue(PHASE).getLightLevel();
        }).isSuffocating((state, blockReader, pos) -> {
            return false;
        }).isViewBlocking((state, blockReader, pos) -> {
            return false;
        }).isValidSpawn(AbstractPhasingLeavesBlock::allowsSpawnOnLeaves));
    }
}
