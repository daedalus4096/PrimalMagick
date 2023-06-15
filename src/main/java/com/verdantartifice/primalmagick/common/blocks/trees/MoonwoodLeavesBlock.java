package com.verdantartifice.primalmagick.common.blocks.trees;

import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

/**
 * Block definition for moonwood leaves.  They are decorative blocks that fade out of existence and become indestructable during the day.
 * 
 * @author Daedalus4096
 */
public class MoonwoodLeavesBlock extends AbstractPhasingLeavesBlock {
    public MoonwoodLeavesBlock() {
        super(Block.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).randomTicks().noOcclusion().sound(SoundType.GRASS).lightLevel((state) -> {
            return state.getValue(PHASE).getLightLevel();
        }).isSuffocating((state, blockReader, pos) -> {
            return false;
        }).isViewBlocking((state, blockReader, pos) -> {
            return false;
        }).isRedstoneConductor((state, blockReader, pos) -> {
            return false;
        }).isValidSpawn(AbstractPhasingLeavesBlock::allowsSpawnOnLeaves));
    }

    @Override
    public TimePhase getCurrentPhase(LevelAccessor world) {
        return TimePhase.getMoonPhase(world);
    }
}
