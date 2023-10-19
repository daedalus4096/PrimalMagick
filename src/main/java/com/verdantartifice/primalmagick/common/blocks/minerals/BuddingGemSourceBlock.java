package com.verdantartifice.primalmagick.common.blocks.minerals;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

/**
 * Definition of a block that grows gem clusters like amethyst geodes.  Unlike amethyst geodes, these
 * blocks are imperfect and have a chance to decay every time they create or grow a bud, eventually
 * decaying into inert gem blocks.
 * 
 * @author Daedalus4096
 */
public class BuddingGemSourceBlock extends Block {
    protected static final int GROWTH_CHANCE = 5;
    
    protected final GemBudType gemType;
    protected final Supplier<BuddingGemClusterBlock> initialBudSupplier;
    protected final Supplier<Block> decayBlockSupplier;
    protected final float decayChance;
    
    public BuddingGemSourceBlock(GemBudType gemType, Supplier<BuddingGemClusterBlock> initialBudSupplier, Supplier<Block> decayBlockSupplier, float decayChance, Block.Properties properties) {
        super(properties);
        this.gemType = gemType;
        this.initialBudSupplier = initialBudSupplier;
        this.decayBlockSupplier = decayBlockSupplier;
        this.decayChance = decayChance;
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        // TODO Auto-generated method stub
        super.randomTick(pState, pLevel, pPos, pRandom);
    }

    @Override
    public @Nullable PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }
}
